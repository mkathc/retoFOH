package com.kath.cineapp.data.remote.api

import com.kath.cineapp.data.remote.model.CandyStoreResponse
import com.kath.cineapp.data.remote.model.CompleteBodyRequest
import com.kath.cineapp.data.remote.model.CompleteResponse
import com.kath.cineapp.data.remote.model.PayURequest
import com.kath.cineapp.data.remote.model.PaymentResponse
import com.kath.cineapp.data.remote.model.PremieresResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface CineApi {
    @GET("movies")
    suspend fun getPremieres(): Response<List<PremieresResponse>>

    @GET("/store")
    suspend fun getCandyStore(): Response<List<CandyStoreResponse>>

    @Headers("Accept: application/json")
    @POST("https://sandbox.api.payulatam.com/payments-api/4.0/service.cgi")
    suspend fun sendPaymentToPayU(@Body value: PayURequest): Response<PaymentResponse>

    @POST("https://api.mockfly.dev/mocks/fce44076-609c-479e-91d7-f4b34f7fa3ef/complete")
    suspend fun sendCompleteTransaction(@Body value: CompleteBodyRequest): Response<CompleteResponse>
}