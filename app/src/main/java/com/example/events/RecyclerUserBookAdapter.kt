package com.example.events

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

class RecyclerUserBookAdapter (private val mContext: Context, private val mList: List<BookingModel>) : RecyclerView.Adapter<RecyclerUserBookAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerUserBookAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.book_usr_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerUserBookAdapter.ViewHolder, position: Int) {
        val PackageItem = mList[position]
        holder.titleTV.text = PackageItem.pkgName
        holder.priceTV.text = "\u20B9"+PackageItem.pkgPrice
        holder.usrDesc.text = PackageItem.pkgDesc

        if (PackageItem.status.equals("wait")){
            holder.usrApprove.text ="Pending! Waiting for approval."
            holder.usrApprove.setTextColor(ContextCompat.getColor(mContext, R.color.red))
        }
        else if(PackageItem.status.equals("approve")){
            holder.usrApprove.text ="Approved."
            holder.usrApprove.setTextColor(ContextCompat.getColor(mContext, R.color.green))
        }
        else{
            holder.usrApprove.text ="Declined."
            holder.usrApprove.setTextColor(ContextCompat.getColor(mContext, R.color.red))
        }
        holder.usrCall.setOnClickListener {
            val dBook = FirebaseDatabase.getInstance().getReference("company").child(PackageItem.cmpID.toString())
            val valueEventListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.getValue<CompanyModel>() != null) {
                        val key = dataSnapshot.key
                        if (key == PackageItem.cmpID.toString()) {
                            val phone=dataSnapshot.child("phone").value.toString()
                            val i = Intent(Intent.ACTION_VIEW, Uri.parse("tel:$phone"))
                            it.context.startActivity(i)
                        }
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {
                }
            }
            dBook.addValueEventListener(valueEventListener)
        }
        holder.usrDel.setOnClickListener {
            val builder = AlertDialog.Builder(it.context)
            val alertView: View = LayoutInflater.from(it.context).inflate(R.layout.dialog_delete, null)
            val deButton: Button =alertView.findViewById(R.id.btn_action_delete)
            builder.setView(alertView)
            val alertDialog=builder.create()
            alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog.window?.setGravity(Gravity.CENTER)
            alertDialog.show()
            alertDialog.window?.setLayout(900,800)
            deButton.setOnClickListener {
                val dBook = FirebaseDatabase.getInstance().getReference("bookings").child(PackageItem.bookID.toString())
                dBook.removeValue()
                Toast.makeText(it.context, "Booking Removed", Toast.LENGTH_SHORT).show()
                alertDialog.dismiss()
            }
        }
    }
    override fun getItemCount(): Int {
        return mList.size
    }
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val titleTV: TextView = itemView.findViewById(R.id.titleTV)
        val priceTV: TextView = itemView.findViewById(R.id.priceTV)
        val usrDesc: TextView = itemView.findViewById(R.id.usrDesc)
        val usrApprove: TextView = itemView.findViewById(R.id.usr_approve)
        val usrCall: ImageButton = itemView.findViewById(R.id.usr_call) ?: throw IllegalStateException("usr_call not found in item layout")
        val usrDel: ImageButton = itemView.findViewById(R.id.usr_del) ?: throw IllegalStateException("usr_del not found in item layout")
    }
}