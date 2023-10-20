package com.example.myfirstapp.activities

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.myfirstapp.database.DBHelper
import com.example.myfirstapp.database.SharedPreferenceDatabase
import com.example.myfirstapp.databinding.ActivityUserProfileBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class UserProfile : AppCompatActivity() {
    lateinit var sharedPreferenceDatabase: SharedPreferenceDatabase
    lateinit var dbHelper: DBHelper
    lateinit var binding:ActivityUserProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper= DBHelper.getDB(this@UserProfile)
        sharedPreferenceDatabase= SharedPreferenceDatabase(this@UserProfile)

        binding.btnLogout.setOnClickListener {

            sharedPreferenceDatabase.clearData()

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()

        }

        binding.showDatePickerButton.setOnClickListener {
            val cal = Calendar.getInstance()
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val day = cal.get(Calendar.DAY_OF_MONTH)

            fun calculateAge(selectedDate: Calendar) {
                val currentDate = Calendar.getInstance()

                val diffInMillis = currentDate.timeInMillis - selectedDate.timeInMillis
                val ageYears = diffInMillis / (1000L * 60 * 60 * 24 * 365)
                val ageMonths = diffInMillis / (1000L * 60 * 60 * 24 * 30)
                val ageDays = diffInMillis / (1000L * 60 * 60 * 24)

                binding.tvShowAge.text = "Age: $ageYears years, $ageMonths months, $ageDays days"
                binding.tvShowAge.visibility = TextView.VISIBLE
            }


            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(selectedYear, selectedMonth, selectedDay)
                    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    binding.tvSetAge.text = sdf.format(selectedDate.time)
                    calculateAge(selectedDate)

                },
                year, month, day
            )
            datePickerDialog.show()
        }


        binding.buttonCalculate.setOnClickListener {
            val weight = binding.editTextWeight.text.toString().toFloatOrNull()
            val feet = binding.editTextFeet.text.toString().toIntOrNull()
            val inches = binding.editTextInches.text.toString().toIntOrNull()

            if (weight != null && feet != null && inches != null && feet > 0 && inches >= 0 && inches < 12) {
                val bmi = calculateBMI(weight, feet, inches)
                val resultText = "Your BMI is $bmi"
                binding.textViewResult.text = resultText
            } else {
                binding.textViewResult.text = "Invalid input"
            }
        }
    }
    private fun calculateBMI(weight: Float, feet: Int, inches: Int): Double {
        val heightInMeters = ((feet * 12) + inches) * 0.0254 // Convert feet and inches to meters
        return weight / (heightInMeters * heightInMeters)
    }

    override fun onResume() {
        super.onResume()
        CoroutineScope(Dispatchers.IO).launch {
            val email= sharedPreferenceDatabase.getData("email","")
            val user = dbHelper.userDao().getUserByEmail(email)
            if (user!=null){
                binding.tvName.text = "Name: ${user.name}"
                binding.tvNumber.text = "Phone: ${user.phone}"
                binding.tvEmail.text = "Email: ${user.email}"
            }
        }


    }
}