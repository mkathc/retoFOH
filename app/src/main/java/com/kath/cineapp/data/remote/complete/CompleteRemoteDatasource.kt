package com.kath.cineapp.data.remote.complete

import com.kath.cineapp.data.remote.model.CompleteBodyRequest
import com.kath.cineapp.data.remote.model.CompleteResponse

interface CompleteRemoteDatasource {

    suspend fun sendComplete(completeBodyRequest: CompleteBodyRequest): Result<CompleteResponse>

}
