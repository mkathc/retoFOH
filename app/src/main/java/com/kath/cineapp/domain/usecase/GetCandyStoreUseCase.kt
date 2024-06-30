package com.kath.cineapp.domain.usecase

import com.kath.cineapp.domain.model.CandyStore
import com.kath.cineapp.domain.repository.CandyStoreRepository
import com.kath.cineapp.ui.features.candystore.CandyStoreModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class GetCandyStoreUseCase(
    private val candyStoreRepository: CandyStoreRepository
) {
    suspend operator fun invoke(): Result<List<CandyStoreModel>> {
        return candyStoreRepository.getCandyStore().fold(
            onSuccess = { list ->
               Result.success( list.map { it.parseSuccess() })
            },
            onFailure = {
                Result.failure(it)
            }
        )
    }

    private fun CandyStore.parseSuccess(): CandyStoreModel {
        return CandyStoreModel(
            name = name,
            description = description,
            price = price,
            image = image,
            count = 0
        )
    }
}