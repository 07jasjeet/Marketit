package com.jasjeet.marketit.service

import com.jasjeet.marketit.model.AddProductResponse
import com.jasjeet.marketit.model.ListingData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    
    @GET("api/public/get")
    suspend fun getListings(): Response<ListingData>
    
    @POST("api/public/add")
    suspend fun addProduct(): Response<AddProductResponse>
}