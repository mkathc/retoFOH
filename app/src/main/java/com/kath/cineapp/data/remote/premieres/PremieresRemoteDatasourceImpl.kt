package com.kath.cineapp.data.remote.premieres

import com.kath.cineapp.data.remote.api.CineApi
import com.kath.cineapp.data.remote.model.PremieresResponse
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

class PremieresRemoteDatasourceImpl(
    private val api: CineApi,
) : PremieresRemoteDatasource {

    override suspend fun getPremieres(): Result<List<PremieresResponse>> {
        return try {
            val response = api.getPremieres()
            when {
                response.isSuccessful -> Result.success(response.body() ?: mutableListOf())
                else -> Result.failure(Throwable())
            }
        } catch (exception: Exception) {
            Result.failure(
                when (exception) {
                    is TimeoutException, is SocketTimeoutException, is ConnectException, is UnknownHostException -> Throwable()
                    else -> Throwable()
                }
            )
        }
    }
}