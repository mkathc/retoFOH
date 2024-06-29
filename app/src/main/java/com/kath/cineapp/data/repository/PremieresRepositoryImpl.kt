package com.kath.cineapp.data.repository

import android.util.Log
import com.kath.cineapp.data.remote.model.PremieresResponse
import com.kath.cineapp.data.remote.premieres.PremieresRemoteDatasource
import com.kath.cineapp.domain.model.Premieres
import com.kath.cineapp.domain.repository.PremieresRepository

class PremieresRepositoryImpl(
    private val premieresRemoteDatasource: PremieresRemoteDatasource,
) : PremieresRepository {
    override suspend fun getPremieres(): Result<List<Premieres>> {
        return premieresRemoteDatasource.getPremieres().fold(
            onSuccess = { list ->
                Result.success(list.map { premieres -> premieres.parseSuccess() })
            },
            onFailure = {
                Result.failure(it)
            }
        )
    }

    private fun PremieresResponse.parseSuccess(): Premieres {
        return Premieres(
            description = title,
            image = poster
        )
    }
}