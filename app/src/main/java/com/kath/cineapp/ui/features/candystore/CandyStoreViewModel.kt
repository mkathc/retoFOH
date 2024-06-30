package com.kath.cineapp.ui.features.candystore

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kath.cineapp.domain.usecase.GetCandyStoreUseCase
import com.kath.cineapp.domain.usecase.GetUserIsLoggedUseCase
import com.kath.cineapp.ui.features.home.HomeUiState
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

    private val _isLogged: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLogged: StateFlow<Boolean> = _isLogged.asStateFlow()

    private val _addProduct: MutableStateFlow<AddProductUiState> = MutableStateFlow(AddProductUiState())
    val addProduct: StateFlow<AddProductUiState> get() = _addProduct

    private var resultList: List<CandyStoreModel> = mutableListOf()

    // Function to update the state
    fun updateState(newState: StoreUiState) {
        _state.value = newState
    }

    fun currentProductsState() = _addProduct.value


    init {
        isLoggedUser()
    }

    fun getCandyStore() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = getCandyStoreUseCase()
            if (result.isSuccess) {
                resultList = result.getOrNull()?: mutableListOf()
                updateState(StoreUiState.Success(resultList))
            } else {
                updateState(StoreUiState.Error)
            }
        }
    }

    fun addProduct(product: CandyStoreModel) {
        updateState(StoreUiState.Loading)
        _addProduct.value.addProduct(product)
        updateState(StoreUiState.Success(resultList))
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