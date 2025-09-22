package com.example.catapult.presentation.screens.quiz

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.catapult.domain.usecase.QuestionType
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight

@Composable
fun QuizScreen(
    onQuizFinished: () -> Unit,
    viewModel: QuizViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var showCancelDialog by remember { mutableStateOf(false) }

    BackHandler(enabled = !state.isFinished) {
        showCancelDialog = true
    }

    QuizContent(
        state = state,
        onAnswerSelected = { viewModel.setEvent(QuizContract.UiEvent.SubmitAnswer(it)) },
        onCancelClick = { showCancelDialog = true },
        onQuizFinished = onQuizFinished,
        onPublish = { viewModel.setEvent(QuizContract.UiEvent.PublishResult) },
        setEvent = { viewModel.setEvent(it) }
    )

    if (showCancelDialog) {
        CancelQuizDialog(
            onConfirm = {
                viewModel.setEvent(QuizContract.UiEvent.CancelQuiz)
                showCancelDialog = false
            },
            onDismiss = { showCancelDialog = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizContent(
    state: QuizContract.UiState,
    onAnswerSelected: (String) -> Unit,
    onCancelClick: () -> Unit,
    onQuizFinished: () -> Unit,
    onPublish: () -> Unit,
    setEvent: (QuizContract.UiEvent) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Kviz",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.Bold
                        )
                        , color = MaterialTheme.colorScheme.secondary
                    )
                },
                navigationIcon = {
                    if (!state.isFinished) {
                        IconButton(onClick = onCancelClick) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Prekini kviz")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(padding)) {

            when {
                state.isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                state.isFinished -> QuizFinishedContent(
                    score = state.score,
                    isPublishing = state.isPublishing,
                    onPublish = onPublish,
                    onQuizFinished = onQuizFinished,
                    setEvent = setEvent
                )
                else -> QuizQuestionContent(
                    state = state,
                    onAnswerSelected = onAnswerSelected,
                    onCancelClick = onCancelClick
                )
            }
        }
    }
}

@Composable
fun QuizFinishedContent(
    score: Float,
    isPublishing: Boolean,
    onPublish: () -> Unit,
    onQuizFinished: () -> Unit,
    setEvent: (QuizContract.UiEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Tvoj rezultat",
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = "${"%.2f".format(score)} poena",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        if (isPublishing) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = onPublish,
                shape = RoundedCornerShape(50),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Objavi na leaderboard")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = {
                setEvent(QuizContract.UiEvent.ResetQuiz)
                onQuizFinished()
            },
            shape = RoundedCornerShape(50),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Nazad")
        }

    }
}


@Composable
fun QuizQuestionContent(
    state: QuizContract.UiState,
    onAnswerSelected: (String) -> Unit,
    onCancelClick: () -> Unit
) {
    val currentQuestion = state.questions[state.currentIndex]

    AnimatedContent(
        targetState = currentQuestion,
        transitionSpec = {
            slideInHorizontally { width -> width } + fadeIn() togetherWith
                    slideOutHorizontally { width -> -width } + fadeOut()
        }
    ) { question ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Pitanje ${state.currentIndex + 1} / ${state.questions.size}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "⏱ ${formatTime(state.remainingTime)}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            question.photoUrl?.takeIf { it.isNotBlank() }?.let { url ->
                Image(
                    painter = rememberAsyncImagePainter(url),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .clip(RoundedCornerShape(16.dp))
                )
            }

            val questionText = when (question.type) {
                QuestionType.BREED -> "Koja je rasa ove mačke?"
                QuestionType.TEMPERAMENT -> "Koji temperament ne pripada ovoj mački?"
            }

            Text(
                text = questionText,
                style = MaterialTheme.typography.headlineSmall
            )

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                question.options.forEach { option ->
                    ElevatedButton(
                        onClick = { onAnswerSelected(option) },
                        shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                    ) {
                        Text(option)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = onCancelClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                shape = RoundedCornerShape(50)
            ) {
                Text("Prekini kviz")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}




@Composable
fun CancelQuizDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Prekini kviz") },
        text = { Text("Da li ste sigurni da želite da prekinete kviz? Rezultat neće biti sačuvan.") },
        confirmButton = {
            TextButton(onClick = onConfirm) { Text("Prekini") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Odustani") }
        }
    )
}

private fun formatTime(seconds: Int): String {
    val minutes = seconds / 60
    val secs = seconds % 60
    return String.format("%02d:%02d", minutes, secs)
}
