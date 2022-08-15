package com.android.consumerapp.db

import android.database.Cursor
import com.android.consumerapp.db.DatabaseContract.FavoriteUserColumns.Companion.AVATAR
import com.android.consumerapp.db.DatabaseContract.FavoriteUserColumns.Companion.ID
import com.android.consumerapp.db.DatabaseContract.FavoriteUserColumns.Companion.TYPE
import com.android.consumerapp.db.DatabaseContract.FavoriteUserColumns.Companion.USERNAME
import com.android.consumerapp.model.Users

object MappingHelper {
    fun mapCursorToArrayList(cursor: Cursor?): ArrayList<Users> {
        val favoriteList = ArrayList<Users>()

        if (cursor != null) {
            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(ID))
                val username = cursor.getString(cursor.getColumnIndexOrThrow(USERNAME))
                val avatar = cursor.getString(cursor.getColumnIndexOrThrow(AVATAR))
                val type = cursor.getString(cursor.getColumnIndexOrThrow(TYPE))
                favoriteList.add(
                    Users(
                        id,
                        username,
                        avatar,
                        type
                    )

                )
            }
        }
        return favoriteList


    }
}