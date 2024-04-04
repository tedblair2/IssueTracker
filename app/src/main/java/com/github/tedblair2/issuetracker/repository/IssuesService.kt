package com.github.tedblair2.issuetracker.repository

import com.github.tedblair2.issuetracker.model.CommentData
import com.github.tedblair2.issuetracker.model.DetailedIssue
import com.github.tedblair2.issuetracker.model.IssuePage
import com.github.tedblair2.issuetracker.model.LabelData
import com.github.tedblair2.issuetracker.model.RepoData
import com.github.tedblair2.issuetracker.model.Response
import com.github.tedblair2.issuetracker.model.State
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

interface IssuesService {
    suspend fun getIssues(
        username:String ,
        endCursor:String?=null ,
        state:List<State> = listOf(State.ALL) ,
        labels:List<String> = emptyList(),
        repositoryFilterList: List<String> = emptyList(),
        startDate:LocalDate?=null,
        endDate:LocalDate= Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    ): Response<IssuePage>
    suspend fun getIssue(id:String):Response<DetailedIssue>
    suspend fun getComments(id: String,endCursor: String?):Response<CommentData>
    suspend fun getRepository(username: String,endCursor: String?,filter:String):Response<RepoData>
    suspend fun getLabels(username: String,endCursor: String?):Response<LabelData>
}