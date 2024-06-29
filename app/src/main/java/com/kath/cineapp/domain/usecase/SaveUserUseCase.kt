package com.kath.cineapp.domain.usecase

import com.kath.cineapp.domain.model.User
import com.kath.cineapp.domain.repository.UserRepository
import com.kath.cineapp.ui.features.login.UserModel

class SaveUserUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: UserModel): Result<Boolean> {
        return userRepository.saveUser(user.parse()).fold(
            onSuccess = {
               Result.success(it)
            },
            onFailure = {
                Result.failure(it)
            }
        )
    }

    private fun UserModel.parse(): User {
        return User(
            name = name,
            email = email,
        )
    }
}