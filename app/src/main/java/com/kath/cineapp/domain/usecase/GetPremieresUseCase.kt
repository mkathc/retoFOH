package com.kath.cineapp.domain.usecase

import com.kath.cineapp.domain.model.Premieres
import com.kath.cineapp.domain.repository.PremieresRepository
import com.kath.cineapp.ui.features.home.PremieresModel

class GetPremieresUseCase(
    private val premieresRepository: PremieresRepository
) {
    suspend operator fun invoke(): Result<List<PremieresModel>> {
        return premieresRepository.getPremieres().fold(
            onSuccess = { list ->
                Result.success(list.map { it.parseSuccess() })
            },
            onFailure = {
                Result.failure(it)
            }
        )
    }

    private fun Premieres.parseSuccess(): PremieresModel {
        return PremieresModel(
            description = description,
            image = image
        )
    }
}