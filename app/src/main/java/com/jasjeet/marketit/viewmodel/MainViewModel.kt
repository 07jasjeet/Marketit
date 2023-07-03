package com.jasjeet.marketit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.jasjeet.marketit.model.ListingData
import com.jasjeet.marketit.model.ListingsUiState
import com.jasjeet.marketit.model.ResponseError
import com.jasjeet.marketit.repository.AppRepository
import com.jasjeet.marketit.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    private val repository: AppRepository,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    
    private val errorFlow = MutableStateFlow<ResponseError?>(null)
    private val listingsFlow = MutableStateFlow<ListingData?>(null)
    
    val uiState = createUiStateFlow().asLiveData()
    
    init {
        viewModelScope.launch {
            fetchListings()
        }
    }
    
    private fun createUiStateFlow(): StateFlow<ListingsUiState> {
        return combine(
            listingsFlow,
            errorFlow
        ){ listings: ListingData?, error: ResponseError? ->
            return@combine ListingsUiState(listings, error)
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            ListingsUiState(null, null)
        )
    }
    
    suspend fun fetchListings() = withContext(ioDispatcher){
        val result = repository.getData()
        when (result.status){
            Resource.Status.FAILED -> errorFlow.emit(result.error)
            Resource.Status.SUCCESS -> listingsFlow.emit(result.data)
            else -> Unit
        }
    }
    
    
    
}