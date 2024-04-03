package com.github.tedblair2.issuetracker.features

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.github.tedblair2.issuetracker.R
import com.github.tedblair2.issuetracker.events.HomeScreenEvent
import com.github.tedblair2.issuetracker.model.HomeScreenState
import com.github.tedblair2.issuetracker.model.ScreenRoutes
import com.github.tedblair2.issuetracker.model.SimpleIssue
import com.github.tedblair2.issuetracker.model.State
import com.github.tedblair2.issuetracker.ui.theme.issue_number_theme_color
import com.github.tedblair2.issuetracker.viewmodel.HomeViewModel
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun HomeScreen(
    navigateToSignIn:()->Unit={},
    navigateToIssueDetail: (id: String) -> Unit,
    navigateToProfile: () -> Unit
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
        navigateToIssueDetail = navigateToIssueDetail,
        navigateToProfile = navigateToProfile)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeScreenContent(
    homeScreenState: HomeScreenState,
    onEvent:(HomeScreenEvent)->Unit,
    navigateToIssueDetail:(id:String)->Unit,
    navigateToProfile:()->Unit
){
    val flow= MutableStateFlow(homeScreenState.issuesData)
    val reposFlow= MutableStateFlow(homeScreenState.repositoryNames)
    val issues=flow.collectAsLazyPagingItems()
    val repositories=reposFlow.collectAsLazyPagingItems()
    val loadState=issues.loadState.refresh
    val context= LocalContext.current
    val request=ImageRequest.Builder(context)
        .data(homeScreenState.user?.avatar)
        .placeholder(R.drawable.baseline_person_24)
        .error(R.drawable.baseline_person_24)
        .build()
    var showDialog by remember {
        mutableStateOf(false)
    }
    var showBottomSheet by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = loadState) {
        if (loadState is LoadState.Error){
            Toast.makeText(context , "Error loading data" , Toast.LENGTH_SHORT).show()
        }
    }

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
                    IconButton(onClick = {onEvent(HomeScreenEvent.ShowFilters) }) {
                        Icon(painter = painterResource(id = R.drawable.baseline_filter_list_alt_24) ,
                            contentDescription = "filter")
                    }
                    AsyncImage(
                        model = request ,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 5.dp , start = 8.dp)
                            .size(35.dp)
                            .clip(CircleShape)
                            .clickable { navigateToProfile() })
                })
        }) {paddingValues->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues = paddingValues)){
            Column(modifier = Modifier
                .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {

                if (loadState is LoadState.Loading){
                    CircularProgressIndicator()
                }else{
                    AnimatedVisibility(visible = homeScreenState.showFilters) {
                        LazyRow(modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 5.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                            LazyRowItem(
                                onClick = {

                                },
                                text = "Date"
                            )

                            LazyRowItem(
                                onClick = {
                                          showDialog=true
                                },
                                text = "State"
                            )

                            LazyRowItem(
                                onClick = {

                                },
                                text = "Labels"
                            )

                            LazyRowItem(
                                onClick = {
                                    showBottomSheet=true
                                    onEvent(HomeScreenEvent.GetRepositoryNames(""))
                                },
                                text = "Repository"
                            )
                        }
                    }
                    LazyColumn(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(count = issues.itemCount){index ->
                            val issue=issues[index] ?: SimpleIssue()
                            SingleIssue(simpleIssue = issue,
                                modifier =Modifier
                                    .clickable { navigateToIssueDetail(issue.id) })
                            HorizontalDivider(modifier = Modifier
                                .padding(top = 5.dp))
                        }
                    }
                }
            }

            if (showDialog){
                StateDialog(onDismiss = { showDialog=false },
                    onSelect = {state ->
                        onEvent(HomeScreenEvent.IssueFilterWithState(state))
                        showDialog=false
                    },
                    currentState = homeScreenState.currentState)
            }

            if (showBottomSheet){
                RepositoryBottomSheet(
                    repositories = repositories,
                    onDismiss = {
                        showBottomSheet = !showBottomSheet
                        onEvent(HomeScreenEvent.IssueFilterWithRepository)
                    },
                    onRepositoryClick = {
                        onEvent(HomeScreenEvent.AddNewFilter(it))
                    },
                    onSearch = {
                        onEvent(HomeScreenEvent.GetRepositoryNames(it))
                    },
                    repositoryFilterList = homeScreenState.repositoryFilter)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepositoryBottomSheet(
    onDismiss: () -> Unit={},
    repositories:LazyPagingItems<String>,
    onRepositoryClick:(name:String)->Unit={},
    onSearch:(String)->Unit={},
    repositoryFilterList: List<String> = listOf()
) {
    val modalBottomSheetState= rememberModalBottomSheetState()
    val (text,onTextChange)= remember {
        mutableStateOf("")
    }
    val loadState=repositories.loadState.refresh
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = modalBottomSheetState){

        Text(text = "Your Repositories" ,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(vertical = 10.dp)
                .fillMaxWidth())

        TextField(value =text ,
            onValueChange = {
                onTextChange(it)
                onSearch(it)
            },
            placeholder = {
                Text(text = "Search repositories...",
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp , vertical = 12.dp))

        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp , horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {

            item {
                if (loadState is LoadState.Loading){
                    CircularProgressIndicator()
                }
            }

            items(count = repositories.itemCount){
                val name=repositories[it] ?: "name"
                val isChecked=repositoryFilterList.contains(name)
                CheckBoxItem(
                    text = name,
                    isChecked = isChecked,
                    onChecked = {
                        onRepositoryClick(name)
                    },
                    modifier = Modifier
                        .clickable {
                            onRepositoryClick(name)
                        }
                )
            }
            item {
                Spacer(modifier = Modifier
                    .height(30.dp))
            }
        }
    }
}

@Composable
fun CheckBoxItem(
    modifier: Modifier=Modifier,
    isChecked:Boolean=false,
    onChecked:(Boolean)->Unit={},
    text: String=""
) {
    Row(modifier = modifier
        .fillMaxWidth()
        .padding(start = 10.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = isChecked ,
            onCheckedChange = onChecked)
        Spacer(modifier = Modifier.width(5.dp))
        Text(text = text)
    }
}

@Composable
fun StateDialog(
    onDismiss:()->Unit,
    onSelect:(state:State)->Unit,
    currentState: State
) {
    val options= listOf(State.ALL,State.CLOSED,State.OPEN)
    Dialog(onDismissRequest = onDismiss) {
        Card(modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier
                .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {

                Text(text = "Issue State" ,
                    style =MaterialTheme.typography.titleLarge)

                options.forEach {state->
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .selectable(
                            selected = state == currentState ,
                            onClick = { onSelect(state) } ,
                            role = Role.RadioButton
                        )
                        .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically) {
                        
                        RadioButton(selected = (state==currentState),
                            onClick = null)
                        Text(text = state.name,
                            modifier = Modifier.padding(start = 16.dp))
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
    val commentInfo by remember {
        derivedStateOf {
            if (simpleIssue.commentCount==1){
                "1 Comment"
            }else{
                "${simpleIssue.commentCount} Comments"
            }
        }
    }
    val title= buildAnnotatedString {
        append(simpleIssue.title)
        addStyle(
            style = SpanStyle(
                fontSize = 18.sp
            ),
            start = 0,
            end = simpleIssue.title.length
        )
        addStyle(
            style = SpanStyle(
                fontSize = 18.sp,
                color = issue_number_theme_color
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
            fontSize = 19.sp,
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
                text = commentInfo
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

fun LazyListScope.LazyRowItem(
    onClick:()->Unit={},
    text: String=""
) {
    item {
        OutlinedButton(onClick = onClick,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .height(33.dp)) {
            Text(text = text, fontSize = 12.sp)
            Icon(imageVector = Icons.Filled.ArrowDropDown ,
                contentDescription = null,
                modifier = Modifier.size(17.dp))
        }
    }
}

fun NavGraphBuilder.homeScreen(
    navigateToSignIn: () -> Unit,
    navigateToIssueDetail: (id: String) -> Unit,
    navigateToProfile: () -> Unit
){
    composable(route = ScreenRoutes.HomeScreen.route){
        HomeScreen(navigateToSignIn = navigateToSignIn,
            navigateToIssueDetail = navigateToIssueDetail,
            navigateToProfile = navigateToProfile)
    }
}

fun NavController.navigateToHome(){
    navigate(ScreenRoutes.HomeScreen.route)
}