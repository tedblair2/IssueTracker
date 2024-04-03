package com.github.tedblair2.issuetracker.repository

import com.github.tedblair2.issuetracker.model.CommentData
import com.github.tedblair2.issuetracker.model.DetailedIssue
import com.github.tedblair2.issuetracker.model.IssuePage
import com.github.tedblair2.issuetracker.model.RepoData
import com.github.tedblair2.issuetracker.model.Response
import com.github.tedblair2.issuetracker.model.State

interface IssuesService {
    suspend fun getIssues(
        username:String ,
        endCursor:String?=null ,
        state:List<State> = listOf(State.ALL) ,
        labels:List<String> = emptyList(),
        repositoryFilterList: List<String> = emptyList()
    ): Response<IssuePage>
    suspend fun getIssue(id:String):Response<DetailedIssue>
    suspend fun getComments(id: String,endCursor: String?):Response<CommentData>
    suspend fun getRepository(username: String,endCursor: String?,filter:String):Response<RepoData>
}