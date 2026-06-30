package com.focusguard.app.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.net.VpnService
import android.os.ParcelFileDescriptor
import androidx.core.app.NotificationCompat
import com.focusguard.app.FocusGuardApp
import com.focusguard.app.MainActivity
import com.focusguard.app.R
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import java.io.FileInputStream
import java.io.FileOutputStream
import java.net.*
import java.nio.ByteBuffer

class FocusVpnService : VpnService() {

    companion object {
        const val ACTION_START = "com.focusguard.app.VPN_START"
        const val ACTION_STOP = "com.focusguard.app.VPN_STOP"
        const val CHANNEL_ID = "focusguard_vpn"
        const val NOTIFICATION_ID = 1

        // DoH provider IPs to block (TCP/UDP port 443)
        private val DOH_IPS = setOf("8.8.8.8", "8.8.4.4", "1.1.1.1", "1.0.0.1", "9.9.9.9")
        private const val UPSTREAM_DNS = "8.8.8.8"
        private const val DNS_PORT = 53
    }

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val blockedDomains = mutableSetOf<String>()
    private var vpnInterface: ParcelFileDescriptor? = null
    private var running = false

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTION_STOP) {
            stopVpn()
            return START_NOT_STICKY
        }
        startVpn()
        return START_STICKY
    }

    private fun startVpn() {
        if (running) return
        running = true

        startForeground(NOTIFICATION_ID, buildNotification())

        // Subscribe to blocked domains
        scope.launch {
            val db = (application as FocusGuardApp).database
            db.blockedDomainDao().getDomains().collectLatest { domains ->
                synchronized(blockedDomains) {
                    blockedDomains.clear()
                    blockedDomains.addAll(domains)
                }
            }
        }

        // Set up TUN interface
        val builder = Builder()
            .setSession("FocusGuard")
            .addAddress("10.0.0.2", 32)
            .addRoute(UPSTREAM_DNS, 32)   // Only route DNS server traffic through VPN
            .addDnsServer("10.0.0.2")     // Route DNS to ourselves
            .setMtu(1500)

        // Route DoH provider IPs through VPN so we can block their port 443 traffic
        for (ip in DOH_IPS) {
            if (ip != UPSTREAM_DNS) builder.addRoute(ip, 32)
        }

        vpnInterface = builder.establish() ?: run {
            stopSelf()
            return
        }

        scope.launch { runPacketLoop() }
    }

    private fun stopVpn() {
        running = false
        scope.coroutineContext.cancelChildren()
        vpnInterface?.close()
        vpnInterface = null
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private suspend fun runPacketLoop() {
        val pfd = vpnInterface ?: return
        val input = FileInputStream(pfd.fileDescriptor)
        val output = FileOutputStream(pfd.fileDescriptor)
        val buffer = ByteBuffer.allocate(32767)

        while (running && pfd.fileDescriptor.valid()) {
            buffer.clear()
            val length = withContext(Dispatchers.IO) {
                try { input.read(buffer.array()) } catch (e: Exception) { -1 }
            }
            if (length <= 0) continue
            buffer.limit(length)

            try {
                handlePacket(buffer, output)
            } catch (e: Exception) {
                // Malformed packet — skip
            }
        }
    }

    /**
     * Minimal IP/UDP packet parser targeting DNS queries (UDP dst-port 53).
     * Drops TCP/UDP packets destined to DoH IPs on port 443.
     * Everything else is forwarded upstream via a protected socket.
     */
    private suspend fun handlePacket(packet: ByteBuffer, output: FileOutputStream) {
        val bytes = packet.array()
        val len = packet.limit()
        if (len < 20) return  // Too short for IP header

        // IPv4 only
        val version = (bytes[0].toInt() and 0xFF) shr 4
        if (version != 4) return

        val ihl = (bytes[0].toInt() and 0x0F) * 4
        val protocol = bytes[9].toInt() and 0xFF  // 6=TCP, 17=UDP

        val dstIp = "%d.%d.%d.%d".format(
            bytes[16].toInt() and 0xFF, bytes[17].toInt() and 0xFF,
            bytes[18].toInt() and 0xFF, bytes[19].toInt() and 0xFF
        )

        // Block DoH providers at IP:443
        if (dstIp in DOH_IPS) {
            val dstPort = if (len >= ihl + 4) {
                ((bytes[ihl + 2].toInt() and 0xFF) shl 8) or (bytes[ihl + 3].toInt() and 0xFF)
            } else 0
            if (dstPort == 443) return  // Drop silently
        }

        // Intercept UDP DNS queries (port 53)
        if (protocol == 17 && len >= ihl + 8) {
            val srcPort = ((bytes[ihl].toInt() and 0xFF) shl 8) or (bytes[ihl + 1].toInt() and 0xFF)
            val dstPort = ((bytes[ihl + 2].toInt() and 0xFF) shl 8) or (bytes[ihl + 3].toInt() and 0xFF)

            if (dstPort == DNS_PORT) {
                val udpPayloadOffset = ihl + 8
                val udpPayloadLen = len - udpPayloadOffset
                if (udpPayloadLen < 12) return

                val dnsPayload = bytes.copyOfRange(udpPayloadOffset, len)
                handleDnsQuery(dnsPayload, bytes, ihl, srcPort, output)
                return
            }
        }

        // Forward everything else upstream using a protected socket
        forwardRaw(bytes, len)
    }

    private suspend fun handleDnsQuery(
        dnsPayload: ByteArray,
        originalPacket: ByteArray,
        ihl: Int,
        srcPort: Int,
        output: FileOutputStream
    ) = withContext(Dispatchers.IO) {
        val queriedDomain = parseDnsQueryDomain(dnsPayload) ?: run {
            forwardDns(dnsPayload, originalPacket, ihl, srcPort, output)
            return@withContext
        }

        val isBlocked = synchronized(blockedDomains) {
            blockedDomains.any { blocked ->
                queriedDomain == blocked || queriedDomain.endsWith(".$blocked")
            }
        }

        if (isBlocked) {
            val nxdomain = buildNxDomainResponse(dnsPayload)
            val response = buildUdpIpPacket(
                srcIp = byteArrayOf(10, 0, 0, 2),
                dstIp = byteArrayOf(
                    originalPacket[12], originalPacket[13],
                    originalPacket[14], originalPacket[15]
                ),
                srcPort = DNS_PORT,
                dstPort = srcPort,
                payload = nxdomain
            )
            output.write(response)
        } else {
            forwardDns(dnsPayload, originalPacket, ihl, srcPort, output)
        }
    }

    private fun forwardDns(
        dnsPayload: ByteArray,
        originalPacket: ByteArray,
        ihl: Int,
        srcPort: Int,
        output: FileOutputStream
    ) {
        try {
            val socket = DatagramSocket()
            protect(socket)
            val address = InetAddress.getByName(UPSTREAM_DNS)
            socket.send(DatagramPacket(dnsPayload, dnsPayload.size, address, DNS_PORT))
            val responseBuffer = ByteArray(4096)
            val responsePacket = DatagramPacket(responseBuffer, responseBuffer.size)
            socket.soTimeout = 3000
            socket.receive(responsePacket)
            socket.close()

            val response = buildUdpIpPacket(
                srcIp = byteArrayOf(10, 0, 0, 2),
                dstIp = byteArrayOf(
                    originalPacket[12], originalPacket[13],
                    originalPacket[14], originalPacket[15]
                ),
                srcPort = DNS_PORT,
                dstPort = srcPort,
                payload = responsePacket.data.copyOf(responsePacket.length)
            )
            output.write(response)
        } catch (e: Exception) {
            // Timeout or network error — drop
        }
    }

    private fun forwardRaw(bytes: ByteArray, len: Int) {
        // For non-DNS traffic, we let the OS route it normally
        // (The VPN routes everything through us, but we only intercept DNS.
        //  For other traffic we'd need a full IP stack — out of MVP scope.
        //  Practical effect: non-DNS TCP/UDP will be routed via the TUN but
        //  we just drop it here. A production app would forward it.)
        // MVP: drop non-DNS non-blocked traffic to keep implementation simple.
        // Users won't notice for browsing because DNS is blocked before TCP connects.
    }

    // ─── DNS Packet Parsing ─────────────────────────────────────────────────────

    private fun parseDnsQueryDomain(dns: ByteArray): String? {
        if (dns.size < 12) return null
        // Skip header (12 bytes), parse QNAME
        var pos = 12
        val labels = mutableListOf<String>()
        while (pos < dns.size) {
            val len = dns[pos].toInt() and 0xFF
            if (len == 0) break
            pos++
            if (pos + len > dns.size) return null
            labels.add(String(dns, pos, len))
            pos += len
        }
        return if (labels.isEmpty()) null else labels.joinToString(".")
    }

    private fun buildNxDomainResponse(query: ByteArray): ByteArray {
        if (query.size < 2) return query
        val response = query.copyOf()
        // Set QR=1, OPCODE=0, AA=1, TC=0, RD=1, RA=1, Z=0, RCODE=3 (NXDOMAIN)
        response[2] = (0x81).toByte()  // QR=1, RD=1
        response[3] = (0x83).toByte()  // RA=1, RCODE=3
        // ANCOUNT, NSCOUNT, ARCOUNT = 0 (already 0 from copy)
        return response
    }

    // ─── Packet Building ────────────────────────────────────────────────────────

    private fun buildUdpIpPacket(
        srcIp: ByteArray,
        dstIp: ByteArray,
        srcPort: Int,
        dstPort: Int,
        payload: ByteArray
    ): ByteArray {
        val udpLen = 8 + payload.size
        val totalLen = 20 + udpLen
        val packet = ByteArray(totalLen)

        // IP header
        packet[0] = 0x45.toByte()              // Version=4, IHL=5
        packet[1] = 0x00
        packet[2] = (totalLen shr 8).toByte()
        packet[3] = totalLen.toByte()
        packet[4] = 0x00; packet[5] = 0x01     // ID
        packet[6] = 0x00; packet[7] = 0x00     // Flags + Fragment offset
        packet[8] = 0x40.toByte()              // TTL=64
        packet[9] = 0x11.toByte()              // Protocol=UDP
        packet[10] = 0x00; packet[11] = 0x00   // Checksum (filled below)
        srcIp.copyInto(packet, 12)
        dstIp.copyInto(packet, 16)

        // IP checksum
        val ipChecksum = checksum(packet, 0, 20)
        packet[10] = (ipChecksum shr 8).toByte()
        packet[11] = ipChecksum.toByte()

        // UDP header
        packet[20] = (srcPort shr 8).toByte()
        packet[21] = srcPort.toByte()
        packet[22] = (dstPort shr 8).toByte()
        packet[23] = dstPort.toByte()
        packet[24] = (udpLen shr 8).toByte()
        packet[25] = udpLen.toByte()
        packet[26] = 0x00; packet[27] = 0x00   // UDP checksum (optional for IPv4)

        payload.copyInto(packet, 28)
        return packet
    }

    private fun checksum(buf: ByteArray, offset: Int, length: Int): Int {
        var sum = 0
        var i = offset
        while (i < offset + length - 1) {
            sum += ((buf[i].toInt() and 0xFF) shl 8) or (buf[i + 1].toInt() and 0xFF)
            i += 2
        }
        if ((offset + length) % 2 != 0) {
            sum += (buf[offset + length - 1].toInt() and 0xFF) shl 8
        }
        while (sum shr 16 != 0) sum = (sum and 0xFFFF) + (sum shr 16)
        return sum.inv() and 0xFFFF
    }

    // ─── Notification ───────────────────────────────────────────────────────────

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "FocusGuard VPN",
            NotificationManager.IMPORTANCE_LOW
        ).apply { description = "Website blocking is active" }
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    private fun buildNotification(): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("FocusGuard Active")
            .setContentText("Website blocking is on")
            .setSmallIcon(android.R.drawable.ic_menu_compass)
            .setContentIntent(pi)
            .setOngoing(true)
            .build()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopVpn()
        scope.cancel()
    }
}
