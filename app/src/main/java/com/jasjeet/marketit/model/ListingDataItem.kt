package com.jasjeet.marketit.model

data class ListingDataItem(
    val image: String = "",
    val price: Double,
    val product_name: String,
    val product_type: String,
    val tax: Double
)