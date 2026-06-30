package com.focusguard.app.services;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000^\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010#\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\n\u0018\u0000 :2\u00020\u0001:\u0001:B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\f\u001a\u00020\rH\u0002J\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000fH\u0002J0\u0010\u0011\u001a\u00020\u000f2\u0006\u0010\u0012\u001a\u00020\u000f2\u0006\u0010\u0013\u001a\u00020\u000f2\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00152\u0006\u0010\u0017\u001a\u00020\u000fH\u0002J \u0010\u0018\u001a\u00020\u00152\u0006\u0010\u0019\u001a\u00020\u000f2\u0006\u0010\u001a\u001a\u00020\u00152\u0006\u0010\u001b\u001a\u00020\u0015H\u0002J\b\u0010\u001c\u001a\u00020\u001dH\u0002J0\u0010\u001e\u001a\u00020\u001d2\u0006\u0010\u001f\u001a\u00020\u000f2\u0006\u0010 \u001a\u00020\u000f2\u0006\u0010!\u001a\u00020\u00152\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\"\u001a\u00020#H\u0002J\u0018\u0010$\u001a\u00020\u001d2\u0006\u0010%\u001a\u00020\u000f2\u0006\u0010&\u001a\u00020\u0015H\u0002J6\u0010\'\u001a\u00020\u001d2\u0006\u0010\u001f\u001a\u00020\u000f2\u0006\u0010 \u001a\u00020\u000f2\u0006\u0010!\u001a\u00020\u00152\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\"\u001a\u00020#H\u0082@\u00a2\u0006\u0002\u0010(J\u001e\u0010)\u001a\u00020\u001d2\u0006\u0010*\u001a\u00020+2\u0006\u0010\"\u001a\u00020#H\u0082@\u00a2\u0006\u0002\u0010,J\b\u0010-\u001a\u00020\u001dH\u0016J\b\u0010.\u001a\u00020\u001dH\u0016J\"\u0010/\u001a\u00020\u00152\b\u00100\u001a\u0004\u0018\u0001012\u0006\u00102\u001a\u00020\u00152\u0006\u00103\u001a\u00020\u0015H\u0016J\u0012\u00104\u001a\u0004\u0018\u00010\u00052\u0006\u00105\u001a\u00020\u000fH\u0002J\u000e\u00106\u001a\u00020\u001dH\u0082@\u00a2\u0006\u0002\u00107J\b\u00108\u001a\u00020\u001dH\u0002J\b\u00109\u001a\u00020\u001dH\u0002R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006;"}, d2 = {"Lcom/focusguard/app/services/FocusVpnService;", "Landroid/net/VpnService;", "()V", "blockedDomains", "", "", "running", "", "scope", "Lkotlinx/coroutines/CoroutineScope;", "vpnInterface", "Landroid/os/ParcelFileDescriptor;", "buildNotification", "Landroid/app/Notification;", "buildNxDomainResponse", "", "query", "buildUdpIpPacket", "srcIp", "dstIp", "srcPort", "", "dstPort", "payload", "checksum", "buf", "offset", "length", "createNotificationChannel", "", "forwardDns", "dnsPayload", "originalPacket", "ihl", "output", "Ljava/io/FileOutputStream;", "forwardRaw", "bytes", "len", "handleDnsQuery", "([B[BIILjava/io/FileOutputStream;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "handlePacket", "packet", "Ljava/nio/ByteBuffer;", "(Ljava/nio/ByteBuffer;Ljava/io/FileOutputStream;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "onCreate", "onDestroy", "onStartCommand", "intent", "Landroid/content/Intent;", "flags", "startId", "parseDnsQueryDomain", "dns", "runPacketLoop", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "startVpn", "stopVpn", "Companion", "app_debug"})
public final class FocusVpnService extends android.net.VpnService {
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ACTION_START = "com.focusguard.app.VPN_START";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ACTION_STOP = "com.focusguard.app.VPN_STOP";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CHANNEL_ID = "focusguard_vpn";
    public static final int NOTIFICATION_ID = 1;
    @org.jetbrains.annotations.NotNull()
    private static final java.util.Set<java.lang.String> DOH_IPS = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String UPSTREAM_DNS = "8.8.8.8";
    private static final int DNS_PORT = 53;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineScope scope = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.Set<java.lang.String> blockedDomains = null;
    @org.jetbrains.annotations.Nullable()
    private android.os.ParcelFileDescriptor vpnInterface;
    private boolean running = false;
    @org.jetbrains.annotations.NotNull()
    public static final com.focusguard.app.services.FocusVpnService.Companion Companion = null;
    
    public FocusVpnService() {
        super();
    }
    
    @java.lang.Override()
    public void onCreate() {
    }
    
    @java.lang.Override()
    public int onStartCommand(@org.jetbrains.annotations.Nullable()
    android.content.Intent intent, int flags, int startId) {
        return 0;
    }
    
    private final void startVpn() {
    }
    
    private final void stopVpn() {
    }
    
    private final java.lang.Object runPacketLoop(kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    /**
     * Minimal IP/UDP packet parser targeting DNS queries (UDP dst-port 53).
     * Drops TCP/UDP packets destined to DoH IPs on port 443.
     * Everything else is forwarded upstream via a protected socket.
     */
    private final java.lang.Object handlePacket(java.nio.ByteBuffer packet, java.io.FileOutputStream output, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final java.lang.Object handleDnsQuery(byte[] dnsPayload, byte[] originalPacket, int ihl, int srcPort, java.io.FileOutputStream output, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final void forwardDns(byte[] dnsPayload, byte[] originalPacket, int ihl, int srcPort, java.io.FileOutputStream output) {
    }
    
    private final void forwardRaw(byte[] bytes, int len) {
    }
    
    private final java.lang.String parseDnsQueryDomain(byte[] dns) {
        return null;
    }
    
    private final byte[] buildNxDomainResponse(byte[] query) {
        return null;
    }
    
    private final byte[] buildUdpIpPacket(byte[] srcIp, byte[] dstIp, int srcPort, int dstPort, byte[] payload) {
        return null;
    }
    
    private final int checksum(byte[] buf, int offset, int length) {
        return 0;
    }
    
    private final void createNotificationChannel() {
    }
    
    private final android.app.Notification buildNotification() {
        return null;
    }
    
    @java.lang.Override()
    public void onDestroy() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010\"\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082T\u00a2\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00040\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\bX\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\r"}, d2 = {"Lcom/focusguard/app/services/FocusVpnService$Companion;", "", "()V", "ACTION_START", "", "ACTION_STOP", "CHANNEL_ID", "DNS_PORT", "", "DOH_IPS", "", "NOTIFICATION_ID", "UPSTREAM_DNS", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}