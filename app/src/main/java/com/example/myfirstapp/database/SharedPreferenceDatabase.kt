package com.example.myfirstapp.database

import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceDatabase(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("SharePref", Context.MODE_PRIVATE)

    fun saveData(key:String,value:String){
        val editor= sharedPreferences.edit()
        editor.putString(key,value)
        editor.apply()
    }

    fun getData(key: String,default:String):String{
        return sharedPreferences.getString(key,default) ?:default
    }

    fun clearData(){
        val editor=sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
}