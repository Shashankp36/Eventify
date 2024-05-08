package com.example.events

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerCompanyHomeAdapter(private val mContext: Context, private val mList: List<PackageModel>) : RecyclerView.Adapter<RecyclerCompanyHomeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.package_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val packageItem = mList[position]
        holder.titleTV.text = "${packageItem.title} - ${packageItem.pcount} ppl"
        holder.priceItemTV.text = "\u20B9${packageItem.price}"
        holder.descItemTV.text = packageItem.desc
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTV: TextView = itemView.findViewById(R.id.titleTV)
        val priceItemTV: TextView = itemView.findViewById(R.id.priceItemTV)
        val descItemTV: TextView = itemView.findViewById(R.id.descItemTV)
    }
}
