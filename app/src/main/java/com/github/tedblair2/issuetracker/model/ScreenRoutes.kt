package com.github.tedblair2.issuetracker.model

const val ARG_1="id"
sealed class ScreenRoutes(val route:String){
    data object SignInScreen:ScreenRoutes("signin")
    data object HomeScreen:ScreenRoutes("home")
    data object WelcomeScreen:ScreenRoutes("welcome")
    data object IssueDetailScreen:ScreenRoutes("details/{$ARG_1}"){
        fun passParam(id:String):String{
            return "details/$id"
        }
    }
    data object ProfileScreen:ScreenRoutes("profile")
}