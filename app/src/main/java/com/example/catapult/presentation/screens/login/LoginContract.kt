package com.example.catapult.presentation.screens.login

object LoginContract {

    data class UiState(
        val name: String = "",
        val nickname: String = "",
        val email: String = "",
        val isLoading: Boolean = false
    )

    sealed class UiEvent {
        data class EnterName(val name: String) : UiEvent()
        data class EnterNickname(val nickname: String) : UiEvent()
        data class EnterEmail(val email: String) : UiEvent()
        object SaveUser : UiEvent()
    }

    sealed class SideEffect {
        data class ShowToast(val message: String) : SideEffect()
        object LoginSuccess : SideEffect()
    }
}
