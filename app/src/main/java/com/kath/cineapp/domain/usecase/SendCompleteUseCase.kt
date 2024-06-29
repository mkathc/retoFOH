package com.kath.cineapp.domain.usecase

import com.kath.cineapp.domain.model.Complete
import com.kath.cineapp.domain.model.CompleteResult
import com.kath.cineapp.domain.model.Payment
import com.kath.cineapp.domain.model.PaymentResult
import com.kath.cineapp.domain.model.User
import com.kath.cineapp.domain.repository.CompleteRepository
import com.kath.cineapp.domain.repository.PaymentRepository
import com.kath.cineapp.domain.repository.UserRepository
import com.kath.cineapp.ui.features.login.UserModel
import com.kath.cineapp.ui.features.payment.PaymentModel
import com.kath.cineapp.ui.features.payment.PaymentResultModel

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