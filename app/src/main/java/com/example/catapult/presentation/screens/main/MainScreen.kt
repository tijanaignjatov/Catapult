package com.example.catapult.presentation.screens.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.catapult.presentation.screens.breeds.BreedsScreen
import com.example.catapult.presentation.screens.leaderboard.LeaderboardScreen
import com.example.catapult.presentation.screens.profile.ProfileScreen
import com.example.catapult.presentation.screens.quiz.QuizScreen

@Composable
fun MainScreen(navController: NavHostController) {
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.List, contentDescription = "Rase") },
                    label = { Text("Rase") },
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Quiz, contentDescription = "Kviz") },
                    label = { Text("Kviz") },
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Leaderboard, contentDescription = "Leaderboard") },
                    label = { Text("Leaderboard") },
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profil") },
                    label = { Text("Profil") },
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 }
                )
            }
        }
    ) { paddingValues ->
        MainScreenContent(
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it },
            navController = navController,
            paddingValues = paddingValues
        )
    }
}

@Composable
fun MainScreenContent(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    navController: NavHostController,
    paddingValues: androidx.compose.foundation.layout.PaddingValues
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        when (selectedTab) {
            0 -> BreedsScreen(
                onBreedClick = { breedId ->
                    navController.navigate("breedDetails/$breedId")
                }
            )
            1 -> QuizScreen(onQuizFinished = { onTabSelected(2) })
            2 -> LeaderboardScreen(onBackToHome = { onTabSelected(0) })
            3 -> ProfileScreen(onLogout = {
                navController.navigate("login") {
                    popUpTo(0) { inclusive = true }
                }
            })
        }
    }
}
