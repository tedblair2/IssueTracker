package com.github.tedblair2.issuetracker.features.welcome.ui

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object Welcome
fun NavGraphBuilder.welcomeScreen(
    navigateToSignIn: () -> Unit,
    navigateToHome: () -> Unit
){
    composable<Welcome>{
        WelcomeScreen(
            navigateToSignIn = navigateToSignIn,
            navigateToHome = navigateToHome
        )
    }
}

fun NavController.navigateToWelcome(){
    navigate(Welcome)
}