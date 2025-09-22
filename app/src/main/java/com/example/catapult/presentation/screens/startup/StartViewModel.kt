package com.example.catapult.presentation.screens.startup


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapult.data.local.database.UserPreferences
import com.example.catapult.data.repository.BreedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
    private val breedRepository: BreedRepository
) : ViewModel() {


    fun checkUser(onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            breedRepository.synchronizeBreedsIfNeeded()

            val name = userPreferences.name.first()
            val userExists = !name.isNullOrBlank()
            onResult(userExists)
        }
    }


}

