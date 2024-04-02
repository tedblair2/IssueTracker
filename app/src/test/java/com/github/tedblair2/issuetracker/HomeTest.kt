package com.github.tedblair2.issuetracker

import com.github.tedblair2.issuetracker.repository.SignInService
import com.github.tedblair2.issuetracker.viewmodel.HomeViewModel
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.jupiter.api.Test

class HomeTest {

    private val signInService:SignInService=SignInTestImpl()
    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher= UnconfinedTestDispatcher()
    @Test
    fun defaultState(){
        val viewModel = HomeViewModel(signInService = signInService, ioDispatcher = testDispatcher)
        val result = viewModel.homeScreenState.value
        assertThat(result).isEqualTo(expected)
    }
}