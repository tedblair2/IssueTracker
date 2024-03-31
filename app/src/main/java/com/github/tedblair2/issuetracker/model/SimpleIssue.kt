package com.github.tedblair2.issuetracker.model

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class SimpleIssue(
    val id:String="",
    val title:String="",
    val createdAt: LocalDateTime=Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    val author:String="",
    val commentCount:Int=0,
    val state:String="",
    val issueNumber:Int=0
)
