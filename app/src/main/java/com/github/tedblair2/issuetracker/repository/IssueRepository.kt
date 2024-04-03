package com.github.tedblair2.issuetracker.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.github.tedblair2.issuetracker.model.SimpleIssue
import com.github.tedblair2.issuetracker.model.State
import com.github.tedblair2.issuetracker.repository.pagingsource.IssuePagingSource
import com.github.tedblair2.issuetracker.repository.pagingsource.RepositoryPagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IssueRepository @Inject constructor(
    private val issuesService: IssuesService
) {

    fun getIssues(
        username:String,
        state:List<State>,
        repositoryFilterList: List<String> = emptyList()
    ):Flow<PagingData<SimpleIssue>>{
        return Pager(
            config = PagingConfig(10),
            pagingSourceFactory = {
                IssuePagingSource(issuesService, username,state,repositoryFilterList)
            }
        ).flow
    }

    fun getRepositoryNames(username:String,filter:String):Flow<PagingData<String>>{
        return Pager(
            config = PagingConfig(30),
            pagingSourceFactory = { RepositoryPagingSource(username, issuesService,filter) }
        ).flow
    }
}