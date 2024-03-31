package com.github.tedblair2.issuetracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.github.tedblair2.issuetracker.events.HomeScreenEvent
import com.github.tedblair2.issuetracker.model.HomeScreenState
import com.github.tedblair2.issuetracker.repository.IssueRepository
import com.github.tedblair2.issuetracker.repository.UserService
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userService: UserService,
    private val issueRepository: IssueRepository
):ViewModel() {

    private val _homeScreenState= MutableStateFlow(HomeScreenState())
    val homeScreenState=_homeScreenState.asStateFlow()

    init {
        getCurrentUser()
    }

    fun onEvent(event: HomeScreenEvent){
        when(event){
            HomeScreenEvent.SignOut->{
                signOut()
            }
        }
    }

    private fun getCurrentUser(){
        viewModelScope.launch {
            userService.getUser()
                .collect{user->
                    _homeScreenState.update {state->
                        state.copy(user = user)
                    }
                    val name="RaphaelNdonga"
                    getIssueList(user.username)
                }
        }
    }

    private suspend fun getIssueList(username: String){
        issueRepository.getIssues(username).cachedIn(viewModelScope).collect{pagingData->
            _homeScreenState.update {
                it.copy(
                    issuesData = pagingData,
                    isLoading = false
                )
            }
        }
    }
    private fun signOut(){
        Firebase.auth.signOut()
        _homeScreenState.update {
            it.copy(
                isLoggedIn = false,
                user = null,
                issues = emptyList()
            )
        }
    }
}