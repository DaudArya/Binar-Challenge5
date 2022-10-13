package com.binarChallenge.mymovies.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.binarChallenge.mymovies.model.User


@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertUser(user: User)

    @Query("SELECT * FROM user_table WHERE username_user = :username")
    fun getUsername(username: String?): User

    @Update
    fun updateProfileUser(user: User): Int

    @Delete
     fun deleteUser(user: User): Int
}