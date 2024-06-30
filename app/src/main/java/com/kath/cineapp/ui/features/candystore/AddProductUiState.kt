package com.kath.cineapp.ui.features.candystore

data class AddProductUiState(
    var list: MutableList<CandyStoreModel> = mutableListOf(),
    var totalAmount: Double = 0.0,
    var totalProducts: Double = 0.0
) {
    fun addProduct(candyStoreModel: CandyStoreModel) {
        list.add(candyStoreModel)
        var sum = 0.0
        list.forEach {
            sum += it.getDoublePrice()
        }
        totalProducts = sum
        totalAmount = sum + 16
    }
}