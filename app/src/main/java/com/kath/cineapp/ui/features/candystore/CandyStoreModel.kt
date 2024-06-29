package com.kath.cineapp.ui.features.candystore

data class CandyStoreModel(
    val name: String,
    val description: String,
    val price: String,
    val image: String
){
    fun getFormattedPrice(): String{
        return "S/. $price"
    }

    fun getDoublePrice(): Double{
        return price.toDouble()
    }
}
