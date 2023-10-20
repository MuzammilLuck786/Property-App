package com.example.myfirstapp.database

import androidx.room.*
import com.example.myfirstapp.database.PropertiesEntity
import com.example.myfirstapp.database.UserEntity

@Dao
interface UserDao {

    @Insert
    suspend fun insert(userEntity: UserEntity)

    @Query("SELECT * FROM userTable")
    fun getAllUsers():List<UserEntity>

    @Query("SELECT * FROM userTable WHERE password = :password")
    suspend fun getUserByPassword(password: String): UserEntity?

    @Query("SELECT * FROM userTable WHERE email =:email")
    suspend fun getUserByEmail(email: String): UserEntity?


    //Property Entity Queries

    @Insert
    suspend fun insert(propertyEntity: PropertiesEntity)

    @Update
    suspend fun update(propertyEntity: PropertiesEntity)

    @Query("SELECT * FROM properties")
    fun getAllProperties():List<PropertiesEntity>


    @Query("Delete from properties where id = :id")
    fun deletebyPropertyid(id: Long)


    @Query("SELECT * FROM properties WHERE name LIKE :searchQuery OR phone LIKE :searchQuery OR area LIKE :searchQuery OR address LIKE :searchQuery OR city LIKE :searchQuery OR marla LIKE :searchQuery OR noOfRooms LIKE :searchQuery OR noOfKitchens LIKE :searchQuery OR noOfWashroom LIKE :searchQuery OR noOfFloors LIKE :searchQuery OR furnishFacility LIKE :searchQuery OR dataPurpose LIKE :searchQuery")
    fun getFilteredProperties(searchQuery: String): List<PropertiesEntity>






}