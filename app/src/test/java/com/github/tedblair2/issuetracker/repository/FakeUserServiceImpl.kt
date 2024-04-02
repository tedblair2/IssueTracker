package com.github.tedblair2.issuetracker.repository

import com.github.tedblair2.issuetracker.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FakeUserServiceImpl:UserService {

    private val user= MutableStateFlow(User("t3dd","token"))
    override suspend fun addUserData(username: String , accessToken: String) {
        user.update {
            it.copy(username, accessToken)
        }
    }
    override fun getUser(): Flow<User> {
        return user
    }
}