package com.android.submission2.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.android.submission2.data.GithubDatabase
import com.android.submission2.data.room.FavoriteDao

class ContentProvider : ContentProvider() {

    companion object {
        private const val AUTHORITY = "com.android.submission2"
        private const val TABLE_NAME = "users_favorite_table"
        private const val ID_USER = 1

        val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            uriMatcher.addURI(AUTHORITY, TABLE_NAME, ID_USER)
        }
    }

    private lateinit var favoriteDao: FavoriteDao

    override fun onCreate(): Boolean {
        favoriteDao = context?.let { ctx ->
            GithubDatabase.getDatabase(ctx).favoriteDao()
        }!!
        return false
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        val cursor: Cursor?
        when (uriMatcher.match(uri)) {
            ID_USER -> {
                cursor = favoriteDao.readUserForProvider()
                if (context != null) {
                    cursor.setNotificationUri(context?.contentResolver, uri)
                }
            }
            else -> {
                cursor = null
            }
        }
        return cursor
    }

    override fun getType(uri: Uri): String? = null

    override fun insert(uri: Uri, values: ContentValues?): Uri? = null

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int = -1


    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int = 0
}