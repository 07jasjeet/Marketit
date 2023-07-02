package com.jasjeet.marketit.model

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jasjeet.marketit.util.Resource
import retrofit2.Response
import java.io.IOException

/** These exceptions need to handled in view-model, shown via UI and not just thrown.
 * @param genericToast Generic message for the error.
 * @param actualResponse Actual response given by the server.*/
interface ResponseError {
    
    val genericToast: String
    var actualResponse: String?
    
    /** Simple function that returns the most suitable message to show the user.*/
    fun toast(): String = actualResponse ?: genericToast
    
    companion object {
        
        fun <T> logAndReturn(error: Throwable): Resource<T> {
            Log.e("Marketit", error.localizedMessage ?: "Error" )
            return if (error is IOException)
                Resource.failure(error = GeneralError.NETWORK_ERROR)
            else
                Resource.failure(error = GeneralError.UNKNOWN)
        }
        
        /** Get [ResponseError] for a given Retrofit **error** [Response] from server.*/
        fun <T> getGeneralResponseError(response: Response<T>) : ResponseError {
            val error = parseError(response)
            return getGeneralErrorType(error)
        }
        
        /** Parsing server response into [ApiError] class.*/
        private fun <T> parseError(response: Response<T>) : ApiError =
            Gson().fromJson(
                /* json = */ response.errorBody()?.string(),
                /* typeOfT = */ object : TypeToken<ApiError>() {}.type
            )
        
        
        /** Get [ResponseError] for social type API endpoints. Automatically puts actual error message.
         * Prefer using [getSocialResponseError] in repository functions.*/
        private fun getGeneralErrorType(apiError: ApiError) : ResponseError {
            val error = apiError.message
            
            return when {
                else -> GeneralError.UNKNOWN
            }.apply { actualResponse = error }
        }
        
        
        
    }
}