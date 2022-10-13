package com.binarChallenge.mymovies.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.binarChallenge.mymovies.dao.UserDao

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class DatabaseStore : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        private var userDatabase: DatabaseStore? = null

        fun getData(context: Context): DatabaseStore {
            return userDatabase?: synchronized(this){
                val data = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseStore::class.java,
                    "Data.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                userDatabase = data
                data
            }
        }
    }
}