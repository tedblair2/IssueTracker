package com.github.tedblair2.issuetracker.model

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class Comment(
    val id:String="",
    val body:String="",
    val createdAt:LocalDateTime= Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    val author:String="",
    val avatar:String=""
)

data class CommentData(
    val comments:List<Comment> = emptyList(),
    val totalCount:Int=0,
    val hasNextPage:Boolean=false,
    val endCursor:String?=null
)
