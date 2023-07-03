package com.jasjeet.marketit.service

import com.jasjeet.marketit.model.AddProductResponse
import com.jasjeet.marketit.model.ListingData
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import java.io.File

interface ApiService {
    
    @GET("get")
    suspend fun getListings(): Response<ListingData>
    
    @FormUrlEncoded
    @POST("add")
    suspend fun addProduct(
        @Field("product_name") name: String,
        @Field("product_type") type: String,
        @Field("price") price: String,
        @Field("tax") tax: String,
        //@Field("file[]") images: File,
    ): Response<AddProductResponse>
    
}