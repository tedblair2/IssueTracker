package com.github.tedblair2.issuetracker.events

sealed interface IssueDetailEvent {
    data class GetDetailedIssue(val id:String):IssueDetailEvent
}