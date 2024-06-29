package com.kath.cineapp.data.remote.payu

import com.kath.cineapp.data.remote.api.CineApi
import com.kath.cineapp.data.remote.model.PayURequest
import com.kath.cineapp.data.remote.model.PaymentResponse
import com.kath.cineapp.domain.model.Payment

class PayURemoteDatasourceImpl(
    private val api: CineApi,
) : PayURemoteDatasource {

    override suspend fun sendPayment(payment: Payment): Result<PaymentResponse> {

        val response = api.sendPaymentToPayU(payment.toRequest())

        return if (response.isSuccessful) {
            val body = response.body()
            Result.success(body ?: PaymentResponse(code = "", error = "", transactionResponse = null))
        } else {
            Result.failure(Throwable())
        }

        /*return try {


            when {
                response.isSuccessful -> {
                    val body = response.body()
                    Result.success(body ?: PayUResponse(code = ""))
                }
                else -> Result.failure(Throwable())
            }
        } catch (exception: Exception) {
            Result.failure(
                when (exception) {
                    is TimeoutException, is SocketTimeoutException, is ConnectException, is UnknownHostException -> Throwable()
                    else -> Throwable()
                }
            )
        }*/
    }

    private fun Payment.toRequest(): PayURequest {
        return PayURequest()
    }
}