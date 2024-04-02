package com.github.tedblair2.issuetracker.model

data class Comment(
    val id:String="" ,
    val body:String="" ,
    val duration:String="",
    val author:String="" ,
    val avatar:String=""
)

data class CommentData(
    val comments:List<Comment> = emptyList(),
    val totalCount:Int=0,
    val hasNextPage:Boolean=false,
    val endCursor:String?=null
)
