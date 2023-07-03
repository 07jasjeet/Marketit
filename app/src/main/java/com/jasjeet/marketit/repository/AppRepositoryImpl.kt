package com.jasjeet.marketit.repository

import com.jasjeet.marketit.model.AddProductResponse
import com.jasjeet.marketit.model.ListingData
import com.jasjeet.marketit.model.ResponseError.Companion.getGeneralResponseError
import com.jasjeet.marketit.model.ResponseError.Companion.logAndReturn
import com.jasjeet.marketit.service.ApiService
import com.jasjeet.marketit.util.Resource
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
    
    /** Add a product.
     *
     * @param image not uploading images even using postman so is useless for now.*/
    override suspend fun addProduct(
        name: String,
        type: String,
        price:String,
        tax: String,
        image: File
    ): Resource<AddProductResponse> =
        runCatching {
            val response = apiService.addProduct(name, type, price, tax, /*image*/)
            return@runCatching if (response.isSuccessful) {
                Resource.success((response.body() ?: AddProductResponse()))
            } else {
                val error = getGeneralResponseError(response)
                Resource.failure(error = error)
            }
        }.getOrElse { logAndReturn(it) }
    
}