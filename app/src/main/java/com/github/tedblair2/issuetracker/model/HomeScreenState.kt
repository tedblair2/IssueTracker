package com.github.tedblair2.issuetracker.model

import androidx.paging.PagingData

data class HomeScreenState(
    val isLoggedIn:Boolean=true ,
    val user: User?=null ,
    val issuesData: PagingData<SimpleIssue> = PagingData.empty(),
    val repositoryNames:PagingData<String> = PagingData.empty(),
    val isLoading:Boolean=true ,
    val isError:Boolean=false ,
    val errorMessage:String="" ,
    val showFilters:Boolean=false,
    val currentState:State = State.ALL,
    val labelsFilter:List<String> = listOf(),
    val repositoryFilter:List<String> = listOf()
)
