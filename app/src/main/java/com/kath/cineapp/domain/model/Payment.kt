package com.kath.cineapp.domain.model

data class Payment(
    val cardNumber: String,
    val cardHolder: String,
    val documentType: String,
    val documentNumber: String,
    val cvvCode: String,
    val expirationDate: String,
    val expirationDateTimestamp: Long? = null,
    val email: String,
    val address: String,
)