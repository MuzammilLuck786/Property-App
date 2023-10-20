package com.example.myfirstapp.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstapp.R
import com.example.myfirstapp.database.UserEntity

class UserAdapter(var context: Context,private val userList: List<UserEntity>) : RecyclerView.Adapter<UserAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.rvlist,
            parent, false
        )
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvNameRv.text = userList[position].name
        holder.tvEmailRv.text= userList[position].email

        holder.itemView.setOnClickListener {
            Toast.makeText(context, "Calling ${userList[position].phone}", Toast.LENGTH_SHORT).show()
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${userList[position].phone}")
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvNameRv = itemView.findViewById<TextView>(R.id.tvNameRv)
        val tvEmailRv= itemView.findViewById<TextView>(R.id.tvEmailRv)

    }

}