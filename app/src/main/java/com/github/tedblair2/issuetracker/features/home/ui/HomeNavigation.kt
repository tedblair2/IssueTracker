package com.github.tedblair2.issuetracker.features.home.ui

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable


@Serializable
data object Home

fun NavGraphBuilder.homeScreen(
    navigateToSignIn: () -> Unit,
    navigateToIssueDetail: (id: String) -> Unit,
    navigateToProfile: () -> Unit
){
    composable<Home>{
        HomeScreen(navigateToSignIn = navigateToSignIn,
            navigateToIssueDetail = navigateToIssueDetail,
            navigateToProfile = navigateToProfile)
    }
}

fun NavController.navigateToHome(){
    navigate(Home)
}