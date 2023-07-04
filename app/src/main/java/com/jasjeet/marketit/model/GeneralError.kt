package com.jasjeet.marketit.model

/** Errors which are common to one or more API endpoints of different repositories belong here.*/
enum class GeneralError(override val genericToast: String, override var actualResponse: String? = null): ResponseError {
    
    NETWORK_ERROR(genericToast = "Please check you internet connection."),
    
    UNKNOWN(genericToast = "Some error has occurred.");
    
}