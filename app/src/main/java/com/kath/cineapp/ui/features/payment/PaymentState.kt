package com.kath.cineapp.ui.features.payment

data class PaymentState(
    val formFields: CardFormFields = CardFormFields(),
    val isLoading: Boolean = false,
    val showDatePicker: Boolean = false
)

data class CardFormFields(
    val cardNumber: UiField = UiField(""),
    val cardHolder: UiField = UiField(""),
    val documentType: UiField = UiField("DNI"),
    val documentNumber: UiField = UiField(""),
    val cvvCode: UiField = UiField(""),
    val expirationDate: UiField = UiField(""),
    val expirationDateTimestamp: Long? = null,
    val address: UiField = UiField("")
)