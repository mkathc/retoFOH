package com.kath.cineapp.data.remote.payu

import com.kath.cineapp.data.remote.model.PaymentResponse
import com.kath.cineapp.domain.model.Payment

interface PayURemoteDatasource {

    suspend fun sendPayment(payment: Payment): Result<PaymentResponse>

}
