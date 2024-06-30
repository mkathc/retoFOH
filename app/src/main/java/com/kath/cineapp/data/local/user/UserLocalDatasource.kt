package com.kath.cineapp.data.local.user

import com.kath.cineapp.data.local.model.UserEntity
import com.kath.cineapp.data.remote.model.PremieresResponse

interface UserLocalDatasource {

    suspend fun getUser(): Result<UserEntity>
    suspend fun saveUser(user:UserEntity): Result<Boolean>
    suspend fun isLogged(): Result<Boolean>

}
