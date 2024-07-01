package com.kath.cineapp.ui.features.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kath.cineapp.domain.model.Complete
import com.kath.cineapp.domain.usecase.GetUserUseCase
import com.kath.cineapp.domain.usecase.SendCompleteUseCase
import com.kath.cineapp.domain.usecase.SendPaymentUseCase
import com.kath.cineapp.ui.features.login.model.UserModel
import com.kath.cineapp.ui.features.payment.model.PaymentModel
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

    private val _resultState: MutableStateFlow<ResultUiState> = MutableStateFlow(ResultUiState.None)
    val resultState: StateFlow<ResultUiState> get() = _resultState


    private fun updateResultState(resultUiState: ResultUiState) {
        _resultState.value = resultUiState
    }

    private val _user: MutableStateFlow<UserModel> = MutableStateFlow(UserModel("", ""))
    val user: StateFlow<UserModel> = _user.asStateFlow()

    fun currentState() = _state.value

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

    fun sendPayment(user: UserModel, value: Double) {

        val model = PaymentModel(
            cardNumber = currentState().formFields.cardNumber.value,
            cardHolder = currentState().formFields.cardHolder.value,
            expirationDate = currentState().formFields.expirationDate.value,
            documentNumber = currentState().formFields.documentNumber.value,
            documentType = currentState().formFields.documentType.value,
            cvvCode = currentState().formFields.cvvCode.value,
            email = user.email,
            address = currentState().formFields.address.value,
            value = value
        )

        viewModelScope.launch(Dispatchers.IO) {
            sendPaymentUseCase(model).onSuccess {
                sendCompleteUseCase.invoke(
                    Complete(
                        name = user.name,
                        mail = user.email,
                        dni = currentState().formFields.documentNumber.value,
                        operationDate = it.operationDate
                    )
                ).onSuccess {
                    updateResultState(ResultUiState.Success)
                }.onFailure {
                    updateResultState(ResultUiState.Error)
                }
            }.onFailure {
                updateResultState(ResultUiState.Error)
            }
        }
    }
}