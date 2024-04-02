package com.github.tedblair2.issuetracker.events

import android.app.Activity

sealed interface SignInScreenEvent {
    data class TypeEmail(val email:String):SignInScreenEvent
    data class SignIn(val activity: Activity?):SignInScreenEvent
    data object ResetState:SignInScreenEvent
}