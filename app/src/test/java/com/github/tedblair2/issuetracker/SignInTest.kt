package com.github.tedblair2.issuetracker

import androidx.lifecycle.SavedStateHandle
import com.github.tedblair2.issuetracker.events.SignInScreenEvent
import com.github.tedblair2.issuetracker.model.SignInScreenState
import com.github.tedblair2.issuetracker.repository.SignInService
import com.github.tedblair2.issuetracker.viewmodel.SignInViewModel
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.jupiter.api.Test

class SignInTest {

    private val signInService: SignInService =SignInTestImpl()
    private val savedStateHandle= SavedStateHandle()
    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher= UnconfinedTestDispatcher()

    @Test
    fun loginError_invalidEmail(){
        val viewModel = SignInViewModel(savedStateHandle, signInService,testDispatcher)
        val invalidEmail = ":invalid:"
        viewModel.onEvent(SignInScreenEvent.TypeEmail(email = invalidEmail))
        viewModel.onEvent(SignInScreenEvent.SignIn(null))
        val result = viewModel.signInScreenState.value
        val expected = SignInScreenState(loginSuccess = false,
            isWrongEmailFormat = true,
            email = invalidEmail,
            errorMessage = "Incorrect email format")
        assertThat(result).isEqualTo(expected)
    }
    @Test
    fun loginSuccess(){
        val viewModel = SignInViewModel(savedStateHandle,signInService,testDispatcher)
        val validEmail = "email@email.com"
        viewModel.onEvent(SignInScreenEvent.TypeEmail(email = validEmail))
        viewModel.onEvent(SignInScreenEvent.SignIn(null))
        val result = viewModel.signInScreenState.value
        val expected = SignInScreenState(
            loginSuccess = true,
            email = validEmail
        )
        assertThat(result).isEqualTo(expected)
    }
}