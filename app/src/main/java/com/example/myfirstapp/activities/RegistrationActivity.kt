package com.example.myfirstapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.myfirstapp.database.DBHelper
import com.example.myfirstapp.databinding.ActivityRegistrationBinding
import com.example.myfirstapp.database.UserEntity
import com.example.myfirstapp.others.Validation
import kotlinx.coroutines.*

class RegistrationActivity : AppCompatActivity() {
    lateinit var name1: String
    lateinit var phone: String
    lateinit var email: String
    lateinit var password: String
    lateinit var dbHelper: DBHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DBHelper.getDB(this@RegistrationActivity)

        binding.tvLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnRegistration.setOnClickListener {

            name1 = binding.edName.text.toString()
            phone = binding.edPhone.text.toString()
            email = binding.edEmail.text.toString()
            password = binding.edPassword.text.toString()

            if (Validation.nameValidation(name1, binding.edName) &&
                Validation.phoneValidation(phone, binding.edPhone) &&
                Validation.emailValidation( email, binding.edEmail) &&
                Validation.passwordValidation(password, binding.edPassword)
            ) {
                CoroutineScope(Dispatchers.IO).launch {
                    val user = dbHelper.userDao().getUserByEmail(email)
                    if (user != null) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                this@RegistrationActivity,
                                "Email Already Exists",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else {
                        CoroutineScope(Dispatchers.IO).launch {
                            val userEntity = UserEntity(name = name1, phone = phone, email = email, password = password)
                            dbHelper.userDao().insert(userEntity)
                            withContext(Dispatchers.Main) {
                                val intent = Intent(this@RegistrationActivity, LoginActivity::class.java)
                                Toast.makeText(this@RegistrationActivity, "Registered Successfully", Toast.LENGTH_LONG).show()
                                startActivity(intent)
                                finish()
                            }
                        }
                    }
                }

            }
        }
    }

}