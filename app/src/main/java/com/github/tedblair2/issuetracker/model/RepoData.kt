package com.github.tedblair2.issuetracker.model

data class RepoData(
    val names:List<String> = emptyList() ,
    val endCursor:String?=null
)
