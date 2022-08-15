package com.android.consumerapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Users(
    val id: Int,
    val login: String,
    val avatar_url: String,
    val type: String
) : Parcelable
