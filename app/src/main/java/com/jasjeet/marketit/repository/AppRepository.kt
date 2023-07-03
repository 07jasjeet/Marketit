package com.jasjeet.marketit.repository

import com.jasjeet.marketit.model.AddProductResponse
import com.jasjeet.marketit.model.ListingData
import com.jasjeet.marketit.util.Resource
import java.io.File

interface AppRepository {
    
    suspend fun getData(): Resource<ListingData>
    
    suspend fun addProduct(
        name: String,
        type: String,
        price:String,
        tax: String,
        image: File
    ): Resource<AddProductResponse>
    
}