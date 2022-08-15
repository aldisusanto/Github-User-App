package com.android.consumerapp.db

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {
    const val AUTHORITY = "com.android.submission2"
    const val SCHEME = "content"

    internal class FavoriteUserColumns : BaseColumns {
        companion object {
            private const val TABLE_NAME = "users_favorite_table"
            const val ID = "id"
            const val USERNAME = "username"
            const val AVATAR = "avatar_url"
            const val TYPE = "type"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}