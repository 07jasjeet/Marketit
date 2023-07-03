package com.jasjeet.marketit.dependencies

import com.jasjeet.marketit.viewmodel.AddProductViewModel
import com.jasjeet.marketit.viewmodel.ListingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val viewModelModule = module {
    
    viewModel {
        ListingsViewModel(get(), get(named("IO")))
    }
    
    viewModel {
        AddProductViewModel(repository = get())
    }
}