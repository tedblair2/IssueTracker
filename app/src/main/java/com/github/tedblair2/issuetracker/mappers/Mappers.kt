package com.github.tedblair2.issuetracker.mappers

import com.github.tedblair2.CommentsQuery
import com.github.tedblair2.IssueQuery
import com.github.tedblair2.IssuesQuery
import com.github.tedblair2.LabelsQuery
import com.github.tedblair2.RepositoriesQuery
import com.github.tedblair2.issuetracker.model.Comment
import com.github.tedblair2.issuetracker.model.CommentData
import com.github.tedblair2.issuetracker.model.DetailedIssue
import com.github.tedblair2.issuetracker.model.IssuePage
import com.github.tedblair2.issuetracker.model.Label
import com.github.tedblair2.issuetracker.model.RepoData
import com.github.tedblair2.issuetracker.model.SimpleIssue
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.periodUntil
import kotlinx.datetime.toLocalDateTime

fun IssuesQuery.Node.toSimpleIssue():SimpleIssue{
    val timeTxt=createdAt as String
    val instanceBefore=Instant.parse(timeTxt)
    val mainTitle="$title #$number"

    return SimpleIssue(
        id = id,
        title = mainTitle,
        createdAt = instanceBefore.toLocalDateTime(TimeZone.currentSystemDefault()),
        author = author?.login ?: "",
        commentCount = comments.totalCount,
        state = state.rawValue,
        issueNumber = number,
        repositoryName = repository.name
    )
}

fun IssuesQuery.Issues.toIssuePage(
    repositoryFilterList: List<String> = emptyList()
):IssuePage{
    val issues=nodes
        ?.map { it?.toSimpleIssue() ?: SimpleIssue() }
        ?: emptyList()

    val filteredIssues=if (repositoryFilterList.isEmpty()){
        issues
    }else{
        issues
            .filter { it.repositoryName in repositoryFilterList }
    }

    return IssuePage(
        nodes = filteredIssues,
        hasNextPage = pageInfo.hasNextPage,
        endCursor = pageInfo.endCursor
    )
}

fun IssueQuery.OnIssue.toDetailedIssue():DetailedIssue{
    val desc= body.ifEmpty { "No Description" }
    val mainTitle="$title #$number"
    return DetailedIssue(
        id = id,
        title = mainTitle,
        description = desc,
        createdAt = Instant.parse(createdAt as String).toLocalDateTime(TimeZone.currentSystemDefault()),
        author = author?.login ?: "",
        state = state.rawValue,
        issueNumber = number,
        commentCount = comments.totalCount,
        avatar = author?.avatarUrl.toString()
    )
}

fun CommentsQuery.Node1.toComment():Comment{
    val createdAt=Instant.parse(createdAt.toString())
    val createdDateTime=createdAt.toLocalDateTime(TimeZone.currentSystemDefault())
    val period=createdAt.periodUntil(Clock.System.now(), TimeZone.currentSystemDefault())
    return Comment(
        id = id,
        body = body,
        author = author?.login ?: "",
        avatar = author?.avatarUrl.toString(),
        duration = getTimePeriod(period,createdDateTime)
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

fun LabelsQuery.Labels.toLabel():Label{
    return Label(
        labelNames = nodes?.map {
            it?.name ?: ""
        } ?: emptyList(),
        cursor = pageInfo.endCursor
    )
}

fun RepositoriesQuery.Issues.toRepoData(filter:String=""):RepoData{
    return RepoData(
        names = nodes?.map { it?.toRepository() ?: "" }
            ?.filter {
                it.lowercase().contains(filter)
            }
            ?.toSet()
            ?.toList()
            ?: emptyList(),
        endCursor = pageInfo.endCursor
    )
}

fun RepositoriesQuery.Node.toRepository():String{
    return repository.name
}

private fun getTimePeriod(period: DateTimePeriod,createdAt:LocalDateTime):String{
    return if (period.years>1){
        "${createdAt.dayOfMonth} ${createdAt.month} ${createdAt.year}"
    }else if (period.months>1){
        "${createdAt.dayOfMonth} ${createdAt.month}"
    }else if (period.days>7){
        "${period.days.div(7)}wk ago"
    }else if (period.days>1){
        "${period.days}d ago"
    }else if (period.hours>1){
        "${period.hours}h ago"
    }else{
        "${period.minutes}min ago"
    }
}
