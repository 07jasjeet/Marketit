package com.jasjeet.marketit.model

data class ListingsUiState(
    val listings: ListingData? = null,
    val error: ResponseError? = null
)