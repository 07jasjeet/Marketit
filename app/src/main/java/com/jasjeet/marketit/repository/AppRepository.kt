package com.jasjeet.marketit.repository

import com.jasjeet.marketit.model.ListingData
import com.jasjeet.marketit.util.Resource

interface AppRepository {
    
    suspend fun getData(): Resource<ListingData>
    
    suspend fun addProduct()
    
}