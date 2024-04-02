package com.github.tedblair2.issuetracker.repository

import com.github.tedblair2.issuetracker.model.CommentData
import com.github.tedblair2.issuetracker.model.DetailedIssue
import com.github.tedblair2.issuetracker.model.IssuePage
import com.github.tedblair2.issuetracker.model.Response
import com.github.tedblair2.issuetracker.model.SimpleIssue
import com.github.tedblair2.issuetracker.model.State
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class FakeIssueImpl:IssuesService {

    override suspend fun getIssues(
        username: String ,
        endCursor: String? ,
        state: List<State> ,
        labels: List<String>
    ): Response<IssuePage> {
        val issues= listOf(
            SimpleIssue(
                id = "id",
                title = "This is the title for one",
                createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                author = "author",
                commentCount = 3,
                state = "Open",
                issueNumber = 1
            ),
            SimpleIssue(
                id = "id2",
                title = "This is the title for two",
                createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                author = "author2",
                commentCount = 1,
                state = "Open",
                issueNumber = 3
            )
        )
        val issuePage=IssuePage(
            nodes = issues,
            hasNextPage = false,
            endCursor = "endcursor"
        )
        return Response.Success(issuePage)
    }

    override suspend fun getIssue(id: String): Response<DetailedIssue> {
        val issue=DetailedIssue(
            id="id",
            title = "this is title",
            description = "This is the desription",
            createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
            state = "Open",
            author = "author",
            commentCount = 2,
            issueNumber = 1,
            avatar = "url"
        )
        return Response.Success(issue)
    }

    override suspend fun getComments(id: String , endCursor: String?): Response<CommentData> {
        return Response.Success(CommentData())
    }
}