package com.kath.cineapp.data.remote.model

import com.google.gson.annotations.SerializedName

data class CompleteBodyRequest(
    val name: String,
    val mail: String,
    val dni: String,
    @SerializedName("operation_date")
    val operationDate: String
)
