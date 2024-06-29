package com.kath.cineapp.data.repository

import com.kath.cineapp.data.remote.candystore.CandyStoreRemoteDatasource
import com.kath.cineapp.data.remote.model.CandyStoreResponse
import com.kath.cineapp.domain.model.CandyStore
import com.kath.cineapp.domain.repository.CandyStoreRepository

class CandyStoreRepositoryImpl(
    private val candyStoreRemoteDatasource: CandyStoreRemoteDatasource,
) : CandyStoreRepository {
    override suspend fun getCandyStore(): Result<List<CandyStore>> {

        return candyStoreRemoteDatasource.getCandyStore().fold(
            onSuccess = { list ->
                Result.success(list.map { premieres -> premieres.parseSuccess() })
            },
            onFailure = {
                Result.failure(it)
            }
        )

    }

    private fun CandyStoreResponse.parseSuccess(): CandyStore {
        return CandyStore(
            name = name,
            description = description,
            price = price,
            image = image
        )
    }
}