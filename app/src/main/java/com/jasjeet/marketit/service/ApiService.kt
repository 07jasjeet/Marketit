package com.jasjeet.marketit.service

import com.jasjeet.marketit.model.AddProductResponse
import com.jasjeet.marketit.model.ListingData
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.io.File

interface ApiService {
    
    @GET("get")
    suspend fun getListings(): Response<ListingData>
    
    @Multipart
    @POST("add")
    suspend fun addProduct(
        @Part("product_name") name: String,
        @Part("product_type") type: String,
        @Part("price") price: String,
        @Part("tax") tax: String,
        @Part images: MultipartBody.Part?,
    ): Response<AddProductResponse>
    
}