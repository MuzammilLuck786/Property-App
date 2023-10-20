package com.example.myfirstapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myfirstapp.adapter.UserAdapter
import com.example.myfirstapp.database.DBHelper
import com.example.myfirstapp.databinding.ActivityUserListBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityUserListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dbHelper= DBHelper.getDB(this@UserListActivity)

        binding.btnPropertyService.setOnClickListener {
            val intent = Intent(this@UserListActivity, PropertyService ::class.java)
            startActivity(intent)
        }

        binding.btnUserList.setOnClickListener {

            binding.recyclerView.layoutManager= LinearLayoutManager(this@UserListActivity)

            CoroutineScope(Dispatchers.IO).launch {
                val userList = dbHelper.userDao().getAllUsers()

                withContext(Dispatchers.Main) {
                    val userAdapter = UserAdapter(this@UserListActivity,userList)
                    binding.recyclerView.adapter = userAdapter
                    userAdapter.notifyDataSetChanged()
                }
            }

        }

        binding.btnPropertylist.setOnClickListener {
            val intent=Intent(this@UserListActivity, PropertyListActivity::class.java)
            startActivity(intent)
        }

        binding.btnUserProfile.setOnClickListener {
            val intent = Intent(this, UserProfile ::class.java)
            startActivity(intent)
        }


    }
}