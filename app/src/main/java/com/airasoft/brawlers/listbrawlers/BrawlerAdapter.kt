package com.airasoft.brawlers.listbrawlers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.airasoft.brawlers.R
import com.airasoft.brawlers.model.Brawler

class BrawlerAdapter(
    private var brawlerList: List<Brawler>,
    private val onClickListener: (Brawler) -> Unit,
    private val onClickEdit: (Brawler) -> Unit,
    private val onClickDelete: (Brawler) -> Unit
) : RecyclerView.Adapter<BrawlerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrawlerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return BrawlerViewHolder(layoutInflater.inflate(R.layout.list_brawlers_item, parent, false))
    }

    override fun onBindViewHolder(holder: BrawlerViewHolder, position: Int) {
        val item = brawlerList[position]
        holder.render(item, onClickListener, onClickEdit, onClickDelete)
    }

    override fun getItemCount(): Int = brawlerList.size

    fun updateBrawlerList(brawlerList: List<Brawler>) {
        this.brawlerList = brawlerList
        notifyDataSetChanged()
    }
}

