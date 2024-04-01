package com.github.tedblair2.issuetracker.features

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.paging.compose.collectAsLazyPagingItems
import com.github.tedblair2.issuetracker.R
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
            TopAppBar(
                title = {
                    Text(
                        text = "Home",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold)
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Filled.Settings ,
                            contentDescription = null)
                    }
                })
        }) {paddingValues->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {

            if (homeScreenState.isLoading){
                CircularProgressIndicator()
            }else{
                LazyColumn(modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(6.dp)) {
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
    val openColor=MaterialTheme.colorScheme.primary
    val closedColor=MaterialTheme.colorScheme.error
    var color by remember {
        mutableStateOf(openColor)
    }
    val title= buildAnnotatedString {
        append(simpleIssue.title)
        addStyle(
            style = SpanStyle(
                fontSize = 21.sp
            ),
            start = 0,
            end = simpleIssue.title.length
        )
        addStyle(
            style = SpanStyle(
                fontSize = 21.sp,
                color = Color.Red
            ),
            start = simpleIssue.title.indexOf('#'),
            end = simpleIssue.title.length
        )
    }

    LaunchedEffect(key1 = simpleIssue.state) {
        color = if (simpleIssue.state.lowercase()=="open"){
            openColor
        }else{
            closedColor
        }
    }

    Column (modifier = modifier
        .padding(horizontal = 16.dp , vertical = 6.dp)
        .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)){

        Row(modifier = Modifier
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom) {
            Text(text = "Opened ${simpleIssue.createdAt.dayOfMonth} ${simpleIssue.createdAt.month} ${simpleIssue.createdAt.year}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f))

            Box(modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(color = color),
                contentAlignment = Alignment.Center){
                Text(text = simpleIssue.state,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(horizontal = 10.dp),
                    textAlign = TextAlign.Center,
                    color = Color.White)
            }
        }


        Text(text = title,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            fontSize = 21.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start)

        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom){
            RowItem(
                painter = painterResource(id = R.drawable.baseline_person_outline_24),
                text = simpleIssue.author
            )

            RowItem(
                painter = painterResource(id = R.drawable.baseline_chat_bubble_outline_24),
                text = "${simpleIssue.commentCount} Comments"
            )
        }
    }
}

@Composable
fun RowItem(
    painter: Painter= painterResource(id = R.drawable.baseline_person_outline_24),
    text:String=""
) {
    Row (modifier = Modifier,
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(5.dp)){
        Icon(painter = painter,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            modifier = Modifier
                .size(17.dp))
        Text(text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f))
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