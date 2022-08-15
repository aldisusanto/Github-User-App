package com.android.submission2.ui.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.submission2.api.RetrofitClient
import com.android.submission2.data.GithubDatabase
import com.android.submission2.data.room.Favorite
import com.android.submission2.data.room.FavoriteDao
import com.android.submission2.model.DetailUsers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : AndroidViewModel(application) {
    private val detailUser = MutableLiveData<DetailUsers>()

    private var favoriteDao: FavoriteDao?
    private var githubDatabase: GithubDatabase? = GithubDatabase.getDatabase(application)

    init {
        favoriteDao = githubDatabase?.favoriteDao()
    }

    fun setDetailUser(username: String) {
        RetrofitClient.instance
            .getDetailUser(username)
            .enqueue(object : Callback<DetailUsers> {
                override fun onResponse(
                    call: Call<DetailUsers>,
                    response: Response<DetailUsers>
                ) {
                    if (response.isSuccessful) {
                        detailUser.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<DetailUsers>, t: Throwable) {
                    Log.d("onFailure", t.message.toString())
                }

            })
    }

    fun getDetailUser(): LiveData<DetailUsers> {
        return detailUser

    }

    fun insertToFavorite(id: Int, username: String, avatar: String, type: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val newFavorite = Favorite(
                id,
                username,
                avatar,
                type,
            )
            favoriteDao?.insertUserToFavorite(newFavorite)
        }
    }

    suspend fun checkUser(id: Int) = favoriteDao?.checkUser(id)

    fun deleteUserToFavorite(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            favoriteDao?.deleteUserFromFavorite(id)
        }
    }

}