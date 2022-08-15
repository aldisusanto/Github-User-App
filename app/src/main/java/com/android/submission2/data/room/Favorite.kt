package com.android.submission2.data.room

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "users_favorite_table")
data class Favorite(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val username: String,
    val avatar_url: String,
    val type: String
) : Parcelable

