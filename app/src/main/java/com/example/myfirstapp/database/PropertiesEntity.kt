package com.example.myfirstapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "properties")
data class PropertiesEntity (
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var name:String,
    var phone:String,
    var area: String,
    var address: String,
    var city: String,
    var marla: String,
    var noOfRooms: String,
    var noOfKitchens: String,
    var noOfWashroom: String,
    var noOfFloors: String,
    var furnishFacility:String,
    var dataPurpose:String): java.io.Serializable {

}