package com.example.myfirstapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.myfirstapp.database.SharedPreferenceDatabase
import com.example.myfirstapp.database.DBHelper
import com.example.myfirstapp.databinding.ActivityLoginBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    lateinit var sharedPreferenceDatabase: SharedPreferenceDatabase
    lateinit var dbHelper: DBHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferenceDatabase= SharedPreferenceDatabase(this@LoginActivity)
        dbHelper= DBHelper.getDB(this@LoginActivity)

        val email= sharedPreferenceDatabase.getData("email","")
        if (email != "") {
            val intent = Intent(this, UserListActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.tvSignUpRegistration.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnLogin.setOnClickListener {

            CoroutineScope(Dispatchers.IO).launch {
                val email = binding.edEmailLogin.text.toString()
                val password = binding.edPasswordLogin.text.toString()

                val userEmail = dbHelper.userDao().getUserByEmail(email =email)
                val userPassword = dbHelper.userDao().getUserByPassword(password = password)
                if (userEmail != null && userPassword !=null) {
                    withContext(Dispatchers.Main) {
                        val intent = Intent(this@LoginActivity, UserListActivity::class.java)
                        sharedPreferenceDatabase.saveData("email",email)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@LoginActivity, "Invalid Email or Password", Toast.LENGTH_LONG).show()
                    }
                }
            }


        }
    }

}