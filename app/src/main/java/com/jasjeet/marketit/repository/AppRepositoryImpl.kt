package com.jasjeet.marketit.repository

import com.jasjeet.marketit.model.GeneralError
import com.jasjeet.marketit.model.ListingData
import com.jasjeet.marketit.model.ResponseError.Companion.logAndReturn
import com.jasjeet.marketit.service.ApiService
import com.jasjeet.marketit.util.Resource
import java.io.IOException

class AppRepositoryImpl(
    private val apiService: ApiService
) : AppRepository {
    
    override suspend fun getData(): Resource<ListingData> =
        runCatching {
            val response = apiService.getListings()
            return@runCatching if (response.isSuccessful){
                 Resource.success(response.body() ?: ListingData())
            } else {
                Resource.failure()
            }
        }.getOrElse {
            logAndReturn(it)
        }
    
    override suspend fun addProduct() {
        // TODO
    }
    
}