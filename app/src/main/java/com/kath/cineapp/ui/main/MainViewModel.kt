package com.kath.cineapp.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kath.cineapp.domain.model.User
import com.kath.cineapp.domain.usecase.GetUserUseCase
import com.kath.cineapp.domain.usecase.SaveUserUseCase
import com.kath.cineapp.ui.features.login.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val getUserUseCase: GetUserUseCase
) : ViewModel() {

    private fun updateUser(user: UserModel) {
        _user.value = user
    }

    fun currentUser(): UserModel = _user.value

    private val _user: MutableStateFlow<UserModel> = MutableStateFlow(UserModel("", ""))
    val user: StateFlow<UserModel> = _user.asStateFlow()

    init {
        getUser()
    }
    fun getUser() {
        viewModelScope.launch(Dispatchers.IO) {
            getUserUseCase().onSuccess {
                Log.e("Debug", it.email)
                updateUser(it)
            }.onFailure {
                updateUser(UserModel("", ""))
            }
        }
    }

}