package com.github.tedblair2.issuetracker.model

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class DetailedIssue(
    val id:String="",
    val title:String="",
    val description:String="",
    val createdAt: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    val state:String="",
    val author:String="",
    val commentCount:Int=0,
    val issueNumber:Int=0
)
