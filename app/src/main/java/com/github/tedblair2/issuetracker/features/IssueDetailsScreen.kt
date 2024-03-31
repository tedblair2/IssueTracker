package com.github.tedblair2.issuetracker.features

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.github.tedblair2.issuetracker.R
import com.github.tedblair2.issuetracker.events.IssueDetailEvent
import com.github.tedblair2.issuetracker.model.ARG_1
import com.github.tedblair2.issuetracker.model.Comment
import com.github.tedblair2.issuetracker.model.DetailedIssue
import com.github.tedblair2.issuetracker.model.IssueDetailScreenState
import com.github.tedblair2.issuetracker.model.ScreenRoutes
import com.github.tedblair2.issuetracker.ui.theme.IssueTrackerTheme
import com.github.tedblair2.issuetracker.viewmodel.DetailViewModel
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
    IssueDetailsContent(issueDetailScreenState = issueDetails)
}


@Composable
fun IssueDetailsContent(issueDetailScreenState: IssueDetailScreenState) {
    val context= LocalContext.current
    val flow= MutableStateFlow(issueDetailScreenState.comments)
    val comments=flow.collectAsLazyPagingItems()

    var color by remember {
        mutableStateOf(Color.Blue)
    }

    LaunchedEffect(key1 = issueDetailScreenState.issue.state) {
        color = if (issueDetailScreenState.issue.state.lowercase()=="open"){
            Color.Blue
        }else{
            Color.Red
        }
    }

    LaunchedEffect(key1 = issueDetailScreenState.isError) {
        if (issueDetailScreenState.isError){
            Toast.makeText(context, issueDetailScreenState.errorMsg, Toast.LENGTH_SHORT).show()
        }
    }
    LazyColumn(modifier = Modifier
        .fillMaxSize()) {
        item {
            Box(modifier = Modifier
                .fillMaxWidth()) {
                if (issueDetailScreenState.isLoading){
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }else{
                    Column(modifier = Modifier
                        .padding(
                            horizontal = 15.dp, vertical = 6.dp
                        )
                        .fillMaxWidth()
                        .align(Alignment.TopCenter),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = issueDetailScreenState.issue.title,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold)

                        Row(modifier = Modifier
                            .padding(vertical = 7.dp)
                            .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "Opened by ${issueDetailScreenState.issue.author} on ${issueDetailScreenState.issue.createdAt.dayOfMonth} ${issueDetailScreenState.issue.createdAt.month} ${issueDetailScreenState.issue.createdAt.year}",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Light,
                                modifier = Modifier
                                    .weight(4f)
                                    .padding(end = 5.dp))
                            Text(text = issueDetailScreenState.issue.state,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(15.dp))
                                    .background(color = color)
                                    .weight(1f)
                                    .padding(start = 3.dp, end = 3.dp),
                                textAlign = TextAlign.Center,
                                fontSize = 16.sp,
                                color = Color.White)
                        }

                        HorizontalDivider()

                        Text(text = issueDetailScreenState.issue.description,
                            fontSize = 20.sp,
                            modifier = Modifier
                                .padding(vertical = 10.dp)
                                .fillMaxWidth())

                        HorizontalDivider()

                        Text(text = "${comments.itemCount} Comments", fontSize = 18.sp,
                            modifier = Modifier
                                .padding(vertical = 6.dp)
                                .fillMaxWidth())
                    }
                }
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

@Composable
fun SingleComment(
    modifier: Modifier=Modifier,
    comment: Comment
) {
    val context= LocalContext.current
    val request=ImageRequest.Builder(context)
        .data(comment.avatar)
        .error(R.drawable.baseline_person_24)
        .placeholder(R.drawable.baseline_person_24)
        .build()
    Row(modifier = modifier
        .padding(vertical = 6.dp, horizontal = 15.dp)
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
            Text(text = "${comment.author} commented on ${comment.createdAt.dayOfMonth} ${comment.createdAt.month} ${comment.createdAt.year}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Light)
            Text(text = comment.body)
        }
    }
}

fun NavGraphBuilder.issueDetailScreen(
    onNavigateUp: () -> Unit
){
    composable(
        route = ScreenRoutes.IssueDetailScreen.route,
        arguments = listOf(
            navArgument(ARG_1){
                type= NavType.StringType
            }
        )
    ){navBackStack->
        val id=navBackStack.arguments?.getString(ARG_1).toString()
        IssueDetailScreen(
            id = id,
            onNavigateUp=onNavigateUp)
    }
}

fun NavHostController.navigateToDetailScreen(id: String){
    navigate(ScreenRoutes.IssueDetailScreen.passParam(id))
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