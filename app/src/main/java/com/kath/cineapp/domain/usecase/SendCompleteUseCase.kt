package com.kath.cineapp.domain.usecase

import com.kath.cineapp.domain.model.Complete
import com.kath.cineapp.domain.model.CompleteResult
import com.kath.cineapp.domain.repository.CompleteRepository

class SendCompleteUseCase(
    private val completeRepository: CompleteRepository,
) {
    suspend operator fun invoke(complete: Complete): Result<CompleteResult> {
        return completeRepository.sendComplete(complete).fold(
            onSuccess = {
                Result.success((it))
            },
            onFailure = {
                Result.failure(it)
            }
        )
    }
}