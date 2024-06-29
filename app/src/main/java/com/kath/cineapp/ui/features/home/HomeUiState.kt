package com.kath.cineapp.ui.features.home

sealed class HomeUiState{
    data object Loading: HomeUiState()
    data class Success(val premieresList: List<PremieresModel>): HomeUiState()
    data object Error: HomeUiState()
}