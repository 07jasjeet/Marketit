package com.jasjeet.marketit.dependencies

import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dispatcherModule = module {
    
    single(named("IO")) {
        Dispatchers.IO
    }
    
    single(named("Default")) {
        Dispatchers.Default
    }
    
    single(named("Main")) {
        Dispatchers.Main
    }
}