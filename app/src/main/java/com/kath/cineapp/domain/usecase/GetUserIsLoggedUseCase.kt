package com.kath.cineapp.domain.usecase

import com.kath.cineapp.domain.repository.UserRepository

class GetUserIsLoggedUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<Boolean> {
        return userRepository.isLogged().fold(
            onSuccess = {
               Result.success(it)
            },
            onFailure = {
                Result.failure(it)
            }
        )
    }
}