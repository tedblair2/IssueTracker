package com.github.tedblair2.issuetracker.events

sealed interface ProfileScreenEvent {
    data object SignOut:ProfileScreenEvent
}