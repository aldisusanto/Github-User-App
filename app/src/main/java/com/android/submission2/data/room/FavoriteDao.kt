package com.android.submission2.data.room

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserToFavorite(userFavorite: Favorite): Long

    @Query("DELETE FROM users_favorite_table WHERE id = :id")
    fun deleteUserFromFavorite(id: Int): Int

    @Query("SELECT * FROM users_favorite_table")
    fun readUserFavorite(): LiveData<List<Favorite>>

    @Query("SELECT count(*) FROM users_favorite_table WHERE id =:id")
    suspend fun checkUser(id: Int): Int

    @Query("SELECT * FROM users_favorite_table")
    fun readUserForProvider(): Cursor
}