package com.example.myfirstapp.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myfirstapp.R
import com.example.myfirstapp.activities.PropertyListActivity
import com.example.myfirstapp.database.DBHelper
import com.example.myfirstapp.database.PropertiesEntity
import com.example.myfirstapp.databinding.ActivityPropertyServiceBinding
import com.example.myfirstapp.others.Validation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PropertyService : AppCompatActivity() {
    lateinit var dbHelper: DBHelper
    lateinit var binding: ActivityPropertyServiceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPropertyServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DBHelper.getDB(this@PropertyService)

        val marla = resources.getStringArray(R.array.Marla)
        val floors = resources.getStringArray(R.array.Floors)
        val adapterMarla = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, marla
        )
        binding.spMarla.adapter = adapterMarla

        val adapterFloor = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, floors
        )
        binding.spFloor.adapter = adapterFloor

        binding.radioGroupServiceType.setOnCheckedChangeListener { group, checkedId ->
            val text = "You selected: " + if (R.id.rdRent == checkedId) "Rent" else "Sale"
            Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
        }

        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == null) {
                Toast.makeText(
                    this@PropertyService,
                    "Selected Furnished or Non-Furnished",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                val text =
                    "You selected: " + if (R.id.rdFurnished == checkedId) "Furnished" else "Non-Furnished"
                Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
            }
        }

        val properties = intent.getSerializableExtra("Properties") as PropertiesEntity?
        val positionMarla = adapterMarla.getPosition(properties?.marla)
        val positionFloor = adapterFloor.getPosition(properties?.noOfFloors)

        if (properties != null) {
            binding.edName.setText(properties.name)
            binding.edPhone.setText(properties.phone)
            binding.edArea.setText(properties.area)
            binding.edAddress.setText(properties.address)
            binding.edCity.setText(properties.city)
            binding.edRooms.setText(properties.noOfRooms)
            binding.edKitchen.setText(properties.noOfKitchens)
            binding.edWashroom.setText(properties.noOfWashroom)
            binding.spMarla.setSelection(positionMarla)
            binding.spFloor.setSelection(positionFloor)
            binding.btnSubmit.text = "Update"
        }



        binding.btnSubmit.setOnClickListener {
            if (properties != null) {
                val selectedRadioServiceType = binding.radioGroupServiceType.checkedRadioButtonId
                val selectedRadioFurnish = binding.radioGroup.checkedRadioButtonId
                val marla = binding.spMarla.selectedItem.toString()
                val floor = binding.spFloor.selectedItem.toString()
                val selectedFloorPosition = binding.spFloor.selectedItemPosition
                val selectedMarlaPosition = binding.spMarla.selectedItemPosition

                val idProperty = properties.id
                val area = binding.edArea.text.toString()
                val address = binding.edAddress.text.toString()
                val city = binding.edCity.text.toString()
                val rooms = binding.edRooms.text.toString()
                val kitchens = binding.edKitchen.text.toString()
                val washrooms = binding.edWashroom.text.toString()
                val name=binding.edName.text.toString()
                val phone=binding.edPhone.text.toString()

                if (selectedRadioServiceType == -1) {
                    Toast.makeText(
                        this,
                        "Please select Rent or Sale Radio Option",
                        Toast.LENGTH_LONG
                    ).show()
                } else if (selectedRadioFurnish == -1) {
                    Toast.makeText(
                        this,
                        "Please select Furnish or Non-Furnish Radio Option",
                        Toast.LENGTH_LONG
                    ).show()
                } else if (selectedMarlaPosition == 0) {
                    Toast.makeText(this, "Please select valid values for Marla", Toast.LENGTH_LONG)
                        .show()
                } else if (selectedFloorPosition == 0) {
                    Toast.makeText(this, "Please select valid values for Floor", Toast.LENGTH_LONG)
                        .show()
                } else if (!Validation.checkNull(name, binding.edName)
                    || !Validation.checkNull(phone,binding.edPhone)
                    || !Validation.checkNull(area,binding.edArea)
                    || !Validation.checkNull(address, binding.edAddress)
                    || !Validation.checkNull(city, binding.edCity)
                    || !Validation.checkNull(rooms, binding.edRooms)
                    || !Validation.checkNull(kitchens, binding.edKitchen)
                    || !Validation.checkNull(washrooms, binding.edWashroom)
                ) {

                    Toast.makeText(this, "Some Fields Missing", Toast.LENGTH_LONG).show()
                } else {
                    val selectedRadioButtonServiceType =
                        findViewById<RadioButton>(selectedRadioServiceType)
                    val selectedRadioButtonFurnish = findViewById<RadioButton>(selectedRadioFurnish)

                    val checkFurnish = selectedRadioButtonFurnish.text.toString()
                    val serviceType = selectedRadioButtonServiceType.text.toString()

                    val properties = PropertiesEntity(
                        id = idProperty,
                        name =name,
                        phone=phone,
                        area = area, address = address, city = city,
                        furnishFacility = checkFurnish,
                        marla = marla,
                        noOfRooms = rooms,
                        noOfKitchens = kitchens,
                        noOfWashroom = washrooms,
                        noOfFloors = floor,
                        dataPurpose = serviceType
                    )
                    CoroutineScope(Dispatchers.IO).launch {

                        dbHelper.userDao().update(properties)
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                this@PropertyService,
                                "Updated Successfully",
                                Toast.LENGTH_LONG
                            ).show()
                            val intent =
                                Intent(this@PropertyService, PropertyListActivity::class.java)
                            startActivity(intent)
                            finish()
                        }

                    }

                }
            } else {
                saveData()
            }
        }
    }


      fun saveData() {

            val selectedRadioServiceType = binding.radioGroupServiceType.checkedRadioButtonId
            val selectedRadioFurnish = binding.radioGroup.checkedRadioButtonId

            val marla = binding.spMarla.selectedItem.toString()
            val floor = binding.spFloor.selectedItem.toString()

            val selectedMarlaPosition = binding.spMarla.selectedItemPosition
            val selectedFloorPosition = binding.spFloor.selectedItemPosition

            val area = binding.edArea.text.toString()
            val address = binding.edAddress.text.toString()
            val city = binding.edCity.text.toString()
            val rooms = binding.edRooms.text.toString()
            val kitchens = binding.edKitchen.text.toString()
            val washrooms = binding.edWashroom.text.toString()
            val name=binding.edName.text.toString()
            val phone=binding.edPhone.text.toString()

            if (selectedRadioServiceType == -1) {
                Toast.makeText(this, "Please select Rent or Sale Radio Option", Toast.LENGTH_LONG)
                    .show()
            } else if (selectedRadioFurnish == -1) {
                Toast.makeText(
                    this,
                    "Please select Furnish or Non-Furnish Radio Option",
                    Toast.LENGTH_LONG
                ).show()
            } else if (selectedMarlaPosition == 0) {
                Toast.makeText(this, "Please select valid values for Marla", Toast.LENGTH_LONG)
                    .show()
            } else if (selectedFloorPosition == 0) {
                Toast.makeText(this, "Please select valid values for Floor", Toast.LENGTH_LONG)
                    .show()
            } else if (!Validation.checkNull(name,binding.edName)
                ||!Validation.checkNull(phone,binding.edPhone)
                || !Validation.checkNull(area, binding.edArea)
                || !Validation.checkNull(address, binding.edAddress)
                || !Validation.checkNull(city, binding.edCity)
                || !Validation.checkNull(rooms, binding.edRooms)
                || !Validation.checkNull(kitchens, binding.edKitchen)
                || !Validation.checkNull(washrooms, binding.edWashroom)
            ) {

                Toast.makeText(this, "Some Fields Missing", Toast.LENGTH_LONG).show()
            } else {
                val selectedRadioButtonServiceType =
                    findViewById<RadioButton>(selectedRadioServiceType)
                val selectedRadioButtonFurnish = findViewById<RadioButton>(selectedRadioFurnish)

                val checkFurnish = selectedRadioButtonFurnish.text.toString()
                val serviceType = selectedRadioButtonServiceType.text.toString()

                val propertiesEntity = PropertiesEntity(
                    name=name,
                    phone=phone,
                    area = area, address = address, city = city,
                    furnishFacility = checkFurnish,
                    marla = marla,
                    noOfRooms = rooms,
                    noOfKitchens = kitchens,
                    noOfWashroom = washrooms,
                    noOfFloors = floor,
                    dataPurpose = serviceType
                )
                CoroutineScope(Dispatchers.IO).launch {

                    dbHelper.userDao().insert(propertiesEntity)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@PropertyService,
                            "Saved Successfully",
                            Toast.LENGTH_LONG
                        ).show()
                        val intent = Intent(this@PropertyService, UserListActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                }

            }
        }
    }





