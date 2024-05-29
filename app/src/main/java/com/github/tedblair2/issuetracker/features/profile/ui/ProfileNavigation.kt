package com.github.tedblair2.issuetracker.features.profile.ui

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object Profile

fun NavGraphBuilder.profileScreen(
    onNavigateUp: () -> Unit,
    navigateToSignIn: () -> Unit
){
    composable<Profile>{
        ProfileScreen(onNavigateUp = onNavigateUp,
            navigateToSignIn = navigateToSignIn
        )
    }
}

fun NavController.navigateToProfile(options:NavOptionsBuilder.()->Unit={}){
    navigate(Profile){
        options()
    }
}