package com.kath.cineapp.ui.features.candystore

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kath.cineapp.domain.usecase.GetCandyStoreUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.launch

class CandyStoreViewModel(
    private val getCandyStoreUseCase: GetCandyStoreUseCase
) : ViewModel() {

    // State is maintained using StateFlow
    private val _state: MutableStateFlow<StoreUiState> = MutableStateFlow(StoreUiState.Loading)
    val state: StateFlow<StoreUiState> = _state.asStateFlow()

    private val _productList: MutableStateFlow<MutableList<CandyStoreModel>> = MutableStateFlow(
        mutableListOf())
    val productList: StateFlow<MutableList<CandyStoreModel>> = _productList.asStateFlow()

    // Function to get the current state
    fun currentState(): StoreUiState = _state.value

    // Function to update the state
    private fun updateState(newState: StoreUiState) {
        _state.value = newState
    }

    init {
        getCandyStore()
    }

    private fun getCandyStore() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = getCandyStoreUseCase()
            if (result.isSuccess){
                updateState(StoreUiState.Success(result.getOrNull() ?: mutableListOf()))
            }else{
                updateState(StoreUiState.Error)
            }
        }
    }

    fun addProduct(product: CandyStoreModel){
        _productList.value.add(product)
    }

    fun sendToPayment(){
        var sum = 0.0
        productList.value.forEach {
            sum += it.getDoublePrice()
        }
        Log.e("Prices", sum.toString() )
    }

}