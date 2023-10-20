package com.example.myfirstapp.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstapp.activities.PropertyService
import com.example.myfirstapp.R
import com.example.myfirstapp.database.DBHelper
import com.example.myfirstapp.database.PropertiesEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class PropertyAdapter(
    private val context: Context,
    private var propertyList: MutableList<PropertiesEntity>,
) : RecyclerView.Adapter<PropertyAdapter.ViewHolder>() {

    private var dbHelper: DBHelper = DBHelper.getDB(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.rvpropertylist,
            parent, false
        )
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvname.text="Name: ${propertyList.get(position).name}"
        holder.tvnumber.text="Number: ${propertyList.get(position).phone}"
        holder.tvArea.text= "Area : ${propertyList.get(position).area}"
        holder.tvCity.text= "City : ${propertyList.get(position).city}"
        holder.tvAddress.text= "Address : ${propertyList.get(position).address}"
        holder.tvMarla.text= "Marla : ${propertyList.get(position).marla}"
        holder.tvPurpose.text= "Purpose : ${propertyList.get(position).dataPurpose}"
        holder.tvRooms.text= "No of Rooms : ${propertyList.get(position).noOfRooms}"
        holder.tvKitchens.text= "No of Kitchens : ${propertyList.get(position).noOfKitchens}"
        holder.tvWashroom.text= "No of Washrooms : ${propertyList.get(position).noOfWashroom}"
        holder.tvFloor.text= "Floor : ${propertyList.get(position).noOfFloors}"
        holder.tvInterior.text= "Interior : ${propertyList.get(position).furnishFacility}"

        holder.itemView.setOnLongClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Delete Data")
                .setMessage("Are you sure you want to Delete?")
                .setPositiveButton("Yes") { dialogInterface, _ ->
                    CoroutineScope(Dispatchers.IO).launch {
                        val propertyId = propertyList[position].id
                        dbHelper.userDao().deletebyPropertyid(propertyId)

                        withContext(Dispatchers.Main) {
                            propertyList.removeAt(position)
                            notifyItemRemoved(position)
                        }
                    }

                }
                .setNegativeButton("No") { dialogInterface, _ ->
                    dialogInterface.dismiss()
                }
            builder.show()
            true
        }

        holder.itemView.setOnClickListener {
            val intent=Intent(context, PropertyService::class.java)
            intent.putExtra("Properties",propertyList[position])
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return propertyList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvname = itemView.findViewById<TextView>(R.id.Name)
        val tvnumber = itemView.findViewById<TextView>(R.id.Phone)
        val tvArea = itemView.findViewById<TextView>(R.id.tvArea)
        val tvAddress= itemView.findViewById<TextView>(R.id.tvAddress)
        val tvCity= itemView.findViewById<TextView>(R.id.tvCity)
        val tvMarla= itemView.findViewById<TextView>(R.id.tvMarla)
        val tvPurpose= itemView.findViewById<TextView>(R.id.tvPurpose)
        val tvRooms= itemView.findViewById<TextView>(R.id.tvRooms)
        val tvKitchens= itemView.findViewById<TextView>(R.id.tvKitchens)
        val tvWashroom= itemView.findViewById<TextView>(R.id.tvWashroom)
        val tvFloor= itemView.findViewById<TextView>(R.id.tvFloor)
        val tvInterior= itemView.findViewById<TextView>(R.id.tvInterior)

    }

    fun submitList(list: List<PropertiesEntity>?) {
        propertyList.clear()
        this.propertyList= list as MutableList<PropertiesEntity>
        notifyDataSetChanged()
    }

}
