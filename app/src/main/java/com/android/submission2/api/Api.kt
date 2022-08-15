package com.android.submission2.api

import com.android.submission2.BuildConfig
import com.android.submission2.model.DetailUsers
import com.android.submission2.model.Users
import com.android.submission2.model.UsersResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    companion object {
        const val TOKEN = BuildConfig.GITHUB_TOKEN
    }

    @GET("search/users")
    @Headers("Authorization: token $TOKEN")
    fun getSearchUser(
        @Query("q") q: String
    ): Call<UsersResponse>

    @GET("users/{username}")
    @Headers("Authorization: token $TOKEN")
    fun getDetailUser(
        @Path("username") username: String
    ): Call<DetailUsers>

    @GET("users/{username}/followers")
    @Headers("Authorization: token $TOKEN")
    fun getFollowersUser(
        @Path("username") username: String
    ): Call<ArrayList<Users>>

    @GET("users/{username}/following")
    @Headers("Authorization: token $TOKEN")
    fun getFollowingUser(
        @Path("username") username: String
    ): Call<ArrayList<Users>>
}