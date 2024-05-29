package com.github.tedblair2.issuetracker.features.home.ui

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.tedblair2.issuetracker.features.home.ui.HomeScreen
import com.github.tedblair2.issuetracker.model.ScreenRoutes

fun NavGraphBuilder.homeScreen(
    navigateToSignIn: () -> Unit,
    navigateToIssueDetail: (id: String) -> Unit,
    navigateToProfile: () -> Unit
){
    composable(route = ScreenRoutes.HomeScreen.route){
        HomeScreen(navigateToSignIn = navigateToSignIn,
            navigateToIssueDetail = navigateToIssueDetail,
            navigateToProfile = navigateToProfile)
    }
}

fun NavController.navigateToHome(){
    navigate(ScreenRoutes.HomeScreen.route)
}