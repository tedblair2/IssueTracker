package com.github.tedblair2.issuetracker.model

data class Label(
    val labelNames:List<String> = emptyList() ,
    val cursor:String?=null
)
