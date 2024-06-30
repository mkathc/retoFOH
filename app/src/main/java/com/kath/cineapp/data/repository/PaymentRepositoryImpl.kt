package com.kath.cineapp.data.repository

import com.kath.cineapp.data.remote.model.PaymentResponse
import com.kath.cineapp.data.remote.payu.PayURemoteDatasource
import com.kath.cineapp.domain.model.Payment
import com.kath.cineapp.domain.model.PaymentResult
import com.kath.cineapp.domain.repository.PaymentRepository

class PaymentRepositoryImpl(
    private val payURemoteDatasource: PayURemoteDatasource,
) : PaymentRepository {

    override suspend fun sendPayment(payment: Payment): Result<PaymentResult> {
        return payURemoteDatasource.sendPayment(payment)
            .fold(
                onSuccess = {
                    Result.success(it.parseToResult())
                },
                onFailure = {
                    Result.failure(it)
                }
            )
    }

    private fun PaymentResponse.parseToResult(): PaymentResult {
        return PaymentResult(
            operationDate = transactionResponse?.operationDate.toString()
        )
    }
}