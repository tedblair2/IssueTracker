package com.github.tedblair2.issuetracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.github.tedblair2.issuetracker.events.HomeScreenEvent
import com.github.tedblair2.issuetracker.model.HomeScreenState
import com.github.tedblair2.issuetracker.model.Response
import com.github.tedblair2.issuetracker.model.State
import com.github.tedblair2.issuetracker.repository.IssueRepository
import com.github.tedblair2.issuetracker.repository.SignInService
import com.github.tedblair2.issuetracker.repository.UserService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userService: UserService,
    private val issueRepository: IssueRepository,
    private val signInService: SignInService,
    private val ioDispatcher: CoroutineDispatcher
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
            is HomeScreenEvent.IssueFilterWithState->{
                filterIssues(event.state)
            }
            HomeScreenEvent.ShowFilters->{
                _homeScreenState.update {
                    it.copy(
                        showFilters = !homeScreenState.value.showFilters
                    )
                }
            }
            is HomeScreenEvent.GetRepositoryNames->{
                getRepositories(event.filter)
            }
            is HomeScreenEvent.AddNewFilter->{
                addNewFilter(event.name)
            }
            HomeScreenEvent.IssueFilter->{
                getCurrentUser()
            }
            HomeScreenEvent.GetLabels->{
                getLabels()
            }
            is HomeScreenEvent.AddNewLabelFilter->{
                addLabelFilter(event.label)
            }
            is HomeScreenEvent.UpdateDates->{
                updateDates(event.startDate,event.endDate)
            }
            HomeScreenEvent.ResetFilters->{
                resetFilters()
            }
        }
    }

    private fun updateDates(startDate: LocalDate,endDate:LocalDate){
        _homeScreenState.update {
            it.copy(
                startDate = startDate,
                endDate = endDate,
                isDateFilterOn = true
            )
        }
    }

    private fun getLabels(){
        viewModelScope.launch(ioDispatcher) {
            val username=homeScreenState.value.user?.username ?: ""
            issueRepository.getLabels(username).cachedIn(viewModelScope).collect{pagingData->
                _homeScreenState.update {
                    it.copy(
                        labels = pagingData
                    )
                }
            }
        }
    }

    private fun addNewFilter(name:String){
        _homeScreenState.update {
            val list=homeScreenState.value.repositoryFilter.toMutableList()
            if (list.contains(name)){
                list.remove(name)
            }else{
                list.add(name)
            }
            it.copy(
                repositoryFilter = list.toList(),
                isRepositoryFilterOn = list.isNotEmpty()
            )
        }
    }

    private fun resetFilters(){
        _homeScreenState.update {
            it.copy(
                currentState = State.ALL,
                labelsFilter = emptyList(),
                repositoryFilter = emptyList(),
                startDate = LocalDate(2000,1,1),
                endDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
                isDateFilterOn = false,
                isStateFilterOn = false,
                isLabelFilterOn = false,
                isRepositoryFilterOn = false,
                showFilters = false
            )
        }
        getCurrentUser()
    }

    private fun addLabelFilter(label:String){
        _homeScreenState.update {
            val list=homeScreenState.value.labelsFilter.toMutableList()
            if (list.contains(label)){
                list.remove(label)
            }else{
                list.add(label)
            }
            it.copy(
                labelsFilter = list.toList(),
                isLabelFilterOn = list.isNotEmpty()
            )
        }
    }

    private fun filterIssues(state:State){
        _homeScreenState.update {
            it.copy(
                currentState = state,
                isStateFilterOn = state != State.ALL
            )
        }
        getCurrentUser()
    }

    private fun getCurrentUser(){
        viewModelScope.launch(ioDispatcher) {
            _homeScreenState.update { it.copy(isLoading = true) }
            userService.getUser()
                .collect{user->
                    _homeScreenState.update {state->
                        state.copy(user = user)
                    }
                    getIssueList(user.username)
                }
        }
    }

    private fun getIssueList(username: String){
        viewModelScope.launch(ioDispatcher) {
            val state= listOf(homeScreenState.value.currentState)
            val repositoryFilterList=homeScreenState.value.repositoryFilter
            val labelsFilter= homeScreenState.value.labelsFilter
            val startDate= homeScreenState.value.startDate
            val endDate=homeScreenState.value.endDate
            issueRepository.getIssues(username,state,repositoryFilterList,labelsFilter,startDate,endDate).cachedIn(viewModelScope).collect{pagingData->
                _homeScreenState.update {
                    it.copy(
                        issuesData = pagingData,
                        isLoading = false
                    )
                }
            }
        }
    }
    private fun getRepositories(filter:String=""){
        viewModelScope.launch(ioDispatcher) {
            val username=homeScreenState.value.user?.username ?: ""
            issueRepository.getRepositoryNames(username,filter).cachedIn(viewModelScope).collect{pagingData->
                _homeScreenState.update {
                    it.copy(
                        repositoryNames = pagingData
                    )
                }
            }
        }
    }

    private fun signOut(){
        viewModelScope.launch(ioDispatcher) {
            signInService.signOut().collect{response->
                if (response is Response.Success){
                    resetFilters()
                    _homeScreenState.update {
                        it.copy(
                            isLoggedIn = false,
                            user = null,
                            issuesData = PagingData.empty()
                        )
                    }
                }
            }
        }
    }
}