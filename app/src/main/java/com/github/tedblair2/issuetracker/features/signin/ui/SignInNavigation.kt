package com.github.tedblair2.issuetracker.features.signin.ui

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object SignIn

fun NavGraphBuilder.signInScreen(
    navigateToHome: () -> Unit
){
    composable<SignIn>{
        SignInScreen(navigateToHome = navigateToHome)
    }
}

fun NavController.navigateToSignIn(options: NavOptionsBuilder.()->Unit={}){
    navigate(SignIn){
        options()
    }
}