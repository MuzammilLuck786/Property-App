package com.example.myfirstapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserEntity::class, PropertiesEntity::class], version = 1, exportSchema = false)
abstract class DBHelper : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: DBHelper? = null

        fun getDB(context: Context): DBHelper {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildRoomDB(context).also { INSTANCE = it }
            }
        }

        private fun buildRoomDB(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                DBHelper::class.java,
                "userDB"
            ).build()
    }
}