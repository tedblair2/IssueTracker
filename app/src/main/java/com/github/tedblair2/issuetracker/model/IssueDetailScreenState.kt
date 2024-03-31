package com.github.tedblair2.issuetracker.model

import androidx.paging.PagingData

data class IssueDetailScreenState(
    val isLoading:Boolean=true,
    val issue: DetailedIssue= DetailedIssue(),
    val comments:PagingData<Comment> = PagingData.empty(),
    val isError:Boolean=false,
    val errorMsg:String=""
)
