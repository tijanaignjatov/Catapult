package com.example.catapult.presentation.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catapult.data.local.database.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _state = MutableStateFlow(LoginContract.UiState())
    val state = _state.asStateFlow()

    private val _effect = Channel<LoginContract.SideEffect>()
    val effect = _effect.receiveAsFlow()

    fun setEvent(event: LoginContract.UiEvent) {
        when (event) {
            is LoginContract.UiEvent.EnterName -> _state.update { it.copy(name = event.name) }
            is LoginContract.UiEvent.EnterNickname -> _state.update { it.copy(nickname = event.nickname) }
            is LoginContract.UiEvent.EnterEmail -> _state.update { it.copy(email = event.email) }
            is LoginContract.UiEvent.SaveUser -> saveUser()
        }
    }

    private fun saveUser() {
        viewModelScope.launch {
            val name = state.value.name.trim()
            val nickname = state.value.nickname.trim()
            val email = state.value.email.trim()

            val nicknamePattern = Regex("^[a-zA-Z0-9_]+\$")
            if (!nickname.matches(nicknamePattern)) {
                _effect.send(LoginContract.SideEffect.ShowToast("Nickname može sadržati samo slova, brojeve i _"))
                return@launch
            }

            val emailPattern = Regex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})")
            if (!email.matches(emailPattern)) {
                _effect.send(LoginContract.SideEffect.ShowToast("Unesite validan email"))
                return@launch
            }

            if (name.isEmpty()) {
                _effect.send(LoginContract.SideEffect.ShowToast("Ime je obavezno"))
                return@launch
            }

            userPreferences.saveUser(name, nickname, email)
            _effect.send(LoginContract.SideEffect.LoginSuccess)
        }
    }

}
