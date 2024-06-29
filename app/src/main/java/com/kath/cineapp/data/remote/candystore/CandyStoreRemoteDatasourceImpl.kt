package com.kath.cineapp.data.remote.candystore

import com.kath.cineapp.data.remote.api.CineApi
import com.kath.cineapp.data.remote.model.CandyStoreResponse
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

class CandyStoreRemoteDatasourceImpl(
    private val api: CineApi,
) : CandyStoreRemoteDatasource {

    override suspend fun getCandyStore(): Result<List<CandyStoreResponse>> {

        return try {
            val response = api.getCandyStore()

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