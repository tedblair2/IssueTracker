package com.github.tedblair2.issuetracker.repository

import android.app.Activity
import com.github.tedblair2.issuetracker.model.Response
import kotlinx.coroutines.flow.Flow

interface SignInService {
    fun signIn(email:String,activity: Activity):Flow<Response<Unit>>
    fun checkSignIn():Flow<Response<Unit>>
    fun signOut():Flow<Response<Unit>>
}