package com.binarChallenge.mymovies.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.binarChallenge.mymovies.dao.UserDao
import com.binarChallenge.mymovies.model.User

//User::class,
@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {
    abstract fun myUserDao() : UserDao
    companion object{
        @Volatile

        var INSTANCE : UserDatabase? = null

        fun getDatabaseInstance(context: Context): UserDatabase{
            val temInstance = INSTANCE
            if (temInstance != null ){
                return temInstance
            }
            synchronized(this){
                val roomDatabaseInstance = Room.databaseBuilder(context, UserDatabase::class.java, "User").allowMainThreadQueries().build()
                INSTANCE=roomDatabaseInstance
                return return roomDatabaseInstance
            }
        }
    }


}