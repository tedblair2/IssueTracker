package com.github.tedblair2.issuetracker.features

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.github.tedblair2.issuetracker.features.home.ui.homeScreen
import com.github.tedblair2.issuetracker.features.home.ui.navigateToHome
import com.github.tedblair2.issuetracker.features.issuedetails.ui.issueDetailScreen
import com.github.tedblair2.issuetracker.features.issuedetails.ui.navigateToDetailScreen
import com.github.tedblair2.issuetracker.features.profile.ui.navigateToProfile
import com.github.tedblair2.issuetracker.features.profile.ui.profileScreen
import com.github.tedblair2.issuetracker.features.signin.ui.navigateToSignIn
import com.github.tedblair2.issuetracker.features.signin.ui.signInScreen
import com.github.tedblair2.issuetracker.features.welcome.ui.Welcome
import com.github.tedblair2.issuetracker.features.welcome.ui.welcomeScreen

@Composable
fun MainApp() {
    val navController= rememberNavController()

    NavHost(navController = navController, startDestination = Welcome){
        welcomeScreen(
            navigateToSignIn = {
                navController.navigateToSignIn(options = {
                    popUpTo(navController.graph.id){
                        inclusive=true
                    }
                })
            },
            navigateToHome = {
                navController.navigateToHome(options = {
                    popUpTo(navController.graph.id){
                        inclusive=true
                    }
                })
            }
        )
        signInScreen(
            navigateToHome = {
                navController.navigateToHome(options = {
                    popUpTo(navController.graph.id){
                        inclusive=true
                    }
                })
            }
        )
        homeScreen(
            navigateToSignIn = {
                navController.navigateToSignIn(options = {
                    popUpTo(navController.graph.id){
                        inclusive=true
                    }
                })
            },
            navigateToIssueDetail = {id->
                navController.navigateToDetailScreen(id)
            },
            navigateToProfile = {
                navController.navigateToProfile()
            }
        )
        issueDetailScreen(
            onNavigateUp = {
                navController.navigateUp()
            }
        )
        profileScreen(
            onNavigateUp = {
                navController.navigateUp()
            },
            navigateToSignIn = {
                navController.navigateToSignIn(options ={
                    popUpTo(navController.graph.id){
                        inclusive=true
                    }
                })
            }
        )
    }
}