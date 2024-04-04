package com.github.tedblair2.issuetracker.model

import androidx.paging.PagingData
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class HomeScreenState(
    val isLoggedIn:Boolean=true ,
    val user: User?=null ,
    val issuesData: PagingData<SimpleIssue> = PagingData.empty() ,
    val repositoryNames:PagingData<String> = PagingData.empty() ,
    val labels:PagingData<String> = PagingData.empty() ,
    val isLoading:Boolean=true ,
    val isError:Boolean=false ,
    val errorMessage:String="" ,
    val showFilters:Boolean=false ,
    val currentState:State = State.ALL ,
    val labelsFilter:List<String> = listOf() ,
    val repositoryFilter:List<String> = listOf() ,
    val startDate: LocalDate = LocalDate(2000,1,1) ,
    val endDate: LocalDate= Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date ,
    val isDateFilterOn:Boolean=false,
    val isStateFilterOn:Boolean=false,
    val isLabelFilterOn:Boolean=false,
    val isRepositoryFilterOn:Boolean=false
)
