package com.kath.cineapp.data.remote.payu

import android.util.Log
import com.kath.cineapp.data.remote.api.CineApi
import com.kath.cineapp.data.remote.model.AdditionalValue
import com.kath.cineapp.data.remote.model.CreditCard
import com.kath.cineapp.data.remote.model.Order
import com.kath.cineapp.data.remote.model.PayURequest
import com.kath.cineapp.data.remote.model.PaymentResponse
import com.kath.cineapp.data.remote.model.Transaction
import com.kath.cineapp.data.remote.model.TxValues
import com.kath.cineapp.domain.model.Payment
import java.math.BigInteger
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.security.MessageDigest
import java.util.concurrent.TimeoutException

class PayURemoteDatasourceImpl(
    private val api: CineApi,
) : PayURemoteDatasource {

    override suspend fun sendPayment(payment: Payment): Result<PaymentResponse> {

        return try {
            val response = api.sendPaymentToPayU(payment.toRequest())

            when {
                response.isSuccessful -> {
                    val body = response.body()
                    if (body?.transactionResponse?.state == "APPROVED") {
                        Result.success(body)
                    } else {
                        Result.failure(Throwable())
                    }
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
        }
    }

    private fun Payment.toRequest(): PayURequest {
        val referenceCode = randomReference()
        return PayURequest(
            transaction = Transaction().copy(
                creditCard = CreditCard(
                    number = cardNumber,
                    securityCode = cvvCode,
                    expirationDate = expirationDate
                ),
                order = Order().copy(
                    additionalValues = AdditionalValue(TxValues(value = value)),
                    signature = getSignature(referenceCode = referenceCode, value),
                    referenceCode = referenceCode
                )
            )
        )
    }

    private fun getSignature(referenceCode: String, value: Double): String {
        val input = "4Vj8eK4rloUd272L48hsrarnUA~508029~$referenceCode~$value~PEN"
        val md = MessageDigest.getInstance("MD5")
        val result = BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
        Log.e("Debug", "input $input")
        return result
    }

}