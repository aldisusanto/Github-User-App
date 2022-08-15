package com.android.submission2.ui.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.android.submission2.data.GithubDatabase
import com.android.submission2.data.room.Favorite
import com.android.submission2.data.room.FavoriteDao

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {

    private var favoriteDao: FavoriteDao?
    private var githubDatabase: GithubDatabase? = GithubDatabase.getDatabase(application)

    init {
        favoriteDao = githubDatabase?.favoriteDao()
    }

    fun readFavoriteUsers(): LiveData<List<Favorite>>?{
        return favoriteDao?.readUserFavorite()
    }
}