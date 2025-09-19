package com.example.qrcode

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bld.qrscanner.R

class CreateFragmentListAdapter(private var createList: List<CreateList>) :
    RecyclerView.Adapter<CreateFragmentListAdapter.CreateFragmentViewHolder>() {

        private lateinit var onItemClick: OnItemClick

    inner class CreateFragmentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvName = view.findViewById<TextView>(R.id.tvName)
        private val ivIcon = view.findViewById<ImageView>(R.id.ivIcon)

        fun bind(createList: CreateList) {
            tvName.text = createList.name
            ivIcon.setImageResource(createList.icon)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreateFragmentViewHolder {
        return CreateFragmentViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.create_list_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return createList.size
    }

    override fun onBindViewHolder(holder: CreateFragmentViewHolder, position: Int) {
        val item = createList[position]
        holder.bind(item)

        holder.itemView.setOnClickListener {
            onItemClick.onItemClick(position,item)
        }
    }

    interface OnItemClick{
        fun onItemClick(position: Int, model: CreateList)
    }

    fun setOnItemClick(onItemClick: OnItemClick){
        this.onItemClick = onItemClick
    }
}