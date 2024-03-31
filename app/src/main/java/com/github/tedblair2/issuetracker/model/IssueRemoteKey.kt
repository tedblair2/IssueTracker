package com.github.tedblair2.issuetracker.model

data class IssueRemoteKey(
    val id:Int,
    val nextPage:Int?=null,
    val prevPage:Int?=null
)
