package com.kath.cineapp.di

import com.kath.cineapp.data.local.preferences.ILocalPreferences
import com.kath.cineapp.data.local.preferences.LocalPreferencesImpl
import com.kath.cineapp.data.local.user.UserLocalDatasource
import com.kath.cineapp.data.local.user.UserLocalDatasourceImpl
import com.kath.cineapp.data.remote.api.CineApi
import com.kath.cineapp.data.remote.candystore.CandyStoreRemoteDatasource
import com.kath.cineapp.data.remote.candystore.CandyStoreRemoteDatasourceImpl
import com.kath.cineapp.data.remote.complete.CompleteRemoteDatasource
import com.kath.cineapp.data.remote.complete.CompleteRemoteDatasourceImpl
import com.kath.cineapp.data.remote.payu.PayURemoteDatasource
import com.kath.cineapp.data.remote.payu.PayURemoteDatasourceImpl
import com.kath.cineapp.data.remote.premieres.PremieresRemoteDatasource
import com.kath.cineapp.data.remote.premieres.PremieresRemoteDatasourceImpl
import com.kath.cineapp.data.repository.CandyStoreRepositoryImpl
import com.kath.cineapp.data.repository.CompleteRepositoryImpl
import com.kath.cineapp.data.repository.PaymentRepositoryImpl
import com.kath.cineapp.data.repository.PremieresRepositoryImpl
import com.kath.cineapp.data.repository.UserRepositoryImpl
import com.kath.cineapp.domain.repository.CandyStoreRepository
import com.kath.cineapp.domain.repository.CompleteRepository
import com.kath.cineapp.domain.repository.PaymentRepository
import com.kath.cineapp.domain.repository.PremieresRepository
import com.kath.cineapp.domain.repository.UserRepository
import com.kath.cineapp.domain.usecase.GetCandyStoreUseCase
import com.kath.cineapp.domain.usecase.GetPremieresUseCase
import com.kath.cineapp.domain.usecase.GetUserIsLoggedUseCase
import com.kath.cineapp.domain.usecase.GetUserUseCase
import com.kath.cineapp.domain.usecase.SaveUserUseCase
import com.kath.cineapp.domain.usecase.SendCompleteUseCase
import com.kath.cineapp.domain.usecase.SendPaymentUseCase
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import org.koin.androidx.viewmodel.dsl.viewModelOf
import com.kath.cineapp.ui.features.home.HomeViewModel
import com.kath.cineapp.ui.features.candystore.CandyStoreViewModel
import com.kath.cineapp.ui.features.login.model.LoginViewModel
import com.kath.cineapp.ui.features.payment.PaymentViewModel
import okhttp3.logging.HttpLoggingInterceptor

import org.koin.android.ext.koin.androidContext

val appModule = module {

    single { provideOkHttpClient() }
    single { provideRetrofit(get()) }
    single { provideApiService(get(), CineApi::class.java) }

    single<ILocalPreferences> {
        LocalPreferencesImpl(
            androidContext(),
            LocalPreferencesImpl.USER_SHARED_PREFERENCES
        )
    }
    single<UserLocalDatasource> { UserLocalDatasourceImpl(get()) }
    single<UserRepository> { UserRepositoryImpl(get()) }
    single<SaveUserUseCase> { SaveUserUseCase(get()) }
    single<GetUserUseCase> { GetUserUseCase(get()) }

    single<PremieresRemoteDatasource> { PremieresRemoteDatasourceImpl(get()) }
    single<PremieresRepository> { PremieresRepositoryImpl(get()) }
    single<GetPremieresUseCase> { GetPremieresUseCase(get()) }

    single<CandyStoreRemoteDatasource> { CandyStoreRemoteDatasourceImpl(get()) }
    single<CandyStoreRepository> { CandyStoreRepositoryImpl(get()) }
    single<GetCandyStoreUseCase> { GetCandyStoreUseCase(get()) }

    single<PayURemoteDatasource> { PayURemoteDatasourceImpl(get()) }
    single<PaymentRepository> { PaymentRepositoryImpl(get()) }
    single<SendPaymentUseCase> { SendPaymentUseCase(get()) }

    single<CompleteRemoteDatasource> { CompleteRemoteDatasourceImpl(get()) }
    single<CompleteRepository> { CompleteRepositoryImpl(get()) }
    single<SendCompleteUseCase> { SendCompleteUseCase(get()) }
    single<GetUserIsLoggedUseCase> { GetUserIsLoggedUseCase(get()) }

    viewModelOf(::HomeViewModel)
    viewModelOf(::CandyStoreViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::PaymentViewModel)
}


fun provideOkHttpClient(): OkHttpClient {
    val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    val client = OkHttpClient.Builder()
    client.interceptors().add(interceptor)
    return client.build()
}

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
    Retrofit.Builder().baseUrl("https://667f16f7f2cb59c38dc80872.mockapi.io/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient).build()

fun provideApiService(retrofit: Retrofit, apiService: Class<CineApi>) =
    createService(retrofit, apiService)

fun <T> createService(retrofit: Retrofit, serviceClass: Class<T>): T = retrofit.create(serviceClass)
