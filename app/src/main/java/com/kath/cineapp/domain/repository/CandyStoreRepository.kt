package com.kath.cineapp.domain.repository

import com.kath.cineapp.domain.model.CandyStore

interface CandyStoreRepository {
    suspend fun getCandyStore(): Result<List<CandyStore>>
}