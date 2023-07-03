package com.jasjeet.marketit.dependencies

import org.koin.dsl.module

/** Main Module of the app.*/
val appModule = module {
    
    includes(
        networkModule,
        viewModelModule
    )
    
}