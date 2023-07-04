package com.jasjeet.marketit.dependencies

import com.jasjeet.marketit.viewmodel.AddProductViewModel
import com.jasjeet.marketit.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    
    viewModel {
        MainViewModel(
            repository = get(),
            Dispatchers.IO,
            Dispatchers.Default,
            Dispatchers.Main
        )
    }
    
    viewModel {
        AddProductViewModel(
            repository = get(),
            Dispatchers.IO,
            Dispatchers.Default
        )
    }
}