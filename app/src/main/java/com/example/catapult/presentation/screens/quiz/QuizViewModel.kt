package com.example.catapult.presentation.screens.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapult.data.local.database.UserPreferences
import com.example.catapult.data.remote.dto.QuizResultDto
import com.example.catapult.data.remote.dto.SubmitQuizResultDto
import com.example.catapult.data.repository.LeaderboardRepository
import com.example.catapult.domain.usecase.CalculateScoreUseCase
import com.example.catapult.domain.usecase.GenerateQuizUseCase
import com.example.catapult.presentation.screens.breedgallery.BreedGalleryContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val generateQuizUseCase: GenerateQuizUseCase,
    private val calculateScoreUseCase: CalculateScoreUseCase,
    private val userPreferences: UserPreferences,
    private val leaderboardRepository: LeaderboardRepository
) : ViewModel() {

    private val _state = MutableStateFlow(QuizContract.UiState())
    val state: StateFlow<QuizContract.UiState> = _state.asStateFlow()

    private val _effect = Channel<QuizContract.SideEffect>()
    val effect = _effect.receiveAsFlow()

    private val events = MutableSharedFlow<QuizContract.UiEvent>()

    private var timerJob: Job? = null

    init {
        observeEvents()
        setEvent(QuizContract.UiEvent.LoadQuestions)
    }

    fun setEvent(event: QuizContract.UiEvent) {
        viewModelScope.launch { events.emit(event) }
    }

    private fun observeEvents() {
        viewModelScope.launch {
            events.collect { event ->
                when (event) {
                    is QuizContract.UiEvent.LoadQuestions -> loadQuestions()
                    is QuizContract.UiEvent.SubmitAnswer -> submitAnswer(event.answer)
                    is QuizContract.UiEvent.TimeTick -> handleTimeTick()
                    is QuizContract.UiEvent.Timeout -> finishDueToTimeout()
                    is QuizContract.UiEvent.CancelQuiz -> cancelQuiz()
                    is QuizContract.UiEvent.PublishResult -> publishResult()
                    is QuizContract.UiEvent.ResetQuiz -> {
                        stopTimer()
                        _state.value = QuizContract.UiState()
                        setEvent(QuizContract.UiEvent.LoadQuestions)
                    }

                }
            }
        }
    }

    private fun loadQuestions() = viewModelScope.launch {
        _state.value = _state.value.copy(isLoading = true)
        try {
            val questions = generateQuizUseCase.generateQuestions()
                .filter { it.photoUrl.isNotBlank() }

            _state.value = _state.value.copy(
                questions = questions,
                currentIndex = 0,
                score = 0f,
                isLoading = false,
                isFinished = false,
                isCanceled = false,
                remainingTime = 300
            )

            startTimer()

        } catch (e: Exception) {
            _state.value = _state.value.copy(isLoading = false, errorMessage = e.message)
            _effect.send(QuizContract.SideEffect.ShowToast("Greška pri učitavanju pitanja"))
        }
    }

    private fun submitAnswer(answer: String) {
        val currentQuestion = _state.value.questions[_state.value.currentIndex]
        val isCorrect = currentQuestion.correctAnswer == answer
        val newScore = if (isCorrect) _state.value.score + 5 else _state.value.score

        if (_state.value.currentIndex + 1 >= _state.value.questions.size) {
            stopTimer()
            _state.value = _state.value.copy(score = newScore, isFinished = true)
        } else {
            _state.value = _state.value.copy(
                score = newScore,
                currentIndex = _state.value.currentIndex + 1
            )
        }
    }

    private fun handleTimeTick() {
        val newTime = _state.value.remainingTime - 1
        if (newTime <= 0) {
            setEvent(QuizContract.UiEvent.Timeout)
        } else {
            _state.value = _state.value.copy(remainingTime = newTime)
        }
    }

    private fun finishDueToTimeout() {
        stopTimer()
        _state.value = _state.value.copy(isFinished = true)
    }

    private fun cancelQuiz() {
        stopTimer()
        _state.value = _state.value.copy(
            isFinished = true,
            isCanceled = true,
            score = 0f
        )
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000L)
                setEvent(QuizContract.UiEvent.TimeTick)
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    override fun onCleared() {
        stopTimer()
        super.onCleared()
    }


    private fun publishResult() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isPublishing = true)
            try {
                val correctAnswers = (state.value.score / 5).toInt()
                val remainingTime = state.value.remainingTime

                val finalScore = calculateScoreUseCase.calculateScore(correctAnswers, remainingTime)

                val nickname = userPreferences.nickname.first() ?: ""

                val request = SubmitQuizResultDto(
                    nickname = nickname,
                    result = finalScore.toDouble(),
                    category = 1
                )

                leaderboardRepository.postResult(request)
                _effect.send(QuizContract.SideEffect.PublishSuccess)

            } catch (e: Exception) {
                _effect.send(QuizContract.SideEffect.ShowToast("Greška prilikom slanja rezultata"))
            } finally {
                _state.value = _state.value.copy(isPublishing = false)
            }
        }
    }






}
