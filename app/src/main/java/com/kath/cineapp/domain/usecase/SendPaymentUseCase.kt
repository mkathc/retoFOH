package com.kath.cineapp.domain.usecase

import com.kath.cineapp.domain.model.Payment
import com.kath.cineapp.domain.model.PaymentResult
import com.kath.cineapp.domain.repository.PaymentRepository
import com.kath.cineapp.ui.features.payment.model.PaymentModel
import com.kath.cineapp.ui.features.payment.model.PaymentResultModel

class SendPaymentUseCase(
    private val paymentRepository: PaymentRepository,
) {
    suspend operator fun invoke(paymentModel: PaymentModel): Result<PaymentResultModel> {
        return paymentRepository.sendPayment(paymentModel.parseToPayment()).fold(
            onSuccess = {
                Result.success((it.parseToResult()))
            },
            onFailure = {
                Result.failure(it)
            }
        )
    }

    private fun PaymentModel.parseToPayment(): Payment {
        return Payment(
            cardNumber,
            cardHolder,
            documentType,
            documentNumber,
            cvvCode,
            expirationDate,
            expirationDateTimestamp,
            email,
            address,
            value
        )
    }

    private fun PaymentResult.parseToResult(): PaymentResultModel {
        return PaymentResultModel(
            operationDate = operationDate
        )
    }
}