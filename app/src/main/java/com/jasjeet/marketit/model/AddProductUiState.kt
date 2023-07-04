package com.jasjeet.marketit.model

import com.jasjeet.marketit.util.Resource

data class AddProductUiState(
    val status: Resource.Status = Resource.Status.LOADING,
    val error: ResponseError? = null
)