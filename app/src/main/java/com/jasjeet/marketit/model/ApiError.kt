package com.jasjeet.marketit.model

import com.google.gson.annotations.SerializedName

/** It is being assumed that all of the API endpoints return errors in this
 * structure only.*/
data class ApiError (
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("success")
    val success: Boolean = false
)
