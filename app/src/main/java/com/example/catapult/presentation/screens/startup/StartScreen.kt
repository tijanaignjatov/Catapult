package com.example.catapult.presentation.screens.startup

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun StartupScreen(
    viewModel: StartViewModel = hiltViewModel(),
    onUserExists: () -> Unit,
    onUserNotFound: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.checkUser { exists ->
            if (exists) onUserExists() else onUserNotFound()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}
