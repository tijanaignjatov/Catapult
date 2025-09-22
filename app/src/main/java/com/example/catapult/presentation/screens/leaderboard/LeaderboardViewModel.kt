package com.example.catapult.presentation.screens.leaderboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapult.data.repository.LeaderboardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    private val repository: LeaderboardRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LeaderboardContract.UiState())
    val state: StateFlow<LeaderboardContract.UiState> = _state

    private val _effect = Channel<LeaderboardContract.SideEffect>()
    val effect = _effect.receiveAsFlow()

    private val events = MutableSharedFlow<LeaderboardContract.UiEvent>()

    init {
        observeEvents()
        setEvent(LeaderboardContract.UiEvent.LoadLeaderboard)
    }

    fun setEvent(event: LeaderboardContract.UiEvent) {
        viewModelScope.launch { events.emit(event) }
    }

    private fun observeEvents() {
        viewModelScope.launch {
            events.collect { event ->
                when (event) {
                    LeaderboardContract.UiEvent.LoadLeaderboard -> loadLeaderboard()
                }
            }
        }
    }

    private fun loadLeaderboard() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val results = repository.getLeaderboard()
                _state.update { it.copy(results = results, isLoading = false) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, errorMessage = e.message) }
                _effect.send(LeaderboardContract.SideEffect.ShowToast("Failed to load leaderboard"))
            }
        }
    }
}
