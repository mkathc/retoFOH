package com.kath.cineapp.data.remote.candystore

import com.kath.cineapp.data.remote.model.CandyStoreResponse
import com.kath.cineapp.data.remote.model.PremieresResponse

interface CandyStoreRemoteDatasource {

    suspend fun getCandyStore(): Result<List<CandyStoreResponse>>

}
