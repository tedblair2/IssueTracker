package com.github.tedblair2.issuetracker.features.signin.ui

import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.tedblair2.issuetracker.R
import com.github.tedblair2.issuetracker.events.SignInScreenEvent
import com.github.tedblair2.issuetracker.model.SignInScreenState
import com.github.tedblair2.issuetracker.features.signin.viewmodel.SignInViewModel

@Composable
fun SignInScreen(
    navigateToHome:()->Unit={},
) {
    val viewModel= hiltViewModel<SignInViewModel>()
    val signInScreenState by viewModel.signInScreenState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = signInScreenState.loginSuccess) {
        if (signInScreenState.loginSuccess){
            navigateToHome()
            viewModel.onEvent(SignInScreenEvent.ResetState)
        }
    }
    SignInScreenContent(
        state = signInScreenState,
        onEvent = viewModel::onEvent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SignInScreenContent(
    state:SignInScreenState= SignInScreenState(),
    onEvent:(SignInScreenEvent)->Unit={},
){
    val context= LocalContext.current
    val activity=context.getActivity()
    
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                Text(
                    text = "Sign In",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold)
                        },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ))
        }
    ) {paddingValues->
        Column(modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .padding(horizontal = 17.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)) {
            
            Text(text = "Kindly sign in with your Github Email" ,
                fontSize = 15.sp,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth()
                    .alpha(0.6f))
            
            OutlinedTextField(
                value = state.email,
                onValueChange ={onEvent(SignInScreenEvent.TypeEmail(it))},
                label = {
                    Text(text = "Github Email",
                        fontSize = 15.sp,
                        modifier = Modifier
                            .alpha(0.5f))
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_email_24),
                        contentDescription = null
                    )
                },
                trailingIcon = {
                    if (state.email.isNotEmpty()) {
                        IconButton(onClick = { onEvent(SignInScreenEvent.TypeEmail("")) }) {
                            Icon(
                                imageVector = Icons.Filled.Clear ,
                                contentDescription = null
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(),
                isError = state.isWrongEmailFormat,
                supportingText = {
                    if (state.isWrongEmailFormat){
                        Text(text = "Provide Correct Email Format")
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        activity?.let {
                            onEvent(SignInScreenEvent.SignIn(activity))
                        }
                    }
                )
            )

            Button(onClick = {
                activity?.let {
                    onEvent(SignInScreenEvent.SignIn(activity))
                }
            },
                shape = RoundedCornerShape(7.dp),
                modifier = Modifier
                    .padding(vertical = 5.dp)
                    .fillMaxWidth()) {
                Text(text = "Sign In",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(vertical = 7.dp))
            }
        }
    }

}

fun Context.getActivity():ComponentActivity?{
    var currentContext=this
    while (currentContext is ContextWrapper){
        if (currentContext is ComponentActivity){
            return currentContext
        }
        currentContext=currentContext.baseContext
    }
    return null
}

@Preview
@Composable
private fun SignInPreview() {
    SignInScreenContent()
}