package com.kath.cineapp.ui.features.payment

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kath.cineapp.ui.features.payment.components.CardNumberField
import com.kath.cineapp.ui.features.payment.components.DatePickerDialog
import com.kath.cineapp.ui.features.payment.components.FormField
import com.kath.cineapp.ui.features.payment.components.FullscreenProgressBar
import com.kath.cineapp.ui.features.payment.components.ReadOnlyTextField
import com.kath.cineapp.ui.features.payment.components.UiField
import com.kath.cineapp.ui.features.payment.components.getFormattedDate
import org.koin.androidx.compose.koinViewModel

@Composable
fun PaymentScreen(
    viewModel: PaymentViewModel = koinViewModel(),
    total: Double,
    onPaymentSuccess: () -> Unit,
    closePaymentScreen: () -> Unit
) {

    val focusManager = LocalFocusManager.current

    val state = viewModel.state.collectAsState()

    val resultPaymentState = viewModel.resultState.collectAsState()

    viewModel.getUser()

    Scaffold(
        topBar = {
            Text(
                text = "Completa el pago",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    ) { pv ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = pv.calculateTopPadding() + 16.dp,
                    bottom = pv.calculateBottomPadding() + 40.dp,
                    start = 24.dp,
                    end = 24.dp
                )
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                    })
                }, verticalArrangement = Arrangement.SpaceBetween
        ) {

            Box(Modifier.weight(1f)) {
                Column(
                    Modifier.verticalScroll(rememberScrollState())
                ) {
                    CardNumberField(
                        title = "Card Number",
                        cardNumber = state.value.formFields.cardNumber,
                        onPostValue = {
                            val formField = viewModel.currentState().formFields.copy(
                                cardNumber = UiField(value = it)
                            )
                            viewModel.updateFields(formField)
                        },
                        type = KeyboardType.Number,
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {

                        Column(Modifier.weight(1f)) {
                            Text(
                                text = "Expired Date", style = TextStyle(
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = Color(0xFF808289),
                                ), modifier = Modifier.padding(top = 8.dp)
                            )

                            Spacer(Modifier.height(16.dp))

                            ReadOnlyTextField(
                                value = state.value.formFields.expirationDate.value,
                                onValueChange = {},
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    viewModel.updateState(viewModel.state.value.copy(showDatePicker = true))
                                }
                            )
                        }

                        FormField(
                            title = "CVC/CVV",
                            onValueChange = {
                                val formField = viewModel.currentState().formFields.copy(
                                    cvvCode = UiField(value = it)
                                )
                                viewModel.updateFields(formField)
                            },
                            uiField = state.value.formFields.cvvCode,
                            modifier = Modifier.weight(1f),
                            type = KeyboardType.Number
                        )
                    }

                    FormField(
                        title = "Cardholder Name",
                        onValueChange = {
                            val formField = viewModel.currentState().formFields.copy(
                                cardHolder = UiField(value = it)
                            )
                            viewModel.updateFields(formField)
                        },
                        uiField = state.value.formFields.cardHolder,
                        capitalize = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))


                    Text(
                        text = "Billing Address", style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF333333),
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    FormField(
                        title = "Address Line",
                        onValueChange = {
                            val formField = viewModel.currentState().formFields.copy(
                                address = UiField(value = it)
                            )
                            viewModel.updateFields(formField)
                        },
                        uiField = state.value.formFields.address,
                    )

                    Spacer(modifier = Modifier.height(12.dp))


                    Text(
                        text = "Document Details", style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF333333),
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    FormField(
                        title = "Document Type",
                        onValueChange = {
                            val formField = viewModel.currentState().formFields.copy(
                                documentType = UiField(value = it)
                            )
                            viewModel.updateFields(formField)
                        },
                        uiField = state.value.formFields.documentType,
                    )

                    FormField(
                        title = "Document Number",
                        onValueChange = {
                            val formField = viewModel.currentState().formFields.copy(
                                documentNumber = UiField(value = it)
                            )
                            viewModel.updateFields(formField)
                        },
                        uiField = state.value.formFields.documentNumber,
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.updateState(viewModel.currentState().copy(isLoading = true))
                    viewModel.sendPayment(viewModel.user.value, total)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .imePadding()
                    .then(Modifier.wrapContentHeight()),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                shape = RoundedCornerShape(30.dp),
                contentPadding = PaddingValues(16.dp),
                enabled = true
            ) {
                Text(
                    text = "Pagar",
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }

    if (state.value.isLoading) {
        FullscreenProgressBar()
    }

    if (state.value.showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { selectedMills ->
                val formatted = selectedMills?.getFormattedDate("dd MMM yyyy") ?: "-"
                val formField = viewModel.currentState().formFields.copy(
                    expirationDate = UiField(formatted), expirationDateTimestamp = selectedMills
                )
                viewModel.updateState(
                    viewModel.currentState().copy(formFields = formField, showDatePicker = false)
                )
            },
            initialSelectedDate = state.value.formFields.expirationDateTimestamp
        )
    }

    when (resultPaymentState.value) {
        ResultUiState.Error -> {
            val openAlertDialog = remember { mutableStateOf(true) }

            when {
                openAlertDialog.value -> {
                    AlertDialog(
                        icon = {
                            Icon(Icons.Filled.Error, contentDescription = "Example Icon")
                        },
                        title = {
                            Text(text = "Error en la transacción")
                        },
                        text = {
                            Text(text = "Por favor intente nuevamente")
                        },
                        onDismissRequest = {
                            openAlertDialog.value = false
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    openAlertDialog.value = false
                                    closePaymentScreen()
                                }
                            ) {
                                Text("Aceptar")
                            }
                        }
                    )
                }
            }

        }

        ResultUiState.None -> {

        }

        ResultUiState.Success -> {
            closePaymentScreen()
            onPaymentSuccess()
        }
    }

}
