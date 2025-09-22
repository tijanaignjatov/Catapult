package com.example.catapult.presentation.screens.profile

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.catapult.data.local.database.entities.QuizResultEntity




@Composable
fun ProfileScreen(
    onLogout: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    val context = LocalContext.current


    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ProfileContract.SideEffect.ShowToast -> { Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show() }
                is ProfileContract.SideEffect.LogoutSuccess -> onLogout()
            }
        }
    }

    ProfileContent(
        state = state,
        onLogout = { viewModel.setEvent(ProfileContract.UiEvent.Logout) }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileContent(
    state: ProfileContract.UiState,
    onLogout: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Profil",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.secondary
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            AnimatedVisibility(
                visible = state.isLoading,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                CircularProgressIndicator()
            }

            AnimatedVisibility(
                visible = !state.isLoading,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                ProfileBodyContent(state = state, onLogout = onLogout)
            }
        }
    }
}


@Composable
fun ProfileBodyContent(
    state: ProfileContract.UiState,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        Text("Ime: ${state.name.orEmpty()}", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(4.dp))
        Text("Nadimak: ${state.nickname.orEmpty()}", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(4.dp))
        Text("Email: ${state.email.orEmpty()}", style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(16.dp))

        Text("Najbolji rezultat:", style = MaterialTheme.typography.titleMedium)
        Text("${"%.2f".format(state.bestScore)}", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(8.dp))

        Text("Pozicija na leaderboard-u:", style = MaterialTheme.typography.titleMedium)
        Text("${state.leaderboardPosition ?: "N/A"}", style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(24.dp))

        Text("Istorija rezultata:", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 300.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(state.results) { result ->
                ResultItem(result)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onLogout,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .height(48.dp),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text("Logout")
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}


@Composable
fun ResultItem(result: QuizResultEntity) {
    ElevatedCard(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.elevatedCardElevation(2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Rezultat: ${"%.2f".format(result.result)}", style = MaterialTheme.typography.bodyLarge)
            Text("Datum: ${formatTimestamp(result.createdAt)}", style = MaterialTheme.typography.bodyMedium)
            Text("Objavljen: ${if (result.published) "Da" else "Ne"}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}


fun formatTimestamp(timestamp: Long): String {
    return java.text.SimpleDateFormat("dd.MM.yyyy HH:mm").format(java.util.Date(timestamp))
}
