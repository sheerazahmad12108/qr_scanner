package com.example.qrcode

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bld.qrscanner.R
import com.example.qrcode.data.ScanHistory


class TabScanListAdapter(private var listItems: List<ScanHistory>) :
    RecyclerView.Adapter<TabScanListAdapter.TabScanItemViewHolder>() {

    private var onClickListener: OnClickListener? = null
    private var onCheckChangeListener: OnCheckChangeListener? = null
    var onLongClick = false

    inner class TabScanItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val link = view.findViewById<TextView>(R.id.tvLink)
        private val date = view.findViewById<TextView>(R.id.tvDate)
        val favourite = view.findViewById<ImageView>(R.id.ivFavourite)
        val cbDelete = view.findViewById<CheckBox>(R.id.cbDelete)

        fun bind(tabScanItem: ScanHistory) {
            link.text = tabScanItem.link
            date.text = tabScanItem.date
            if (tabScanItem.favourite == true) {
                favourite.setImageResource(R.drawable.favourite_selected_icon)
            } else {
                favourite.setImageResource(R.drawable.favourite_unselected_icon)
            }
            if (onLongClick) {
                favourite.visibility = View.GONE
                cbDelete.visibility = View.VISIBLE
            } else {
                favourite.visibility = View.VISIBLE
                cbDelete.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TabScanItemViewHolder {
        return TabScanItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.tab_scan_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    override fun onBindViewHolder(holder: TabScanItemViewHolder, position: Int) {
        val item = listItems[position]
        holder.bind(item)

        holder.itemView.setOnClickListener {
            if (!onLongClick) {
                onClickListener?.onClick(position, item)
            }
        }

//        holder.itemView.setOnLongClickListener {
//            onLongClick = true
//            notifyDataSetChanged()
////            holder.favourite.visibility = View.GONE
////            holder.cbDelete.visibility = View.VISIBLE
//            false
//        }

        holder.favourite.setOnClickListener {
            onClickListener?.onClickFavourite(position, item)
        }

        holder.cbDelete.setOnCheckedChangeListener { _, isChecked ->
            onCheckChangeListener?.onCheckChanged(position, item, isChecked)
        }
    }

    interface OnClickListener {
        fun onClick(position: Int, model: ScanHistory)
        fun onClickFavourite(position: Int, model: ScanHistory)
    }

    interface OnCheckChangeListener {
        fun onCheckChanged(position: Int, model: ScanHistory, isChecked: Boolean)
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    fun setonCheckChangeListener(onCheckChangeListener: OnCheckChangeListener) {
        this.onCheckChangeListener = onCheckChangeListener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterList(filterList: List<ScanHistory>) {
        listItems = filterList
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setRecords(list: List<ScanHistory>) {
        listItems = list
        notifyDataSetChanged()
    }
}