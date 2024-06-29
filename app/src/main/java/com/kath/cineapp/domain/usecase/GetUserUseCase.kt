package com.kath.cineapp.domain.usecase

import com.kath.cineapp.domain.model.User
import com.kath.cineapp.domain.repository.UserRepository
import com.kath.cineapp.ui.features.login.UserModel

class GetUserUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<UserModel> {
        return userRepository.getUser().fold(
            onSuccess = {
               Result.success(it.parse())
            },
            onFailure = {
                Result.failure(it)
            }
        )
    }

    private fun User.parse(): UserModel {
        return UserModel(
            name = name,
            email = email,
        )
    }
}