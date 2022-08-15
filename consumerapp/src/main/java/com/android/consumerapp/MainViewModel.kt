package com.android.consumerapp

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.consumerapp.db.DatabaseContract
import com.android.consumerapp.db.MappingHelper
import com.android.consumerapp.model.Users

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private var favoriteList = MutableLiveData<ArrayList<Users>>()

    fun setFavoriteUsers(context: Context) {
        val cursor = context.contentResolver.query(
            DatabaseContract.FavoriteUserColumns.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        val listConverted = MappingHelper.mapCursorToArrayList(cursor)
        favoriteList.postValue(listConverted)
    }

    fun getFavoriteUsers(): LiveData<ArrayList<Users>> {
        return favoriteList
    }
}