package com.github.tedblair2.issuetracker.features.welcome.ui

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.tedblair2.issuetracker.features.welcome.ui.WelcomeScreen
import com.github.tedblair2.issuetracker.model.ScreenRoutes

fun NavGraphBuilder.welcomeScreen(
    navigateToSignIn: () -> Unit,
    navigateToHome: () -> Unit
){
    composable(route = ScreenRoutes.WelcomeScreen.route){
        WelcomeScreen(
            navigateToSignIn = navigateToSignIn,
            navigateToHome = navigateToHome
        )
    }
}

fun NavController.navigateToWelcome(){
    navigate(ScreenRoutes.WelcomeScreen.route)
}