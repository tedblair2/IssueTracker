package com.github.tedblair2.issuetracker.features.welcome.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.tedblair2.issuetracker.R
import com.github.tedblair2.issuetracker.features.welcome.viewmodel.WelcomeViewModel
import kotlinx.coroutines.delay

@Composable
fun WelcomeScreen(
    navigateToSignIn:()->Unit={},
    navigateToHome:()->Unit={}
) {
    val viewModel= hiltViewModel<WelcomeViewModel>()
    val welcomeScreenState by viewModel.welcomeScreenState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = welcomeScreenState.isLoggedIn) {
        delay(1500)
        if (welcomeScreenState.isLoggedIn){
            navigateToHome()
        }else{
            navigateToSignIn()
        }
        viewModel.resetState()
    }

    WelcomeScreenContent()
}

@Composable
private fun WelcomeScreenContent() {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painter = painterResource(id = R.drawable.bug_report_fill0_wght400_grad0_opsz24),
            contentDescription = "app image",
            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .size(85.dp))
        Spacer(modifier = Modifier.height(30.dp))
        CircularProgressIndicator(
            modifier = Modifier
                .size(35.dp)
        )
    }
}

@Preview
@Composable
private fun WelcomePreview() {
    WelcomeScreenContent()
}