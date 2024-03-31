package com.github.tedblair2.issuetracker.mappers

import com.github.tedblair2.CommentsQuery
import com.github.tedblair2.IssueQuery
import com.github.tedblair2.IssuesQuery
import com.github.tedblair2.issuetracker.model.Comment
import com.github.tedblair2.issuetracker.model.CommentData
import com.github.tedblair2.issuetracker.model.DetailedIssue
import com.github.tedblair2.issuetracker.model.IssuePage
import com.github.tedblair2.issuetracker.model.SimpleIssue
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun IssuesQuery.Node.toSimpleIssue():SimpleIssue{
    val timeTxt=createdAt as String
    val instanceBefore=Instant.parse(timeTxt)

    return SimpleIssue(
        id = id,
        title = title,
        createdAt = instanceBefore.toLocalDateTime(TimeZone.currentSystemDefault()),
        author = author?.login ?: "",
        commentCount = comments.totalCount,
        state = state.rawValue,
        issueNumber = number
    )
}

fun IssuesQuery.Issues.toIssuePage():IssuePage{
    val issues=nodes
        ?.map { it?.toSimpleIssue() ?: SimpleIssue() }
        ?: emptyList()

    return IssuePage(
        nodes = issues,
        hasNextPage = pageInfo.hasNextPage,
        endCursor = pageInfo.endCursor
    )
}

fun IssueQuery.OnIssue.toDetailedIssue():DetailedIssue{
    val desc= body.ifEmpty { "No Description" }
    return DetailedIssue(
        id = id,
        title = title,
        description = desc,
        createdAt = Instant.parse(createdAt as String).toLocalDateTime(TimeZone.currentSystemDefault()),
        author = author?.login ?: "",
        state = state.rawValue,
        issueNumber = number,
        commentCount = comments.totalCount
    )
}

fun CommentsQuery.Node1.toComment():Comment{
    return Comment(
        id = id,
        body = body,
        author = author?.login ?: "",
        avatar = author?.avatarUrl.toString(),
        createdAt = Instant.parse(createdAt.toString()).toLocalDateTime(TimeZone.currentSystemDefault())
    )
}

fun CommentsQuery.Comments.toCommentData():CommentData{
    return CommentData(
        totalCount = totalCount,
        hasNextPage = pageInfo.hasNextPage,
        endCursor = pageInfo.endCursor,
        comments = nodes?.map {
            it?.toComment() ?: Comment()
        } ?: emptyList()
    )
}