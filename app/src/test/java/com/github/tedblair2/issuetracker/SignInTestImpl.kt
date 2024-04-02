package com.github.tedblair2.issuetracker

import android.app.Activity
import com.github.tedblair2.issuetracker.helpers.EmailValidator
import com.github.tedblair2.issuetracker.model.Response
import com.github.tedblair2.issuetracker.repository.SignInService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update

class SignInTestImpl:SignInService {

    private val user=MutableStateFlow("")
    private val emailValidator=EmailValidator()
    override fun signIn(email: String , activity: Activity?): Flow<Response<Unit>> = flow{
        if (!emailValidator.validateEmail(email)){
            emit(Response.Error("Enter correct email"))
        }else{
            user.update {
                email
            }
            emit(Response.Success(Unit))
        }
    }

    override fun checkSignIn(): Flow<Response<Unit>> = flow{
        if (user.value.isNotEmpty()){
            emit(Response.Success(Unit))
        }else{
            emit(Response.Error("Not logged in"))
        }
    }

    override fun signOut(): Flow<Response<Unit>> = flow{
        user.update {
            ""
        }
        emit(Response.Success(Unit))
    }
}