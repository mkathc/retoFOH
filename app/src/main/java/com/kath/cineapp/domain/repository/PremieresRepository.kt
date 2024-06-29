package com.kath.cineapp.domain.repository

import com.kath.cineapp.domain.model.Premieres

interface PremieresRepository {
    suspend fun getPremieres(): Result<List<Premieres>>
}