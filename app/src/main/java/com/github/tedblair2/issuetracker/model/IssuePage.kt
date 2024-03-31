package com.github.tedblair2.issuetracker.model

data class IssuePage(
    val nodes:List<SimpleIssue> = emptyList(),
    val hasNextPage:Boolean=false,
    val endCursor:String?=null
)
