package com.focusguard.app.ui.websites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.focusguard.app.data.BlockedDomain
import com.focusguard.app.databinding.ItemWebsiteBinding

class WebsiteListAdapter(
    private val onDelete: (String) -> Unit
) : ListAdapter<BlockedDomain, WebsiteListAdapter.ViewHolder>(DIFF) {

    inner class ViewHolder(private val binding: ItemWebsiteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BlockedDomain) {
            binding.textDomain.text = item.domain
            binding.btnDelete.setOnClickListener { onDelete(item.domain) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemWebsiteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<BlockedDomain>() {
            override fun areItemsTheSame(a: BlockedDomain, b: BlockedDomain) = a.domain == b.domain
            override fun areContentsTheSame(a: BlockedDomain, b: BlockedDomain) = a == b
        }
    }
}
