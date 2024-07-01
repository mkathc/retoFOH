package com.kath.cineapp.ui.features.payment.components

import java.text.SimpleDateFormat
import java.util.Locale

data class CardUi(
    val id: String,
    val cardNumber: String,
    val expiration: String,
    val recentBalance: String,
) {
    companion object {
        fun mock(): CardUi {
            val mockNumber = "2298126833989874"

            return CardUi(
                id = mockNumber,
                cardNumber = mockNumber.splitStringWithDivider(),
                expiration = "02/24",
                recentBalance = "\$2887.65")
        }
    }
}


fun Long.getFormattedDate(format: String): String {
    val formatter = SimpleDateFormat(format, Locale.getDefault())
    return formatter.format(this)
}

fun String.splitStringWithDivider(
    groupCharCount: Int = 4,
    divider: Char = ' '
): String {
    val formattedStringBuilder = StringBuilder()
    var count = 0

    for (char in this) {
        if (count > 0 && count % groupCharCount == 0) {
            formattedStringBuilder.append(divider)
        }
        formattedStringBuilder.append(char)
        count++
    }

    return formattedStringBuilder.toString()
}

data class UiField(
    val value: String,
    val error: String? = null,
)