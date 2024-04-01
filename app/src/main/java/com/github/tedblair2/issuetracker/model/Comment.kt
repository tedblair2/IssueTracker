package com.github.tedblair2.issuetracker.model

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.TimeZone
import kotlinx.datetime.periodUntil

data class Comment(
    val id:String="" ,
    val body:String="" ,
    val timePeriod:DateTimePeriod=Clock.System.now().periodUntil(Clock.System.now(), TimeZone.UTC) ,
    val author:String="" ,
    val avatar:String=""
)

data class CommentData(
    val comments:List<Comment> = emptyList(),
    val totalCount:Int=0,
    val hasNextPage:Boolean=false,
    val endCursor:String?=null
)
