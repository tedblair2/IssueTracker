package com.github.tedblair2.issuetracker.features.issuedetails.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.github.tedblair2.issuetracker.events.IssueDetailEvent
import com.github.tedblair2.issuetracker.model.DetailedIssue
import com.github.tedblair2.issuetracker.model.IssueDetailScreenState
import com.github.tedblair2.issuetracker.model.Response
import com.github.tedblair2.issuetracker.repository.CommentsRepository
import com.github.tedblair2.issuetracker.repository.IssuesService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val issuesService: IssuesService,
    private val commentsRepository: CommentsRepository
):ViewModel() {

    private val _detailScreenState= MutableStateFlow(IssueDetailScreenState())
    val detailScreenState=_detailScreenState.asStateFlow()

    fun onEvent(event:IssueDetailEvent){
        when(event){
            is IssueDetailEvent.GetDetailedIssue->{
                getIssueDetails(event.id)
                getComments(event.id)
            }
        }
    }

    private fun getComments(id: String){
        viewModelScope.launch {
            commentsRepository.getComments(id).cachedIn(viewModelScope)
                .collect{pagingData->
                    _detailScreenState.update {
                        it.copy(
                            comments = pagingData
                        )
                    }
                }
        }
    }

    private fun getIssueDetails(id:String){
        viewModelScope.launch {
            when(val response=issuesService.getIssue(id)){
                is Response.Success->{
                    val data=response.data ?: DetailedIssue()
                    _detailScreenState.update {
                        it.copy(
                            isLoading = false,
                            issue = data,
                            isError = false
                        )
                    }
                }
                is Response.Error->{
                    _detailScreenState.update {
                        it.copy(
                            isLoading = false,
                            isError = true,
                            errorMsg = response.error ?: "Something went wrong!"
                        )
                    }
                }
                else->{}
            }
        }
    }
}