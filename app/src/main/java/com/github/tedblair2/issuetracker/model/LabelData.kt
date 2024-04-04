package com.github.tedblair2.issuetracker.model

data class LabelData(
    val labelNames:List<String> = emptyList(),
    val cursor:String?=null
)
