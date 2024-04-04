package com.github.tedblair2.issuetracker.events

import com.github.tedblair2.issuetracker.model.State
import kotlinx.datetime.LocalDate

sealed interface HomeScreenEvent {
    data object SignOut:HomeScreenEvent
    data object ShowFilters:HomeScreenEvent
    data class IssueFilterWithState(val state: State):HomeScreenEvent
    data class GetRepositoryNames(val filter:String):HomeScreenEvent
    data class AddNewFilter(val name:String):HomeScreenEvent
    data object IssueFilter:HomeScreenEvent
    data object GetLabels:HomeScreenEvent
    data class AddNewLabelFilter(val label:String):HomeScreenEvent
    data class UpdateDates(val startDate:LocalDate,val endDate:LocalDate):HomeScreenEvent
    data object ResetFilters:HomeScreenEvent
}