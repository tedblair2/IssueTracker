package com.github.tedblair2.issuetracker.features.issuedetails.ui

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.github.tedblair2.issuetracker.model.ARG_1
import com.github.tedblair2.issuetracker.model.ScreenRoutes

fun NavGraphBuilder.issueDetailScreen(
    onNavigateUp: () -> Unit
){
    composable(
        route = ScreenRoutes.IssueDetailScreen.route,
        arguments = listOf(
            navArgument(ARG_1){
                type= NavType.StringType
            }
        )
    ){navBackStack->
        val id=navBackStack.arguments?.getString(ARG_1).toString()
        IssueDetailScreen(
            id = id,
            onNavigateUp=onNavigateUp)
    }
}

fun NavHostController.navigateToDetailScreen(id: String){
    navigate(ScreenRoutes.IssueDetailScreen.passParam(id))
}