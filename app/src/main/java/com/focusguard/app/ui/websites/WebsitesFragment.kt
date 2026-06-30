package com.focusguard.app.ui.websites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.focusguard.app.databinding.FragmentWebsitesBinding
import kotlinx.coroutines.launch

class WebsitesFragment : Fragment() {

    private var _binding: FragmentWebsitesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: WebsitesViewModel by viewModels()

    private lateinit var adapter: WebsiteListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentWebsitesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = WebsiteListAdapter { domain -> viewModel.removeDomain(domain) }
        binding.recyclerWebsites.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerWebsites.adapter = adapter

        lifecycleScope.launch {
            viewModel.blockedDomains.collect { domains ->
                adapter.submitList(domains)
            }
        }

        binding.btnAddDomain.setOnClickListener { addDomain() }
        binding.editDomain.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) { addDomain(); true } else false
        }
    }

    private fun addDomain() {
        val text = binding.editDomain.text.toString()
        viewModel.addDomain(text)
        binding.editDomain.text?.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
