package com.kath.cineapp.ui.features.payment

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun PaymentScreen(viewModel: PaymentViewModel = koinViewModel()) {

    val focusManager = LocalFocusManager.current

    val state = viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            //SecondaryToolBar(onBack = onBack, title = UiText.StringResource(R.string.add_a_card))
        },
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

                            ReadonlyTextField(
                                value = state.value.formFields.expirationDate.value,
                                onValueChange = {},
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    viewModel.updateState(viewModel.state.value.copy(showDatePicker = true))
                                },
                                error = state.value.formFields.expirationDate.error
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

            PrimaryButton(
                onClick = {
                    viewModel.getUser()
                    val formField = viewModel.currentState().formFields.copy(
                        email = UiField(value = viewModel.user.value.email)
                    )
                    viewModel.updateFields(formField)
                    viewModel.sendPayment()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .imePadding(),
                text = "Pagar"
            )
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

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    onDismissRequest: (selectedMills: Long?) -> Unit = {},
    initialSelectedDate: Long? = null,
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialSelectedDate,
    )
    val dialogState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    ModalBottomSheet(
        onDismissRequest = {
            onDismissRequest.invoke(datePickerState.selectedDateMillis)
        },
        sheetState = dialogState,
        containerColor = MaterialTheme.colorScheme.background
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            DatePicker(
                state = datePickerState
            )
        }
    }
}

@Composable
fun FullscreenProgressBar(
    backgroundColor: Color = Color(0x80000000),
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .pointerInput(Unit) {
                // Skip touch events
            },
        contentAlignment = Alignment.Center
    ) {
        DotsProgressIndicator()
    }
}


@Composable
fun DotsProgressIndicator(
    modifier: Modifier = Modifier,
    circleSize: Dp = 24.dp,
    spaceBetween: Dp = 12.dp,
    travelDistance: Dp = 20.dp,
    circleColor: Color = MaterialTheme.colorScheme.primary,
) {
    val circles = listOf(
        remember { Animatable(0f) },
        remember { Animatable(0f) },
        remember { Animatable(0f) },
    )

    // Actual values during animation
    val circleValues = circles.map { it.value }
    val distance = with(LocalDensity.current) { travelDistance.toPx() }

    // Launch animation
    circles.forEachIndexed { index, animatable ->
        LaunchedEffect(key1 = animatable) {
            delay(index * 100L)
            animatable.animateTo(
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = keyframes {
                        durationMillis = 1200
                        0.0f at 0 with LinearOutSlowInEasing
                        1.0f at 300 with LinearOutSlowInEasing
                        0.0f at 600 with LinearOutSlowInEasing
                        0.0f at 1200 with LinearOutSlowInEasing
                    },
                    repeatMode = RepeatMode.Restart
                )
            )
        }
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spaceBetween)
    ) {
        circleValues.forEach { value ->
            Box(
                Modifier
                    .size(circleSize)
                    .graphicsLayer {
                        translationY = -value * distance
                    }
                    .background(
                        shape = CircleShape,
                        color = circleColor
                    )
            )
        }
    }


}


@Composable
fun PrimaryButton(
    onClick: () -> Unit,
    modifier: Modifier,
    text: String,
    isEnabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        modifier = modifier.then(Modifier.wrapContentHeight()),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        shape = RoundedCornerShape(30.dp),
        contentPadding = PaddingValues(16.dp),
        enabled = isEnabled
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleSmall
        )
    }
}


data class UiField(
    val value: String,
    val error: String? = null,
)

@Composable
fun CardNumberField(
    title: String,
    cardNumber: UiField,
    onPostValue: (value: String) -> Unit,
    modifier: Modifier = Modifier,
    type: KeyboardType = KeyboardType.Text,
) {

    Column(modifier = modifier) {
        Text(
            text = title,
            style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF808289),
            ),
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(Modifier.height(16.dp))

        var textFieldValue by remember {
            mutableStateOf(TextFieldValue(text = ""))
        }

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = textFieldValue,
            onValueChange = { newValue ->
                textFieldValue = newValue
                onPostValue(newValue.text)
            },
            singleLine = true,
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                keyboardType = type,
            ),
            visualTransformation = { number ->
                CardUiHelpers.formatCardNumber(number)
            },
            trailingIcon = {
                Box(
                    Modifier
                        .padding(end = 10.dp)
                        .heightIn(max = 32.dp)
                ) {
                    SmallCardIcon()
                }
            }
        )
    }
}


@Composable
fun ReadonlyTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    error: String? = null,
) {
    Box(modifier = modifier) {
        PrimaryTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = visualTransformation,
            error = error
        )

        // TODO ripple
        Box(
            modifier = Modifier
                .matchParentSize()
                .alpha(0f)
                .clickable(onClick = onClick),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrimaryTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = TextStyle(
        fontSize = 14.sp,
        color = Color.LightGray,
        fontWeight = FontWeight.Normal,
        lineHeight = 20.sp
    ),
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    error: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    colors: TextFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
        unfocusedIndicatorColor = Color(0xFFCFCFD3),
        errorContainerColor = Color.Transparent,
    ),
    shape: Shape = RoundedCornerShape(4.dp),
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        interactionSource = interactionSource,
        enabled = enabled,
        singleLine = singleLine,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        readOnly = readOnly,
        maxLines = maxLines,
        textStyle = textStyle,
    ) { innerTextField ->
        OutlinedTextFieldDefaults.DecorationBox(
            value = value,
            visualTransformation = visualTransformation,
            innerTextField = innerTextField,
            singleLine = singleLine,
            enabled = enabled,
            interactionSource = interactionSource,
            contentPadding = PaddingValues(
                vertical = 14.dp, horizontal = 16.dp
            ),
            placeholder = placeholder,
            colors = colors,
            isError = error != null,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            supportingText = {
                if (error != null) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = error,
                        color = MaterialTheme.colorScheme.error
                    )
                } else supportingText?.invoke()
            },
            label = label,
            container = {
                OutlinedTextFieldDefaults.ContainerBox(
                    enabled,
                    error != null,
                    interactionSource,
                    colors,
                    shape
                )
            }
        )
    }
}


// TODO refactoring
@Composable
private fun FormField(
    title: String,
    // TODO rename
    uiField: UiField,
    onValueChange: (value: String) -> Unit,
    modifier: Modifier = Modifier,
    type: KeyboardType = KeyboardType.Text,
    capitalize: Boolean = false,
) {

    Column(modifier = modifier) {
        Text(
            text = title, style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF808289),
            ),
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(Modifier.height(16.dp))

        PrimaryTextField(
            value = if (capitalize) {
                uiField.value.toUpperCase(Locale.ENGLISH)
            } else {
                uiField.value
            },
            onValueChange = {
                onValueChange.invoke(it)
            },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = type,
            ),
            error = uiField.error
        )
    }
}
