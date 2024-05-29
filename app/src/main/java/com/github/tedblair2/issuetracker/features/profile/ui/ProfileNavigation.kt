package com.github.tedblair2.issuetracker.features.profile.ui

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.tedblair2.issuetracker.features.profile.ui.ProfileScreen
import com.github.tedblair2.issuetracker.model.ScreenRoutes

fun NavGraphBuilder.profileScreen(
    onNavigateUp: () -> Unit,
    navigateToSignIn: () -> Unit
){
    composable(route = ScreenRoutes.ProfileScreen.route){
        ProfileScreen(onNavigateUp = onNavigateUp,
            navigateToSignIn = navigateToSignIn)
    }
}

fun NavController.navigateToProfile(){
    navigate(ScreenRoutes.ProfileScreen.route)
}