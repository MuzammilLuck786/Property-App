package com.example.myfirstapp.activities

import android.os.Bundle
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myfirstapp.adapter.PropertyAdapter
import com.example.myfirstapp.database.DBHelper
import com.example.myfirstapp.database.PropertiesEntity
import com.example.myfirstapp.databinding.ActivityPropertyListBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PropertyListActivity : AppCompatActivity() {
    lateinit var dbHelper: DBHelper
    lateinit var userProperties: List<PropertiesEntity>
    lateinit var propertyAdapter: PropertyAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityPropertyListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DBHelper.getDB(this@PropertyListActivity)
        binding.rvPropertyList.layoutManager = LinearLayoutManager(this@PropertyListActivity)

        CoroutineScope(Dispatchers.IO).launch {
            userProperties = dbHelper.userDao().getAllProperties()

            withContext(Dispatchers.Main) {
                propertyAdapter = PropertyAdapter(
                    this@PropertyListActivity, userProperties as MutableList<PropertiesEntity>
                )
                binding.rvPropertyList.adapter = propertyAdapter
                propertyAdapter.notifyDataSetChanged()
            }
        }


        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    CoroutineScope(Dispatchers.IO).launch {
                        val list = dbHelper.userDao().getFilteredProperties("%$newText%")
                        withContext(Dispatchers.Main){
                            propertyAdapter.submitList(list)
                        }
                    }
                }
                return true
            }
        })
    }
}

