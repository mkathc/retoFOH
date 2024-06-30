package com.kath.cineapp.ui.features.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
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
    val state: StateFlow<HomeUiState> get() = _state

    // Function to update the state
    private fun updateState(newState: HomeUiState) {
        Log.e("Debug", "updateState")
        _state.value = newState
    }

    fun getPremieres() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = getPremieresUseCase()
            if (result.isSuccess){
                Log.e("Debug", "get premiere success")
                updateState(HomeUiState.Success(result.getOrNull() ?: mutableListOf()))
            }else{
                updateState(HomeUiState.Error)
            }
        }
    }
}