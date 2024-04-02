package com.github.tedblair2.issuetracker.model

import androidx.paging.PagingData

data class HomeScreenState(
    val isLoggedIn:Boolean=true,
    val user: User?=null,
    val issuesData: PagingData<SimpleIssue> = PagingData.empty(),
    val isLoading:Boolean=true,
    val isError:Boolean=false,
    val errorMessage:String=""
)
