package com.github.tedblair2.issuetracker.repository

import com.github.tedblair2.issuetracker.model.CommentData
import com.github.tedblair2.issuetracker.model.DetailedIssue
import com.github.tedblair2.issuetracker.model.IssuePage
import com.github.tedblair2.issuetracker.model.Response

class FakeIssueImpl:IssuesService {
    override suspend fun getIssues(username: String , endCursor: String?): Response<IssuePage> {
        TODO("Not yet implemented")
    }

    override suspend fun getIssue(id: String): Response<DetailedIssue> {
        TODO("Not yet implemented")
    }

    override suspend fun getComments(id: String , endCursor: String?): Response<CommentData> {
        TODO("Not yet implemented")
    }
}