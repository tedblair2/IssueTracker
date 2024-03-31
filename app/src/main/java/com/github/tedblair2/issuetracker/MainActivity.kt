package com.github.tedblair2.issuetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.github.tedblair2.issuetracker.features.MainApp
import com.github.tedblair2.issuetracker.ui.theme.IssueTrackerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IssueTrackerTheme {
                MainApp()
            }
        }
    }
}