package com.android.submission2.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DetailUsers(
    val id: Long,
    val login: String,
    val name: String? = null,
    val type: String? = null,
    val avatar_url: String,
    val company: String? = null,
    val location: String? = null,
    val public_repos: Int? = 0,
    val followers: Int? = 0,
    val following: Int? = 0,
) : Parcelable