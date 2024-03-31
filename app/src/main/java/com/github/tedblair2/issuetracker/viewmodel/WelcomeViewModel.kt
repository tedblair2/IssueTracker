package com.github.tedblair2.issuetracker.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.tedblair2.issuetracker.model.Response
import com.github.tedblair2.issuetracker.model.WelcomeScreenState
import com.github.tedblair2.issuetracker.repository.SignInService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val signInService: SignInService
):ViewModel() {

    val welcomeScreenState=savedStateHandle.getStateFlow(
        WELCOME_SCREEN_KEY,WelcomeScreenState()
    )

    init {
        checkSignIn()
    }

    private fun checkSignIn(){
        viewModelScope.launch {
            signInService.checkSignIn().collect{response->
                when(response){
                    is Response.Success->{
                        savedStateHandle[WELCOME_SCREEN_KEY]=welcomeScreenState.value.copy(isLoggedIn = true)
                    }
                    else->{
                        savedStateHandle[WELCOME_SCREEN_KEY]=welcomeScreenState.value.copy(isLoggedIn = false)
                    }
                }
            }
        }
    }

    fun resetState(){
        savedStateHandle[WELCOME_SCREEN_KEY]=WelcomeScreenState()
    }

    companion object {
        const val WELCOME_SCREEN_KEY="WelcomeScreenKey"
    }
}