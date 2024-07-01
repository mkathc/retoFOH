package com.kath.cineapp.ui.features.payment


sealed class ResultUiState{
    data object None: ResultUiState()
    data object Success: ResultUiState()
    data object Error: ResultUiState()
}
