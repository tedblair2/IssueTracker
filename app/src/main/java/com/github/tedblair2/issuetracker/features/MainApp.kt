package com.github.tedblair2.issuetracker.features

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.github.tedblair2.issuetracker.model.ScreenRoutes

@Composable
fun MainApp() {
    val navController= rememberNavController()

    NavHost(navController = navController, startDestination = ScreenRoutes.WelcomeScreen.route){
        welcomeScreen(
            navigateToSignIn = {
                navController.popBackStack(navController.graph.id,true)
                navController.navigateToSignIn()
            },
            navigateToHome = {
                navController.popBackStack(navController.graph.id,true)
                navController.navigateToHome()
            }
        )
        signInScreen(
            navigateToHome = {
                navController.popBackStack(navController.graph.id,true)
                navController.navigateToHome()
            }
        )
        homeScreen(
            navigateToSignIn = {
                navController.popBackStack(navController.graph.id,true)
                navController.navigateToSignIn()
            },
            navigateToIssueDetail = {id->
                navController.navigateToDetailScreen(id)
            }
        )
        issueDetailScreen(
            onNavigateUp = {
                navController.navigateUp()
            }
        )
    }
}