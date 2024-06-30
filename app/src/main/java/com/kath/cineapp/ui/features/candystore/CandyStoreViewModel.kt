package com.kath.cineapp.ui.features.candystore

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kath.cineapp.domain.usecase.GetCandyStoreUseCase
import com.kath.cineapp.domain.usecase.GetUserIsLoggedUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.launch

class CandyStoreViewModel(
    private val getCandyStoreUseCase: GetCandyStoreUseCase,
    private val getUserIsLoggedUseCase: GetUserIsLoggedUseCase
) : ViewModel() {

    // State is maintained using StateFlow
    private val _state: MutableStateFlow<StoreUiState> = MutableStateFlow(StoreUiState.IsNotLogged)
    val state: StateFlow<StoreUiState> = _state.asStateFlow()

    private val _productList: MutableStateFlow<MutableList<CandyStoreModel>> = MutableStateFlow(
        mutableListOf()
    )
    val productList: StateFlow<MutableList<CandyStoreModel>> = _productList.asStateFlow()

    private val _isLogged: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLogged: StateFlow<Boolean> = _isLogged.asStateFlow()

    // Function to get the current state
    fun currentState(): StoreUiState = _state.value

    // Function to update the state
    fun updateState(newState: StoreUiState) {
        _state.value = newState
    }

    init {
        isLoggedUser()
    }

    fun getCandyStore() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = getCandyStoreUseCase()
            if (result.isSuccess) {
                updateState(StoreUiState.Success(result.getOrNull() ?: mutableListOf()))
            } else {
                updateState(StoreUiState.Error)
            }
        }
    }

    fun addProduct(product: CandyStoreModel) {
        _productList.value.add(product)
    }

    fun sendToPayment() {
        var sum = 0.0
        productList.value.forEach {
            sum += it.getDoublePrice()
        }
        Log.e("Prices", sum.toString())
    }

    fun isLoggedUser() {
        viewModelScope.launch(Dispatchers.IO) {
            getUserIsLoggedUseCase().onSuccess {
                _isLogged.value = it
                if (it) {
                    updateState(StoreUiState.Loading)
                } else {
                    updateState(StoreUiState.IsNotLogged)
                }
            }.onFailure {
                _isLogged.value = false
                updateState(StoreUiState.IsNotLogged)
            }
        }
    }

}