package com.github.tedblair2.issuetracker.features.profile.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.github.tedblair2.issuetracker.R
import com.github.tedblair2.issuetracker.events.ProfileScreenEvent
import com.github.tedblair2.issuetracker.model.User
import com.github.tedblair2.issuetracker.features.profile.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    onNavigateUp:()->Unit,
    navigateToSignIn:()->Unit
) {
    val viewModel= hiltViewModel<ProfileViewModel>()
    val profileScreenState by viewModel.profileScreenState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = profileScreenState.isLoggedIn) {
        if (!profileScreenState.isLoggedIn){
            navigateToSignIn()
        }
    }

    ProfileScreenContent(
        user = profileScreenState.currentUser,
        onEvent = viewModel::onEvent,
        onNavigateUp = onNavigateUp)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileScreenContent(
    user: User?=null,
    onEvent:(ProfileScreenEvent)->Unit={},
    onNavigateUp: () -> Unit={}
) {
    val context= LocalContext.current
    val request=ImageRequest.Builder(context)
        .data(user?.avatar)
        .error(R.drawable.baseline_person_24)
        .placeholder(R.drawable.baseline_person_24)
        .build()
    var showDialog by remember {
        mutableStateOf(false)
    }
    Scaffold(modifier = Modifier
        .fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text(text = "Profile") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack ,
                            contentDescription = null)
                    }
                })
        }
    ) {paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues = paddingValues)){
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(top = 15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(15.dp)) {

                AsyncImage(model = request,
                    contentDescription = null,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape))

                ProfileRowItem(painter = painterResource(id = R.drawable.baseline_person_24) ,
                    text = user?.username ?: "")

                ProfileRowItem(
                    painter = painterResource(id = R.drawable.baseline_email_24) ,
                    text = user?.email ?: "")

                Button(onClick = { showDialog=true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp , horizontal = 10.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Logout")
                }
            }

            if (showDialog){
                ConfirmActionDialog(
                    onDismissRequest = { showDialog=false },
                    onConfirmation = {
                        showDialog = false
                        onEvent(ProfileScreenEvent.SignOut)
                    },
                    dialogTitle = "Logout" ,
                    dialogText = "Are you sure you want to logout?" ,
                    icon = painterResource(id = R.drawable.baseline_logout_24)
                )
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmActionDialog(
    onDismissRequest: () -> Unit ,
    onConfirmation: () -> Unit ,
    dialogTitle: String ,
    dialogText: String ,
    icon: Painter ,
) {
    AlertDialog(
        icon = {
            Icon(icon,
                contentDescription = "Logout Icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}

@Composable
fun ProfileRowItem(
    painter: Painter,
    text:String
) {
    Row (modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically){

        Image(painter = painter ,
            contentDescription = null,
            modifier = Modifier
                .padding(end = 15.dp)
                .size(30.dp))
        Text(text = text,
            fontSize = 19.sp)
    }
}

@Preview
@Composable
private fun ProfileScreenPreview() {
    ProfileScreenContent()
}