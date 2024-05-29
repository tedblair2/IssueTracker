package com.github.tedblair2.issuetracker.features.signin.ui

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.tedblair2.issuetracker.features.signin.ui.SignInScreen
import com.github.tedblair2.issuetracker.model.ScreenRoutes

fun NavGraphBuilder.signInScreen(
    navigateToHome: () -> Unit
){
    composable(route = ScreenRoutes.SignInScreen.route){
        SignInScreen(navigateToHome = navigateToHome)
    }
}

fun NavController.navigateToSignIn(){
    navigate(ScreenRoutes.SignInScreen.route)
}