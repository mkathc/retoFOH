package com.kath.cineapp.ui.features.payment

import com.kath.cineapp.ui.features.payment.components.UiField

data class PaymentState(
    val formFields: CardFormFields = CardFormFields(),
    val isLoading: Boolean = false,
    val showDatePicker: Boolean = false
)

data class CardFormFields(
    val cardNumber: UiField = UiField("4907840000000005"),
    val cardHolder: UiField = UiField("Kath"),
    val documentType: UiField = UiField("DNI"),
    val documentNumber: UiField = UiField("47372313"),
    val cvvCode: UiField = UiField("777"),
    val expirationDate: UiField = UiField("2025/06"),
    val expirationDateTimestamp: Long? = null,
    val address: UiField = UiField("La Unidad 7670")
)