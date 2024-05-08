package com.example.events

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class RecyclerUserCmpPackageAdapter (
    private val mContext: Context,
    private val mList: List<PackageModel>
) : RecyclerView.Adapter<RecyclerUserCmpPackageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.package_usr_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val packageItem = mList[position]
        holder.titleTV.text = packageItem.title + " - " + packageItem.pcount + " ppl"
        holder.priceItemTV.text = "\u20B9" + packageItem.price
        holder.descItemTV.text = packageItem.desc

        if (FirebaseAuth.getInstance().currentUser != null) {
            holder.editItemButton.visibility = View.VISIBLE
        } else {
            holder.editItemButton.visibility = View.GONE
        }

        holder.editItemButton.setOnClickListener {
            Toast.makeText(mContext,"Booking done",Toast.LENGTH_SHORT).show()
            val intent = Intent(it.context, UserBookConfirm::class.java).apply {
                putExtra("packID", packageItem.packID)
                putExtra("cmpID", packageItem.cmpID)
                putExtra("pkgName", packageItem.title)
                putExtra("pkgPrice", packageItem.price)
                putExtra("pkgPpl", packageItem.pcount)
                putExtra("pkgDesc", packageItem.desc)
            }
            it.context.startActivity(intent)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTV: TextView = itemView.findViewById(R.id.titleTV)
        val priceItemTV: TextView = itemView.findViewById(R.id.priceItemTV)
        val descItemTV: TextView = itemView.findViewById(R.id.descItemTV)
        val editItemButton: Button = itemView.findViewById(R.id.editItemButton)
    }
}