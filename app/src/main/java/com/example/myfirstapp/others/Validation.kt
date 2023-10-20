package com.example.myfirstapp.others

import android.util.Patterns
import android.widget.EditText
import android.widget.TextView

object Validation {
    fun nameValidation(name:String,edName:EditText):Boolean {
        if (!name.matches("^[a-zA-Z.\\s]+\$".toRegex())) {
            edName.error = "Name Must Contain Only Alphabets"
            return false
        }
        return true
    }

    fun phoneValidation(phone:String,edPhone:EditText):Boolean{
        if (phone.length != 11){
            edPhone.error = "Phone Number must be 11 characters"
            return false
        }
        return true
    }

    fun emailValidation(email:String,edEmail:EditText):Boolean{
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edEmail.error = "Email format is not correct"
            return false
        }
        return true
    }

    fun passwordValidation(password:String,edPassword:EditText):Boolean{

        val passwordRegex = "^(?=.*[0-9])(?=.*[!@#\$%^&*])[a-zA-Z0-9!@#\$%^&*]{8,}\$"
        val regexResult = password.matches(passwordRegex.toRegex())

        if (!regexResult || password.length != 8) {
            edPassword.error = "Enter 8 characters with alphabet and special characters"
            return false
        }
        return true
    }

    fun ageValidation(age:String, tvAge:TextView):Boolean{
        if (age.isEmpty()){
            tvAge.error = "Select Your BirthDay"
            return false
        }
        return true
    }

    fun checkNull(value:String,edvalue:EditText):Boolean{
        if (value.isEmpty()){
            edvalue.error = "Enter Data"
            return false
        }
        return true
    }

}