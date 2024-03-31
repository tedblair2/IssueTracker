package com.github.tedblair2.issuetracker.events

sealed interface HomeScreenEvent {
    data object SignOut:HomeScreenEvent
}