package com.kath.cineapp.ui.features.payment

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.kath.cineapp.R
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.Locale
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class CardUi(
    val id: String,
    val isPrimary: Boolean,
    val cardNumber: String,
    val expiration: String,
    val recentBalance: String,
    val balanceFlow: Flow<String>,
    val addressFirstLine: String,
    val addressSecondLine: String?,
    val dateAdded: String,
    val cardType: UiText,
    val cardColor: Color,
    val cardNetwork: CardNetwork
) {
    companion object {
        fun mock(
            cardColor: Color = Color(0xFF100D40),
        ): CardUi {
            val mockNumber = "2298126833989874"

            return CardUi(
                id = mockNumber,
                cardNumber = mockNumber.splitStringWithDivider(),
                expiration = "02/24",
                recentBalance = "\$2887.65",
                balanceFlow = flowOf("\$2887.65"),
                addressFirstLine = "2890 Pangandaran Street",
                addressSecondLine = null,
                dateAdded = "12 Jan 2021 22:12",
                cardType = UiText.DynamicString("Debit"),
                cardColor = cardColor,
                isPrimary = true,
                cardNetwork = CardNetwork.MASTERCARD
            )
        }

        fun mapFromDomain(
            card: PaymentCard,
            balanceFlow: Flow<String>? = null
        ): CardUi {
            val date = card.expiration.getFormattedDate("MM/yy")
            val recentBalance = MoneyAmountUi.mapFromDomain(card.recentBalance).amountStr

            return CardUi(
                id = card.cardId,
                cardNumber = card.cardNumber.splitStringWithDivider(),
                isPrimary = card.isPrimary,
                expiration = date,
                recentBalance = recentBalance,
                balanceFlow = balanceFlow ?: flowOf(recentBalance),
                addressFirstLine = card.addressFirstLine,
                addressSecondLine = card.addressSecondLine.ifBlank { null },
                dateAdded = card.addedDate.getFormattedDate("dd MMM yyyy HH:mm"),
                cardType = when (card.cardType) {
                    CardType.DEBIT -> UiText.StringResource(R.string.debit)
                    CardType.CREDIT -> UiText.StringResource(R.string.credit)
                },
                cardColor = when (card.cardType) {
                    CardType.DEBIT -> {
                        Color(0xFF100D40)
                    }

                    CardType.CREDIT -> {
                        Color(0xFF262627)
                    }
                },
                cardNetwork = CardUiHelpers.detectCardNetwork(card.cardNumber)
            )
        }
    }
}

data class MoneyAmountUi(
    val amountStr: String,
) {
    companion object {
        fun mapFromDomain(balance: MoneyAmount): MoneyAmountUi {
            val symbols = DecimalFormatSymbols(Locale.US)
            val decimalFormat = DecimalFormat("#,##0.##", symbols)
            decimalFormat.isGroupingUsed = false
            val formattedValue = decimalFormat.format(balance.value)


            // Add currency prefixes
            return when (balance.currency) {
                BalanceCurrency.USD -> {
                    MoneyAmountUi("$$formattedValue")
                }
            }
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

fun String.maskCardId(visibleCharacters: Int = 4): String {
    return "*" + substring(length - visibleCharacters)
}

sealed class UiText {
    data class DynamicString(val value: String) : UiText()
    class StringResource(
        @StringRes val resId: Int,
        vararg val args: Any
    ) : UiText()

    @Composable
    fun asString(): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> stringResource(resId, *args)
        }
    }

    fun asString(context: Context): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> context.getString(resId, *args)
        }
    }
}

data class PaymentCard(
    val cardId: String,
    val isPrimary: Boolean,
    val cardNumber: String,
    val cardType: CardType,
    val cardHolder: String,
    val expiration: Long,
    val recentBalance: MoneyAmount,
    val addressFirstLine: String,
    val addressSecondLine: String,
    val addedDate: Long
)

enum class CardType {
    DEBIT,
    CREDIT
}

data class MoneyAmount(
    val value: Float,
    val currency: BalanceCurrency = BalanceCurrency.USD,
) {
    operator fun plus(other: MoneyAmount): MoneyAmount {
        return this.copy(value = this.value + other.value)
    }

    operator fun minus(other: MoneyAmount): MoneyAmount {
        return this.copy(value = this.value - other.value)
    }

    operator fun compareTo(other: MoneyAmount): Int {
        return this.value.compareTo(other.value)
    }
}

enum class BalanceCurrency {
    USD
}