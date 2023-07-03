package com.jasjeet.marketit.model

enum class GeneralError(override val genericToast: String, override var actualResponse: String? = null): ResponseError {
    
    AUTH_HEADER_NOT_FOUND(genericToast = "Please login in order to perform this operation."),
    
    RATE_LIMIT_EXCEEDED(genericToast = "Server slow down detected."),
    
    NETWORK_ERROR(genericToast = "Please check you internet connection."),
    
    UNKNOWN(genericToast = "Some error has occurred.");
    
}