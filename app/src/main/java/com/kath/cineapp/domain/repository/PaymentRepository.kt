package com.kath.cineapp.domain.repository

import com.kath.cineapp.domain.model.Payment
import com.kath.cineapp.domain.model.PaymentResult

interface PaymentRepository {
    suspend fun sendPayment(payment: Payment): Result<PaymentResult>
}