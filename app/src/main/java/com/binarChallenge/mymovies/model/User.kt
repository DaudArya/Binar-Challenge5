package com.binarChallenge.mymovies.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = true)
    var id: Int?,
    @ColumnInfo(name = "name_user")
    var name: String?,
    @ColumnInfo(name = "email_user")
    var email: String?,
    @ColumnInfo(name = "date_birth")
    var born: String?,
    @ColumnInfo(name = "address")
    var adddress: String?,
    @ColumnInfo(name = "username_user")
    var username: String?,
    @ColumnInfo(name = "password_user")
    var password: String?
) : Serializable