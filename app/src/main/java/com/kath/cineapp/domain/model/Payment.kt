package com.kath.cineapp.domain.model

import java.util.UUID

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
    val value: Double
) {
    fun randomReference(): String {
        val myUuid = UUID.randomUUID()
        val myUuidAsString = myUuid.toString()
        return cardHolder + myUuidAsString
    }
}