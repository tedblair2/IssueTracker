package com.github.tedblair2.issuetracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.tedblair2.issuetracker.events.ProfileScreenEvent
import com.github.tedblair2.issuetracker.model.ProfileScreenState
import com.github.tedblair2.issuetracker.repository.SignInService
import com.github.tedblair2.issuetracker.repository.UserService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userService: UserService,
    private val dispatcher: CoroutineDispatcher,
    private val signInService:SignInService
):ViewModel() {

    private val _profileScreenState= MutableStateFlow(ProfileScreenState())
    val profileScreenState=_profileScreenState.asStateFlow()

    init {
        getUserData()
    }

    fun onEvent(event: ProfileScreenEvent){
        when(event){
            is ProfileScreenEvent.SignOut->signOut()
        }
    }

    private fun getUserData(){
        viewModelScope.launch(dispatcher) {
            userService.getUser().collect{user->
                _profileScreenState.update {
                    it.copy(
                        isLoggedIn = true,
                        currentUser = user
                    )
                }
            }
        }
    }

    private fun signOut(){
        viewModelScope.launch(dispatcher) {
            signInService.signOut().collect{
                _profileScreenState.update {
                    it.copy(
                        isLoggedIn = false,
                        currentUser = null
                    )
                }
            }
        }
    }
}