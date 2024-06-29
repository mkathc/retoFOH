package com.kath.cineapp.ui.features.login

sealed class LoginUiState{
    data object None: LoginUiState()
    data object Success: LoginUiState()
    data class Error(var message: String): LoginUiState()
}