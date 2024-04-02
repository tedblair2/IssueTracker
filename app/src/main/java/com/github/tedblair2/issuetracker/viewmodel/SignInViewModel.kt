package com.github.tedblair2.issuetracker.viewmodel

import android.app.Activity
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.tedblair2.issuetracker.events.SignInScreenEvent
import com.github.tedblair2.issuetracker.model.Response
import com.github.tedblair2.issuetracker.model.SignInScreenState
import com.github.tedblair2.issuetracker.repository.SignInService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val signInService: SignInService
):ViewModel() {

    val signInScreenState=savedStateHandle.getStateFlow(
        SIGN_IN_STATE_KEY,SignInScreenState())

    private fun updateEmail(email: String){
        savedStateHandle[SIGN_IN_STATE_KEY] =signInScreenState.value.copy(email=email,isWrongEmailFormat = false)
    }

    fun onEvent(event:SignInScreenEvent){
        when(event){
            is SignInScreenEvent.TypeEmail->{
                updateEmail(event.email)
            }
            is SignInScreenEvent.SignIn->{
                signIn(event.activity)
            }
            SignInScreenEvent.ResetState->{
                resetState()
            }
        }
    }

    private fun signIn(activity: Activity){
        viewModelScope.launch {
            val email=signInScreenState.value.email
            signInService.signIn(email, activity).collect{response->
                when(response){
                    is Response.Success->{
                        savedStateHandle[SIGN_IN_STATE_KEY]=signInScreenState.value.copy(
                            isWrongEmailFormat = false,
                            loginSuccess = true
                        )
                    }
                    is Response.Error->{
                        if (response.error.isNullOrEmpty()){
                            savedStateHandle[SIGN_IN_STATE_KEY]=signInScreenState.value.copy(
                                isWrongEmailFormat = false,
                                loginSuccess = false
                            )
                        }else{
                            savedStateHandle[SIGN_IN_STATE_KEY]=signInScreenState.value.copy(
                                isWrongEmailFormat = true,
                                loginSuccess = false,
                                errorMessage = "Enter correct error message"
                            )
                        }
                    }
                    else->{}
                }
            }
        }
    }

    private fun resetState(){
        savedStateHandle[SIGN_IN_STATE_KEY]=SignInScreenState()
    }

    companion object {
        const val SIGN_IN_STATE_KEY="signInStateKey"
    }
}