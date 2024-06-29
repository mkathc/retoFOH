package com.kath.cineapp.data.remote.model

data class PaymentResponse(
    val code: String?,
    val error: String?,
    val transactionResponse: TransactionResponse?
)

data class TransactionResponse(
    val orderId: String?,
    val transactionId: String?,
    val state: String?,
    val paymentNetworkResponseCode: String?,
    val paymentNetworkResponseErrorMessage: String?,
    val trazabilityCode: String?,
    val authorizationCode: String?,
    val pendingReason: String?,
    val responseCode: String,
    val errorCode: String?,
    val responseMessage: String?,
    val transactionDate: String?,
    val transactionTime: String?,
    val operationDate: String?,
    val referenceQuestionnaire: String?,
    val additionalInfo: String?
)
