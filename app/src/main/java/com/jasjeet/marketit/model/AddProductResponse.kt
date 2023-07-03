package com.jasjeet.marketit.model

data class AddProductResponse(
    val message: String? = null,
    val product_details: ListingDataItem? = null,
    val product_id: Int? = null,
    val success: Boolean? = null
)