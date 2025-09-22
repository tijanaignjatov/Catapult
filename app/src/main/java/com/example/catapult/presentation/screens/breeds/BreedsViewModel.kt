package com.example.catapult.presentation.screens.breeds

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapult.data.local.database.entities.BreedEntity
import com.example.catapult.data.repository.BreedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BreedsViewModel @Inject constructor(
    private val repository: BreedRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var fullList = emptyList<BreedEntity>()

    private val _state = MutableStateFlow(BreedsContract.UiState())
    val state = _state.asStateFlow()

    private val events = MutableSharedFlow<BreedsContract.UiEvent>()

    private val _effect = Channel<BreedsContract.SideEffect>()
    val effect = _effect.receiveAsFlow()

    private var currentQuery: String
        get() = savedStateHandle["query"] ?: ""
        set(value) { savedStateHandle["query"] = value }

    fun getSavedQuery() = currentQuery

    init {
        observeEvents()
    }

    fun setEvent(event: BreedsContract.UiEvent) {
        viewModelScope.launch { events.emit(event) }
    }

    private fun observeEvents() {
        viewModelScope.launch {
            events.collect { event ->
                when (event) {
                    is BreedsContract.UiEvent.LoadBreeds -> loadBreeds()
                    is BreedsContract.UiEvent.Search -> {
                        currentQuery = event.query
                        filterBreeds(currentQuery)
                    }
                }
            }
        }
    }

    private fun loadBreeds() = viewModelScope.launch {
        _state.value = _state.value.copy(isLoading = true, errorMessage = null)
        try {
            fullList = repository.observeAllBreeds().first()
            filterBreeds(currentQuery)
        } catch (e: Exception) {
            _state.value = _state.value.copy(isLoading = false, errorMessage = e.message)
        }
    }

    private fun filterBreeds(query: String) {
        val filtered = if (query.isBlank()) fullList else fullList.filter { it.name.startsWith(query, true) }
        _state.value = _state.value.copy(breeds = filtered, isLoading = false)
    }
}
