package com.github.tedblair2.issuetracker.model

data class ProfileScreenState(
    val isLoggedIn:Boolean=true,
    val currentUser:User?=null
)
