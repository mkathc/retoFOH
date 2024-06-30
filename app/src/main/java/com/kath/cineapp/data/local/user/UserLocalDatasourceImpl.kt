package com.kath.cineapp.data.local.user

import com.kath.cineapp.data.local.model.UserEntity
import com.kath.cineapp.data.local.preferences.ILocalPreferences

class UserLocalDatasourceImpl(
    private val localPreferences: ILocalPreferences
) : UserLocalDatasource {

    companion object {
        private const val KEY_NAME_USER = "com.kath.NAME_DATA_KEY"
        private const val KEY_MAIL_USER = "com.kath.MAIL_DATA_KEY"
        private const val KEY_LOGGED_USER = "com.kath.LOGGED_DATA_KEY"

    }

    override suspend fun getUser(): Result<UserEntity> {
        val name = localPreferences.getString(KEY_NAME_USER)
        val mail = localPreferences.getString(KEY_MAIL_USER)

        return if (name.isNotEmpty() && mail.isNotEmpty()) {
            Result.success(UserEntity(name = name, email = mail))
        } else {
            Result.failure(Throwable())
        }
    }

    override suspend fun saveUser(user: UserEntity): Result<Boolean> {
        try {
            localPreferences.setString(KEY_NAME_USER, user.name)
            localPreferences.setString(KEY_MAIL_USER, user.name)
            localPreferences.setBoolean(KEY_LOGGED_USER, true)
            return Result.success(true)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun isLogged(): Result<Boolean> {
        return try {
            val result = localPreferences.getBoolean(KEY_LOGGED_USER)
            Result.success(result)
        }catch (e:Exception){
            Result.failure(Throwable())
        }
    }
}