package com.focusguard.app.ui.home

import android.content.Intent
import android.net.VpnService
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.focusguard.app.databinding.FragmentHomeBinding
import com.focusguard.app.services.FocusVpnService

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

    private val vpnPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            startVpnService()
        } else {
            binding.switchWebBlocking.isChecked = false
            viewModel.setVpnEnabled(false)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.vpnEnabled.observe(viewLifecycleOwner) { enabled ->
            binding.switchWebBlocking.isChecked = enabled
        }

        binding.switchWebBlocking.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val intent = VpnService.prepare(requireContext())
                if (intent != null) {
                    vpnPermissionLauncher.launch(intent)
                } else {
                    startVpnService()
                }
            } else {
                stopVpnService()
            }
            viewModel.setVpnEnabled(isChecked)
        }
    }

    private fun startVpnService() {
        val intent = Intent(requireContext(), FocusVpnService::class.java).apply {
            action = FocusVpnService.ACTION_START
        }
        requireContext().startForegroundService(intent)
    }

    private fun stopVpnService() {
        val intent = Intent(requireContext(), FocusVpnService::class.java).apply {
            action = FocusVpnService.ACTION_STOP
        }
        requireContext().startService(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
