package com.example.qrcode

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bld.qrscanner.R
import com.example.qrcode.data.ScanHistory

class TabFavouriteListAdapter(private var listItems: List<ScanHistory>) :
    RecyclerView.Adapter<TabFavouriteListAdapter.TabFavouriteItemViewHolder>() {
        private var onItemClick: OnItemClick? = null

    inner class TabFavouriteItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val link = view.findViewById<TextView>(R.id.tvLink)
        private val date = view.findViewById<TextView>(R.id.tvDate)
        val favourite = view.findViewById<ImageView>(R.id.ivFavourite)

        fun bind(tabScanItem: ScanHistory) {
            link.text = tabScanItem.link
            date.text = tabScanItem.date
            favourite.setImageResource(R.drawable.favourite_selected_icon)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TabFavouriteItemViewHolder {
        return TabFavouriteItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.tab_scan_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    override fun onBindViewHolder(holder: TabFavouriteItemViewHolder, position: Int) {
        val item = listItems[position]
        holder.bind(item)

        holder.itemView.setOnClickListener {
            onItemClick?.onItemClick(position,item)
        }
    }

    interface OnItemClick{
        fun onItemClick(position: Int, model: ScanHistory)
    }

    fun setOnItemClick(onItemClick: OnItemClick){
        this.onItemClick = onItemClick
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterList(filterList: List<ScanHistory>) {
        listItems = filterList
        notifyDataSetChanged()
    }
}