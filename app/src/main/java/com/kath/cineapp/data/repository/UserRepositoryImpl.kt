package com.kath.cineapp.data.repository

import com.kath.cineapp.data.local.model.UserEntity
import com.kath.cineapp.data.local.user.UserLocalDatasource
import com.kath.cineapp.data.remote.candystore.CandyStoreRemoteDatasource
import com.kath.cineapp.data.remote.model.CandyStoreResponse
import com.kath.cineapp.domain.model.CandyStore
import com.kath.cineapp.domain.model.User
import com.kath.cineapp.domain.repository.CandyStoreRepository
import com.kath.cineapp.domain.repository.UserRepository

class UserRepositoryImpl(
    private val userLocalDatasource: UserLocalDatasource,
) : UserRepository {

    override suspend fun saveUser(user: User): Result<Boolean> {
        return userLocalDatasource.saveUser(user.parseToEntity())
            .fold(
                onSuccess = {
                    Result.success(it)
                },
                onFailure = {
                    Result.failure(it)
                }
            )
    }

    override suspend fun getUser(): Result<User> {
        return userLocalDatasource.getUser().fold(
            onSuccess = {
                Result.success(it.parseToUser())
            },
            onFailure = {
                Result.failure(it)
            }
        )
    }

    override suspend fun isLogged(): Result<Boolean> {
        return userLocalDatasource.isLogged()
    }

    private fun User.parseToEntity(): UserEntity {
        return UserEntity(
            name = name,
            email = email
        )
    }

    private fun UserEntity.parseToUser(): User {
        return User(
            name = name,
            email = email
        )
    }
}