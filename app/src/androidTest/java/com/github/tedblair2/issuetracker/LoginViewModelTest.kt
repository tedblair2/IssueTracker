package com.github.tedblair2.issuetracker

import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule

@HiltAndroidTest
class LoginViewModelTest {

    @get:Rule(order = 1)
    val hiltRule=HiltAndroidRule(this)
}