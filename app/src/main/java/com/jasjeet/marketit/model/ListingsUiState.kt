package com.jasjeet.marketit.model

data class ListingsUiState(
    val listings: ListingData?,
    val error: ResponseError?
)