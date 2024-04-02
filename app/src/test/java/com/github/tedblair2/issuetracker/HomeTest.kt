@file:OptIn(ExperimentalCoroutinesApi::class)

package com.github.tedblair2.issuetracker

import androidx.paging.PagingData
import com.github.tedblair2.issuetracker.events.HomeScreenEvent
import com.github.tedblair2.issuetracker.model.HomeScreenState
import com.github.tedblair2.issuetracker.model.SimpleIssue
import com.github.tedblair2.issuetracker.model.User
import com.github.tedblair2.issuetracker.repository.FakeIssueImpl
import com.github.tedblair2.issuetracker.repository.FakeUserServiceImpl
import com.github.tedblair2.issuetracker.repository.IssueRepository
import com.github.tedblair2.issuetracker.repository.SignInService
import com.github.tedblair2.issuetracker.viewmodel.HomeViewModel
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class HomeTest {

    private val signInService:SignInService=SignInTestImpl()
    private val issueService=FakeIssueImpl()
    private val issueRepository=IssueRepository(issueService)
    private val userService=FakeUserServiceImpl()
    private val issueList=listOf(
        SimpleIssue(
            id = "id",
            title = "This is the title for one",
            createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
            author = "author",
            commentCount = 3,
            state = "Open",
            issueNumber = 1
        ),
        SimpleIssue(
            id = "id2",
            title = "This is the title for two",
            createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
            author = "author2",
            commentCount = 1,
            state = "Open",
            issueNumber = 3
        )
    )

    @Disabled
    @Test
    fun initialState(){
        val viewModel = HomeViewModel(signInService = signInService, ioDispatcher = testDispatcher,
            issueRepository = issueRepository, userService = userService)
        val result = viewModel.homeScreenState.value
        val expected = HomeScreenState(
            isLoading = false,
            user = User("t3dd","token"),
            issuesData = PagingData.from(data = issueList)
        )
        assertThat(result).isEqualTo(expected)
    }

    @Disabled
    @Test
    fun success_signOut(){
        val viewModel = HomeViewModel(userService,issueRepository,signInService, testDispatcher)
        viewModel.onEvent(HomeScreenEvent.SignOut)
        val result = viewModel.homeScreenState.value
        val expected = HomeScreenState(
            isLoggedIn = false,
            issuesData = PagingData.empty(),
            user = null,
            isLoading = false
        )
        assertThat(result).isEqualTo(expected)
    }

    companion object {
        @OptIn(ExperimentalCoroutinesApi::class)
        private val testDispatcher= UnconfinedTestDispatcher()

        @JvmStatic
        @BeforeAll
        internal fun setUp(){
            Dispatchers.setMain(testDispatcher)
        }
        @JvmStatic
        @AfterAll
        internal fun tearDown(){
            Dispatchers.resetMain()
            testDispatcher.cancel()
        }
    }
}