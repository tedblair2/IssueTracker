package com.github.tedblair2.issuetracker.features.issuedetails.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.github.tedblair2.issuetracker.R
import com.github.tedblair2.issuetracker.events.IssueDetailEvent
import com.github.tedblair2.issuetracker.features.issuedetails.viewmodel.DetailViewModel
import com.github.tedblair2.issuetracker.model.Comment
import com.github.tedblair2.issuetracker.model.DetailedIssue
import com.github.tedblair2.issuetracker.model.IssueDetailScreenState
import com.github.tedblair2.issuetracker.ui.theme.IssueTrackerTheme
import com.github.tedblair2.issuetracker.ui.theme.issue_number_theme_color
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun IssueDetailScreen(
    id:String="",
    onNavigateUp:()->Unit
) {
    val viewModel= hiltViewModel<DetailViewModel>()
    val issueDetails by viewModel.detailScreenState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = true) {
        viewModel.onEvent(IssueDetailEvent.GetDetailedIssue(id))
    }
    IssueDetailsContent(
        issueDetailScreenState = issueDetails,
        onNavigateUp = onNavigateUp)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun IssueDetailsContent(
    issueDetailScreenState: IssueDetailScreenState,
    onNavigateUp: () -> Unit={}
) {
    val context= LocalContext.current
    val flow= MutableStateFlow(issueDetailScreenState.comments)
    val comments=flow.collectAsLazyPagingItems()

    val openColor= MaterialTheme.colorScheme.primary
    val closedColor= MaterialTheme.colorScheme.error
    var color by remember {
        mutableStateOf(openColor)
    }

    val dateTxt= buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                fontSize = 14.sp
            )
        ){
            append("${issueDetailScreenState.issue.createdAt.hour.pad()}:${issueDetailScreenState.issue.createdAt.minute.pad()}")
        }
        append(" \u2022 ")
        withStyle(
            style = SpanStyle(
                fontSize = 14.sp
            )
        ){
            append("${issueDetailScreenState.issue.createdAt.dayOfMonth} ${issueDetailScreenState.issue.createdAt.month} ${issueDetailScreenState.issue.createdAt.year}")
        }
    }

    val title= buildAnnotatedString {
        append(issueDetailScreenState.issue.title)
        addStyle(
            style = SpanStyle(
                fontSize = 21.sp
            ),
            start = 0,
            end = issueDetailScreenState.issue.title.length
        )
        addStyle(
            style = SpanStyle(
                fontSize = 21.sp,
                color = issue_number_theme_color
            ),
            start = issueDetailScreenState.issue.title.indexOf('#'),
            end = issueDetailScreenState.issue.title.length
        )
    }

    val request=ImageRequest.Builder(context)
        .data(issueDetailScreenState.issue.avatar)
        .error(R.drawable.baseline_person_24)
        .placeholder(R.drawable.baseline_person_24)
        .build()

    LaunchedEffect(key1 = issueDetailScreenState.issue.state) {
        color = if (issueDetailScreenState.issue.state.lowercase()=="open"){
            openColor
        }else{
            closedColor
        }
    }

    LaunchedEffect(key1 = issueDetailScreenState.isError) {
        if (issueDetailScreenState.isError){
            Toast.makeText(context, issueDetailScreenState.errorMsg, Toast.LENGTH_SHORT).show()
        }
    }
    Scaffold(modifier = Modifier
        .fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = title,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis)
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack ,
                            contentDescription = "back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ))
        }
    ) {paddingValues->


        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)){
            if (issueDetailScreenState.isLoading){
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }else{
                LazyColumn(modifier = Modifier
                    .fillMaxSize()) {
                    item {
                        Column(modifier = Modifier
                            .padding(horizontal = 15.dp)
                            .fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally) {

                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 7.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween) {

                                Row (modifier = Modifier,
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)){
                                    Box(modifier = Modifier
                                        .size(35.dp)
                                        .clip(CircleShape),
                                        contentAlignment = Alignment.Center){
                                        AsyncImage(
                                            model = request,
                                            contentDescription = "author",
                                            modifier = Modifier.fillMaxSize())
                                    }
                                    Text(text = issueDetailScreenState.issue.author,
                                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f))
                                }
                                Box(modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(color = color),
                                    contentAlignment = Alignment.Center){
                                    Text(text = issueDetailScreenState.issue.state,
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier
                                            .padding(horizontal = 10.dp),
                                        textAlign = TextAlign.Center,
                                        color = Color.White)
                                }
                            }

                            Text(text = issueDetailScreenState.issue.description,
                                fontSize = 18.sp,
                                modifier = Modifier
                                    .padding(vertical = 10.dp)
                                    .fillMaxWidth())

                            Text(text = dateTxt,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 7.dp))
                        }
                    }
                    item {
                        HorizontalDivider()
                    }
                    items(count = comments.itemCount){
                        val comment=comments[it] ?: Comment()
                        SingleComment(comment = comment)
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

fun usernameMatch(input:String):Sequence<MatchResult>{
    val regex=Regex("@\\w+")
    return regex.findAll(input)
}

@Composable
fun SingleComment(
    modifier: Modifier=Modifier,
    comment: Comment
) {
    val title= buildAnnotatedString {
        append(comment.author)
        withStyle(
            style = SpanStyle(
                fontSize = 17.sp
            )
        ){
            append(" \u2022 ")
        }
        append(comment.duration)
    }

    val text= buildAnnotatedString {
        append(comment.body)
        usernameMatch(comment.body).forEach { matchResult ->
            addStyle(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.primary
                ),
                start = matchResult.range.first,
                end = matchResult.range.last.plus(1)
            )
        }
    }

    val context= LocalContext.current
    val request=ImageRequest.Builder(context)
        .data(comment.avatar)
        .error(R.drawable.baseline_person_24)
        .placeholder(R.drawable.baseline_person_24)
        .build()
    Row(modifier = modifier
        .padding(vertical = 6.dp , horizontal = 15.dp)
        .fillMaxWidth()) {
        Box(modifier = Modifier
            .size(30.dp)
            .clip(CircleShape),
            contentAlignment = Alignment.Center){
            AsyncImage(
                model = request,
                contentDescription = null,
                modifier = Modifier.fillMaxSize())
        }
        Column(modifier = Modifier
            .weight(1f)
            .padding(start = 8.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Text(text = title,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f))
            Text(text = text)
        }
    }
}

fun Int.pad(): String {
    return this.toString().padStart(2, '0')
}

@PreviewLightDark
@Composable
private fun IssueDetailPreview() {
    IssueTrackerTheme {
        IssueDetailsContent(
            issueDetailScreenState = IssueDetailScreenState(
                isLoading = false,
                issue = DetailedIssue(
                    id="id",
                    title = "This is the title",
                    description = "Here is the description",
                    state = "CLOSED",
                    author = "tedblair2"
                )
            )
        )
    }
}