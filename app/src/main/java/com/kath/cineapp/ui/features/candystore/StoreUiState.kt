package com.kath.cineapp.ui.features.candystore

sealed class StoreUiState{
    data object Loading: StoreUiState()
    data class Success(val storeList: List<CandyStoreModel>): StoreUiState()
    data object Error: StoreUiState()
    data object IsNotLogged: StoreUiState()

}