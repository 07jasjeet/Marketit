package com.jasjeet.marketit.model

data class AddProductResponse(
    val message: String,
    val product_details: ListingDataItem,
    val product_id: Int,
    val success: Boolean
)