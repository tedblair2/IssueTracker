package com.github.tedblair2.issuetracker.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WelcomeScreenState(
    val isLoggedIn:Boolean=false
):Parcelable
