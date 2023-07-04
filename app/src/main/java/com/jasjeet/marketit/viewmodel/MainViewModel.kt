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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    private val repository: AppRepository,
    private val ioDispatcher: CoroutineDispatcher,
    private val defaultDispatcher: CoroutineDispatcher,
    private val mainDispatcher: CoroutineDispatcher
) : ViewModel() {
    
    private val errorFlow = MutableStateFlow<ResponseError?>(null)
    private val listingsFlow = MutableStateFlow<ListingData?>(null)
    private val statusFlow = MutableStateFlow(Resource.Status.LOADING)
    
    val uiState = createUiStateFlow().asLiveData()
    
    init {
        // Fetching data for the first time automatically.
        fetchListings()
    }
    
    private fun createUiStateFlow(): StateFlow<ListingsUiState> {
        return combine(
            listingsFlow,
            errorFlow,
            statusFlow
        ){ listings: ListingData?, error: ResponseError?, status: Resource.Status ->
            return@combine ListingsUiState(listings, error, status)
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            ListingsUiState()
        )
    }
    
    fun fetchListings() =
        viewModelScope.launch(ioDispatcher) {
            // Emitting loading status
            withContext(defaultDispatcher){ statusFlow.emit(Resource.Status.LOADING) }
            
            val result = repository.getData()
            
            withContext(defaultDispatcher){
                when (result.status) {
                    Resource.Status.FAILED -> {
                        statusFlow.emit(Resource.Status.FAILED)
                        errorFlow.emit(result.error)
                    }
                    Resource.Status.SUCCESS -> {
                        statusFlow.emit(Resource.Status.SUCCESS)
                        listingsFlow.emit(result.data)
                    }
                    else -> Unit
                }
            }
        }
    
    /** To be used to cleanup error when shown.*/
    fun clearError() =
        viewModelScope.launch {
            errorFlow.emit(null)
        }
    
    fun searchForItem(query: String, onSearchComplete: (ListingData) -> Unit) {
        viewModelScope.launch(defaultDispatcher) {
    
            if (query.isEmpty()){ onSearchComplete(ListingData()) }
            
            val searchArea = uiState.value?.listings ?: return@launch
            val result = ListingData()
            
            // Simple substring search.
            searchArea.forEach { item ->
                if (item.product_name.contains(query))
                    result.add(item)
            }
    
            // Notifying the recycler view.
            withContext(mainDispatcher){
                onSearchComplete(result)
            }
            
        }
    }
    
}