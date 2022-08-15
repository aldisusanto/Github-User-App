package com.android.submission2.ui.following

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.submission2.api.RetrofitClient
import com.android.submission2.model.Users
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingViewModel : ViewModel() {
    val listFollowing = MutableLiveData<ArrayList<Users>>()

    fun setListFollowers(username: String) {
        RetrofitClient.instance
            .getFollowingUser(username)
            .enqueue(object : Callback<ArrayList<Users>> {
                override fun onResponse(
                    call: Call<ArrayList<Users>>,
                    response: Response<ArrayList<Users>>
                ) {
                    if (response.isSuccessful) {
                        listFollowing.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<ArrayList<Users>>, t: Throwable) {
                    t.message?.let { Log.d("onFailure", it) }
                }

            })

    }

    fun getListFollowers(): LiveData<ArrayList<Users>> {
        return listFollowing
    }
}