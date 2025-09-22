package com.example.catapult.presentation.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapult.data.local.database.UserPreferences
import com.example.catapult.data.repository.LeaderboardRepository
import com.example.catapult.data.repository.QuizRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
    private val quizRepository: QuizRepository,
    private val leaderboardRepository: LeaderboardRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileContract.UiState())
    val state: StateFlow<ProfileContract.UiState> = _state

    private val _effect = Channel<ProfileContract.SideEffect>()
    val effect = _effect.receiveAsFlow()

    private val events = MutableSharedFlow<ProfileContract.UiEvent>()

    init {
        observeEvents()
        setEvent(ProfileContract.UiEvent.LoadUser)
    }

    fun setEvent(event: ProfileContract.UiEvent) {
        viewModelScope.launch { events.emit(event) }
    }

    private fun observeEvents() {
        viewModelScope.launch {
            events.collect { event ->
                when (event) {
                    is ProfileContract.UiEvent.LoadUser -> loadUserData()
                    is ProfileContract.UiEvent.Logout -> logout()
                }
            }
        }
    }

    private fun loadUserData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val name = userPreferences.name.first()
            val nickname = userPreferences.nickname.first()
            val email = userPreferences.email.first()

            val results = quizRepository.getAllResults()

            val bestScore = results.maxOfOrNull { it.result } ?: 0f

            val leaderboard = leaderboardRepository.getLeaderboard()

            val position = leaderboard.indexOfFirst { it.nickname == nickname }
            val leaderboardPosition = if (position >= 0) position + 1 else null

            _state.value = ProfileContract.UiState(
                name = name,
                nickname = nickname,
                email = email,
                isLoading = false,
                results = results,
                bestScore = bestScore,
                leaderboardPosition = leaderboardPosition
            )
        }
    }

    private fun logout() {
        viewModelScope.launch {
            userPreferences.clearUser()
            _effect.send(ProfileContract.SideEffect.LogoutSuccess)
        }
    }
}
