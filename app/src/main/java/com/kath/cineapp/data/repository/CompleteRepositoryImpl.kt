package com.kath.cineapp.data.repository

import com.kath.cineapp.data.remote.complete.CompleteRemoteDatasource
import com.kath.cineapp.data.remote.model.CompleteBodyRequest
import com.kath.cineapp.data.remote.model.CompleteResponse
import com.kath.cineapp.domain.model.Complete
import com.kath.cineapp.domain.model.CompleteResult
import com.kath.cineapp.domain.repository.CompleteRepository

class CompleteRepositoryImpl(
    private val completeRemoteDatasource: CompleteRemoteDatasource,
) : CompleteRepository {
    override suspend fun sendComplete(complete: Complete): Result<CompleteResult> {
        return completeRemoteDatasource.sendComplete(complete.parseToRequest()).fold(
            onSuccess = { result ->
                Result.success(result.parseSuccess())
            },
            onFailure = {
                Result.failure(it)
            }
        )
    }

    private fun Complete.parseToRequest(): CompleteBodyRequest {
        return CompleteBodyRequest(
            name, mail, dni, operationDate
        )
    }

    private fun CompleteResponse.parseSuccess(): CompleteResult {
        return CompleteResult(
            resultCode = resultCode,
        )
    }
}