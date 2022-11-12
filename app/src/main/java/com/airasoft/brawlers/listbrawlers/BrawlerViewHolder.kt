package com.airasoft.brawlers.listbrawlers

import android.view.View
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.airasoft.brawlers.databinding.ListBrawlersItemBinding
import com.airasoft.brawlers.model.Brawler
import com.bumptech.glide.Glide

class BrawlerViewHolder(view: View): ViewHolder(view) {

    private val binding = ListBrawlersItemBinding.bind(view)

    fun render(brawler: Brawler, onClickListener: (Brawler) -> Unit, onClickEdit: (Brawler) -> Unit, onClickDelete: (Brawler) -> Unit) {
        binding.brawlerName.text = brawler.brawlerName
        binding.brawlerClass.text = brawler.brawlerClass
        binding.brawlerType.text = brawler.brawlerType
        Glide.with(binding.brawlerImage.context).load(brawler.brawlerImage.toUri()).into(binding.brawlerImage)
        itemView.setOnClickListener { onClickListener(brawler) }
        binding.editBrawler.setOnClickListener { onClickEdit(brawler) }
        binding.deleteBrawler.setOnClickListener { onClickDelete(brawler) }
    }
}