package com.airasoft.brawlers.listbrawlers

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.airasoft.brawlers.model.Brawler
import com.airasoft.brawlers.databinding.ListBrawlersItemBinding
import com.bumptech.glide.Glide

class BrawlerAdapter(private val clickListener: BrawlerListener, private val viewModel: ListBrawlersViewModel): ListAdapter<Brawler, BrawlerAdapter.ViewHolder>(BrawlerDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListBrawlersItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener, viewModel)
    }

    class ViewHolder (private val binding: ListBrawlersItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(brawler: Brawler, clickListener: BrawlerListener, viewModel: ListBrawlersViewModel) {
            Glide.with(itemView.context).load(brawler.brawlerImage.toUri()).into(binding.brawlerImage)
            binding.brawler = brawler
            binding.clickListener = clickListener

            binding.editBrawler.setOnClickListener {
                viewModel.onEditBrawler(brawler)
            }

            binding.deleteBrawler.setOnClickListener {
                viewModel.onDeleteBrawler(brawler.brawlerId)
            }
        }
    }
}

class BrawlerListener(val clickListener: (brawler: Brawler) -> Unit) {
    fun onClick(brawler: Brawler) = clickListener(brawler)
}

class BrawlerDiffCallback: DiffUtil.ItemCallback<Brawler>() {
    override fun areItemsTheSame(oldItem: Brawler, newItem: Brawler): Boolean {
        return oldItem.brawlerId == newItem.brawlerId
    }

    override fun areContentsTheSame(oldItem: Brawler, newItem: Brawler): Boolean {
        return oldItem == newItem
    }
}

