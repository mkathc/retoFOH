package com.kath.cineapp.domain.repository

import com.kath.cineapp.domain.model.Complete
import com.kath.cineapp.domain.model.CompleteResult
import com.kath.cineapp.domain.model.Premieres

interface CompleteRepository {
    suspend fun sendComplete(complete: Complete): Result<CompleteResult>
}