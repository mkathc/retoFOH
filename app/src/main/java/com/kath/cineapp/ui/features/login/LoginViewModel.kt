package com.kath.cineapp.ui.features.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kath.cineapp.domain.usecase.GetUserIsLoggedUseCase
import com.kath.cineapp.domain.usecase.GetUserUseCase
import com.kath.cineapp.domain.usecase.SaveUserUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val saveUserUseCase: SaveUserUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val getUserIsLoggedUseCase: GetUserIsLoggedUseCase
) : ViewModel() {

    // State is maintained using StateFlow
    private val _state: MutableStateFlow<LoginUiState> = MutableStateFlow(LoginUiState.None)
    val state: StateFlow<LoginUiState> = _state.asStateFlow()

    private val _isLogged: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLogged: StateFlow<Boolean> = _isLogged.asStateFlow()

    // Function to get the current state
    fun currentState(): LoginUiState = _state.value

    // Function to update the state
    private fun updateState(newState: LoginUiState) {
        _state.value = newState
    }

    private fun updateUser(user: UserModel) {
        _user.value = user
    }

    fun currentUser(): UserModel = _user.value

    private val _user: MutableStateFlow<UserModel> = MutableStateFlow(UserModel("", ""))
    val user: StateFlow<UserModel> = _user.asStateFlow()

    fun saveUser(userModel: UserModel) {
        viewModelScope.launch(Dispatchers.IO) {
            saveUserUseCase(userModel).onSuccess {
                updateState(LoginUiState.Success)
            }.onFailure {
                updateState(LoginUiState.Error("Log in Failure"))
            }
        }
    }

    fun getUser() {
        viewModelScope.launch(Dispatchers.IO) {
            getUserUseCase().onSuccess {
                updateUser(it)
            }.onFailure {
                updateState(LoginUiState.Error("Get User Failure"))
            }
        }
    }

    fun isLoggedUser() {
        viewModelScope.launch(Dispatchers.IO) {
            getUserIsLoggedUseCase().onSuccess {
                _isLogged.value = it
                if (it) {
                    updateState(LoginUiState.Success)
                } else {
                    updateState(LoginUiState.None)
                }
            }.onFailure {
                _isLogged.value = false
                updateState(LoginUiState.None)
            }
        }
    }

}