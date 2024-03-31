package com.github.tedblair2.issuetracker.repository

import com.github.tedblair2.issuetracker.model.User
import kotlinx.coroutines.flow.Flow

interface UserService {
    suspend fun addUserData(username:String,accessToken:String)
    fun getUser():Flow<User>
}