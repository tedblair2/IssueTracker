package com.github.tedblair2.issuetracker.events

import com.github.tedblair2.issuetracker.model.State

sealed interface HomeScreenEvent {
    data object SignOut:HomeScreenEvent
    data object ShowFilters:HomeScreenEvent
    data class IssueFilterWithState(val state: State):HomeScreenEvent
    data class GetRepositoryNames(val filter:String):HomeScreenEvent
    data class AddNewFilter(val name:String):HomeScreenEvent
    data object IssueFilterWithRepository:HomeScreenEvent
}