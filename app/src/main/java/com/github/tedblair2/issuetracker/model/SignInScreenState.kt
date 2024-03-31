package com.github.tedblair2.issuetracker.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SignInScreenState(
    val email:String="",
    val isWrongEmailFormat:Boolean=false,
    val errorMessage:String="",
    val loginSuccess:Boolean=false
):Parcelable
