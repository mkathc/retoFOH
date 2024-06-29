package com.kath.cineapp.ui.features.payment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kath.cineapp.domain.model.Complete
import com.kath.cineapp.domain.usecase.GetUserUseCase
import com.kath.cineapp.domain.usecase.SendCompleteUseCase
import com.kath.cineapp.domain.usecase.SendPaymentUseCase
import com.kath.cineapp.ui.features.login.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PaymentViewModel(
    private val getUserUseCase: GetUserUseCase,
    private val sendPaymentUseCase: SendPaymentUseCase,
    private val sendCompleteUseCase: SendCompleteUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(PaymentState())
    val state = _state.asStateFlow()

    fun updateState(paymentState: PaymentState) {
        _state.value = paymentState
    }

    private val _user: MutableStateFlow<UserModel> = MutableStateFlow(UserModel("", ""))
    val user: StateFlow<UserModel> = _user.asStateFlow()

    fun currentState() = _state.value

    /*   fun emitIntent(intent: AddCardIntent) {
           val currentState = _state.value

           when (intent) {
               is AddCardIntent.EnterScreen -> {
                   if (BuildConfig.DEBUG) {
                       reduceInitialMockState()
                   }
               }

               is AddCardIntent.StringFieldChanged -> {
                   when (intent.fieldType) {
                       AddCardFieldType.CARD_NUMBER -> reduceFields(currentState.formFields.copy(cardNumber = UiField(intent.fieldValue)))
                       AddCardFieldType.CARD_HOLDER -> reduceFields(currentState.formFields.copy(cardHolder = UiField(intent.fieldValue)))
                       AddCardFieldType.ADDRESS_LINE_1 -> reduceFields(currentState.formFields.copy(addressFirstLine = UiField(intent.fieldValue)))
                       AddCardFieldType.ADDRESS_LINE_2 -> reduceFields(currentState.formFields.copy(addressSecondLine = UiField(intent.fieldValue)))
                       AddCardFieldType.CVV_CODE -> reduceFields(currentState.formFields.copy(cvvCode = UiField(intent.fieldValue)))
                       AddCardFieldType.CARD_EXPIRATION_DATE -> { *//* handled in other intent *//*
                    }
                }
            }

            is AddCardIntent.ToggleDatePicker -> {
                _state.update { curr ->
                    curr.copy(showDatePicker = intent.isShown)
                }
            }

            is AddCardIntent.ExpirationPickerSet -> {
                val formatted = if (intent.date == null) {
                    "-"
                } else {
                    intent.date.getFormattedDate("dd MMM yyyy")
                }

                reduceFields(
                    currentState.formFields.copy(
                        expirationDate = UiField(formatted), expirationDateTimestamp = intent.date
                    )
                )
            }

            is AddCardIntent.SaveCard -> {
                _state.update { curr ->
                    curr.copy(
                        isLoading = true
                    )
                }

                viewModelScope.launch {
                    val formValid = reduceValidationWithResult()
                    if (!formValid) {
                        _state.update { curr ->
                            curr.copy(
                                isLoading = false,
                                cardSavedEvent = triggered(
                                    OperationResult.Failure(error = AppError(ErrorType.GENERIC_VALIDATION_ERROR))
                                )
                            )
                        }
                    } else {
                        val res = OperationResult.runWrapped {
                            addCardUseCase.execute(payload = AddCardPayload(
                                cardNumber = currentState.formFields.cardNumber.value,
                                cardHolder = currentState.formFields.cardHolder.value,
                                expirationDate = currentState.formFields.expirationDateTimestamp!!,
                                addressFirstLine = currentState.formFields.addressFirstLine.value,
                                addressSecondLine = currentState.formFields.addressSecondLine.value,
                                cvvCode =   currentState.formFields.cvvCode.value
                            )
                            )
                        }

                        when (res) {
                            is OperationResult.Success -> {
                                _state.update { curr ->
                                    curr.copy(
                                        isLoading = false, cardSavedEvent = triggered(res)
                                    )
                                }
                            }
                            is OperationResult.Failure -> {
                                _state.update { curr ->
                                    curr.copy(
                                        isLoading = false, cardSavedEvent = triggered(res)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            is AddCardIntent.ConsumeResultEvent -> {
                _state.update { curr ->
                    curr.copy(
                        cardSavedEvent = consumed()
                    )
                }
            }
        }
    }*/

    fun updateFields(
        cardFields: CardFormFields,
    ) {
        _state.update { curr ->
            curr.copy(formFields = cardFields)
        }
    }


    fun getUser() {
        viewModelScope.launch(Dispatchers.IO) {
            getUserUseCase().onSuccess {
                _user.value = it
            }.onFailure {
                _user.value = UserModel("", "")
            }
        }
    }

    fun sendPayment() {

        val model = PaymentModel(
            cardNumber = currentState().formFields.cardNumber.value,
            cardHolder = currentState().formFields.cardHolder.value,
            expirationDate = currentState().formFields.expirationDate.value,
            documentNumber = currentState().formFields.documentNumber.value,
            documentType = currentState().formFields.documentType.value,
            cvvCode = currentState().formFields.cvvCode.value,
            email = currentState().formFields.email.value,
            address = currentState().formFields.address.value
        )

        viewModelScope.launch(Dispatchers.IO) {
            sendPaymentUseCase(model).onSuccess {
                Log.e("sendPaymentUseCase", "onSuccess")
                sendCompleteUseCase.invoke(
                    Complete(
                        name = user.value.name,
                        mail = user.value.email,
                        dni = currentState().formFields.documentNumber.value,
                        operationDate = it.operationDate
                    )
                ).onSuccess {
                    Log.e("sendCompleteUseCase", "onSuccess")
                }.onFailure {
                    Log.e("sendCompleteUseCase", "onFailure")
                }
            }.onFailure {
                Log.e("sendPaymentUseCase", "onFailure")
            }
        }
    }
}