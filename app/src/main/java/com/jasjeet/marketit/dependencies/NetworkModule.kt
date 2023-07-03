package com.jasjeet.marketit.dependencies

import com.jasjeet.marketit.repository.AppRepository
import com.jasjeet.marketit.repository.AppRepositoryImpl
import com.jasjeet.marketit.service.ApiService
import com.jasjeet.marketit.util.Constants
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    
    single {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
    
    single<AppRepository> {
        AppRepositoryImpl(apiService = get())
    }
    
}