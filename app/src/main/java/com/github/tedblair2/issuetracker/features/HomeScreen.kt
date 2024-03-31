package com.github.tedblair2.issuetracker.features

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.paging.compose.collectAsLazyPagingItems
import com.github.tedblair2.issuetracker.events.HomeScreenEvent
import com.github.tedblair2.issuetracker.model.HomeScreenState
import com.github.tedblair2.issuetracker.model.ScreenRoutes
import com.github.tedblair2.issuetracker.model.SimpleIssue
import com.github.tedblair2.issuetracker.viewmodel.HomeViewModel
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun HomeScreen(
    navigateToSignIn:()->Unit={},
    navigateToIssueDetail: (id: String) -> Unit
) {
    val viewModel= hiltViewModel<HomeViewModel>()
    val homeScreenState by viewModel.homeScreenState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = homeScreenState.isLoggedIn) {
        if (!homeScreenState.isLoggedIn){
            navigateToSignIn()
        }
    }

    HomeScreenContent(
        homeScreenState = homeScreenState,
        onEvent = viewModel::onEvent,
        navigateToIssueDetail = navigateToIssueDetail)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeScreenContent(
    homeScreenState: HomeScreenState,
    onEvent:(HomeScreenEvent)->Unit,
    navigateToIssueDetail:(id:String)->Unit
){
    val flow= MutableStateFlow(homeScreenState.issuesData)
    val issues=flow.collectAsLazyPagingItems()

    Scaffold(modifier = Modifier
        .fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Home",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold)
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ))
        }) {paddingValues->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {

            if (homeScreenState.isLoading){
                CircularProgressIndicator()
            }else{
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(count = issues.itemCount){index ->
                        val issue=issues[index] ?: SimpleIssue()
                        SingleIssue(simpleIssue = issue,
                            modifier =Modifier.clickable { navigateToIssueDetail(issue.id) } )
                    }
                }
            }
        }
    }
}

@Composable
fun SingleIssue(
    modifier: Modifier=Modifier,
    simpleIssue: SimpleIssue
) {

    Column (modifier = modifier
        .padding(horizontal = 12.dp, vertical = 6.dp)
        .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(5.dp)){

        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "Opened ${simpleIssue.createdAt.dayOfMonth} ${simpleIssue.createdAt.month} ${simpleIssue.createdAt.year}", fontSize = 14.sp)
            Text(text = simpleIssue.state, fontSize = 15.sp)
        }
        Text(text = simpleIssue.title,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            fontSize = 18.sp)
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween){
            Text(text = simpleIssue.author, 
                fontSize = 14.sp)
            Text(text = "${simpleIssue.commentCount} Comments", fontSize = 14.sp)
        }
    }
}

fun NavGraphBuilder.homeScreen(
    navigateToSignIn: () -> Unit,
    navigateToIssueDetail: (id: String) -> Unit
){
    composable(route = ScreenRoutes.HomeScreen.route){
        HomeScreen(navigateToSignIn = navigateToSignIn,
            navigateToIssueDetail = navigateToIssueDetail)
    }
}

fun NavController.navigateToHome(){
    navigate(ScreenRoutes.HomeScreen.route)
}