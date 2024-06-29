package com.kath.cineapp.ui.features.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kath.cineapp.domain.usecase.GetPremieresUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getPremieresUseCase: GetPremieresUseCase
) : ViewModel() {

    // State is maintained using StateFlow
    private val _state: MutableStateFlow<HomeUiState> = MutableStateFlow(HomeUiState.Loading)
    val state: StateFlow<HomeUiState> = _state.asStateFlow()

    // Function to get the current state
    fun currentState(): HomeUiState = _state.value

    // Function to update the state
    private fun updateState(newState: HomeUiState) {
        _state.value = newState
    }

    init {
        getPremieres()
    }

    private fun getPremieres() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = getPremieresUseCase()
            if (result.isSuccess){
                Log.e("Result", "get premiere success")
                updateState(HomeUiState.Success(result.getOrNull() ?: mutableListOf()))
                Log.e("Result", result.toString())

            }else{
                Log.e("Result", "get premiere error")
                updateState(HomeUiState.Error)
            }
        }
    }

}