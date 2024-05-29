package com.github.tedblair2.issuetracker

import androidx.lifecycle.SavedStateHandle
import com.github.tedblair2.issuetracker.events.SignInScreenEvent
import com.github.tedblair2.issuetracker.model.SignInScreenState
import com.github.tedblair2.issuetracker.repository.SignInService
import com.github.tedblair2.issuetracker.features.signin.viewmodel.SignInViewModel
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.jupiter.api.Test

class SigninScreenTest {

    private val signInService:SignInService=SignInTestImpl()
    private val savedStateHandle=SavedStateHandle()
    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher= UnconfinedTestDispatcher()
    @Test
    fun defaultScreenState() {
        val viewmodel = SignInViewModel(savedStateHandle,signInService,testDispatcher)
        val result = viewmodel.signInScreenState.value
        val expected = SignInScreenState()
        assertThat(result).isEqualTo(expected)
    }
    @Test
    fun updateEmail(){
        val email = ":email:"
        val viewmodel = SignInViewModel(savedStateHandle, signInService,testDispatcher)
        viewmodel.onEvent(SignInScreenEvent.TypeEmail(email))
        val result = viewmodel.signInScreenState.value
        val expected = SignInScreenState(email = email,isWrongEmailFormat = false)
        assertThat(result).isEqualTo(expected)
    }
    @Test
    fun resetState(){
        val viewModel = SignInViewModel(savedStateHandle,signInService,testDispatcher)
        viewModel.onEvent(SignInScreenEvent.ResetState)
        val result = viewModel.signInScreenState.value
        val expected = SignInScreenState()
        assertThat(result).isEqualTo(expected)
    }
}