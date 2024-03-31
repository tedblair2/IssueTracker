package com.github.tedblair2.issuetracker.repository

import com.github.tedblair2.issuetracker.model.CommentData
import com.github.tedblair2.issuetracker.model.DetailedIssue
import com.github.tedblair2.issuetracker.model.IssuePage
import com.github.tedblair2.issuetracker.model.Response

interface IssuesService {
    suspend fun getIssues(username:String,endCursor:String?=null): Response<IssuePage>
    suspend fun getIssue(id:String):Response<DetailedIssue>
    suspend fun getComments(id: String,endCursor: String?):Response<CommentData>
}