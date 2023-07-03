package com.jasjeet.marketit.model

data class AddProductUiState(
    val result: AddProductResponse? = null,
    val error: ResponseError? = null
)