package com.kath.cineapp.data.remote.complete

import com.kath.cineapp.data.remote.api.CineApi
import com.kath.cineapp.data.remote.model.CompleteBodyRequest
import com.kath.cineapp.data.remote.model.CompleteResponse
import com.kath.cineapp.data.remote.model.PremieresResponse
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

class CompleteRemoteDatasourceImpl(
    private val api: CineApi,
) : CompleteRemoteDatasource {

    override suspend fun sendComplete(completeBodyRequest: CompleteBodyRequest): Result<CompleteResponse> {
        return try {
            val response = api.sendCompleteTransaction(completeBodyRequest)
            when {
                response.isSuccessful -> {
                    if (response.body()?.resultCode == 0) {
                        Result.success(response.body() ?: CompleteResponse(1))
                    } else {
                        Result.failure(Throwable())
                    }
                }

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