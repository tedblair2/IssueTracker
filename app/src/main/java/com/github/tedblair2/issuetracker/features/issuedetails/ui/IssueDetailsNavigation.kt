package com.github.tedblair2.issuetracker.features.issuedetails.ui

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

@Serializable
data class Details(val id:String)

fun NavGraphBuilder.issueDetailScreen(
    onNavigateUp: () -> Unit
){
    composable<Details>{navBackStack->
        val args=navBackStack.toRoute<Details>()
        IssueDetailScreen(
            id = args.id,
            onNavigateUp=onNavigateUp
        )
    }
}

fun NavHostController.navigateToDetailScreen(id: String,options:NavOptionsBuilder.()->Unit={}){
    navigate(Details(id)){
        options()
    }
}