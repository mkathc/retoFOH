package com.kath.cineapp.data.remote.premieres

import com.kath.cineapp.data.remote.model.PremieresResponse

interface PremieresRemoteDatasource {

    suspend fun getPremieres(): Result<List<PremieresResponse>>

}
