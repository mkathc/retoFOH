package com.kath.cineapp.domain.repository

import com.kath.cineapp.domain.model.User

interface UserRepository {
    suspend fun saveUser(user: User): Result<Boolean>
    suspend fun getUser(): Result<User>
    suspend fun isLogged(): Result<Boolean>
}