package com.example.events

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class RecyclerCmpBookAdapter (var mContext: Context, private val mList: List<BookingModel>) : RecyclerView.Adapter<RecyclerCmpBookAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerCmpBookAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.book_cmp_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerCmpBookAdapter.ViewHolder, position: Int) {
        val PackageItem = mList[position]
        holder.titleTV.text = PackageItem.pkgName
        holder.priceTV.text = "\u20B9"+PackageItem.pkgPrice
        holder.packDes.text = PackageItem.pkgDesc
        val user=PackageItem.userID
        val userRef = user?.let { FirebaseDatabase.getInstance().getReference("users").child(it) }
        userRef?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val username = snapshot.child("cname").getValue(String::class.java)
                    val userEmail = snapshot.child("email").getValue(String::class.java)
                    holder.username.text = "Booked By:${username}"
                    holder.useremail.text = "Client mail:${userEmail}"
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
        if (PackageItem.status.equals("wait")){
            holder.cmpApprove.text ="Pending! Waiting for approval."
            holder.cmpApprove.setTextColor(ContextCompat.getColor(mContext, R.color.red))
        }
        else if(PackageItem.status.equals("approve")){
            holder.cmpApprove.text ="Approved."
            holder.approve.visibility=View.GONE
            holder.cmpApprove.setTextColor(ContextCompat.getColor(mContext, R.color.green))
        }
        else{
            holder.cmpApprove.text ="Declined."
            holder.cmpApprove.setTextColor(ContextCompat.getColor(mContext, R.color.red))
            holder.approve.visibility=View.GONE
            holder.decline.visibility=View.GONE
        }
        holder.approve.setOnClickListener {
            var bookID: String? = PackageItem.bookID.toString()
            var databaseCmpBook: DatabaseReference? = FirebaseDatabase.getInstance().getReference("bookings").child(bookID!!)
            val bookingModel= BookingModel(bookID,PackageItem.packID,PackageItem.cmpID,PackageItem.userID,PackageItem.pkgName,PackageItem.pkgPrice,PackageItem.pkgDesc,"approve")
            databaseCmpBook!!.setValue(bookingModel)
        }
        holder.decline.setOnClickListener {
            var bookID: String? = PackageItem.bookID.toString()
            var databaseCmpBook: DatabaseReference? = FirebaseDatabase.getInstance().getReference("bookings").child(bookID!!)
            val bookingModel= BookingModel(bookID,PackageItem.packID,PackageItem.cmpID,PackageItem.userID,PackageItem.pkgName,PackageItem.pkgPrice,PackageItem.pkgDesc,"decline")
            databaseCmpBook!!.setValue(bookingModel)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val titleTV: TextView = itemView.findViewById(R.id.titleTV)
        val priceTV: TextView = itemView.findViewById(R.id.priceTV)
        val packDes: TextView = itemView.findViewById(R.id.packDes)
        val username:TextView=itemView.findViewById(R.id.usernameTv)
        val useremail:TextView=itemView.findViewById(R.id.useremailtv)
        val cmpApprove: TextView = itemView.findViewById(R.id.cmp_approve)
        val approve: ImageButton = itemView.findViewById(R.id.approve)
        val decline: ImageButton = itemView.findViewById(R.id.decline)
    }
}