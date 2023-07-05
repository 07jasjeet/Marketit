package com.jasjeet.marketit.repository

import android.util.Log
import com.jasjeet.marketit.model.AddProductResponse
import com.jasjeet.marketit.model.ListingData
import com.jasjeet.marketit.model.ResponseError.Companion.getGeneralResponseError
import com.jasjeet.marketit.model.ResponseError.Companion.logAndReturn
import com.jasjeet.marketit.service.ApiService
import com.jasjeet.marketit.util.Resource
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import java.io.File

class AppRepositoryImpl(private val apiService: ApiService) : AppRepository {
    
    override suspend fun getData(): Resource<ListingData> =
        runCatching {
            val response = apiService.getListings()
            return@runCatching if (response.isSuccessful) {
                 Resource.success(response.body() ?: ListingData())
            } else {
                val error = getGeneralResponseError(response)
                Resource.failure(error = error)
            }
        }.getOrElse { logAndReturn(it) }
    
    
    override suspend fun addProduct(
        name: String,
        type: String,
        price:String,
        tax: String,
        image: File?
    ): Resource<AddProductResponse> =
        runCatching {
            val response = apiService.addProduct(
                name,
                type,
                price,
                tax,
                if (image != null){
                    MultipartBody.Part.createFormData(
                        "files[]",
                        image.name,
                        RequestBody.create(MediaType.parse("image/*"), image)
                    )
                } else { null }
                
            )
            return@runCatching if (response.isSuccessful) {
                Resource.success((response.body() ?: AddProductResponse()))
            } else {
                val error = getGeneralResponseError(response)
                Resource.failure(error = error)
            }
        }.getOrElse { logAndReturn(it) }
    
}