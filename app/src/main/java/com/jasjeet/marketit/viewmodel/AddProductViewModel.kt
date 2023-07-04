package com.jasjeet.marketit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.jasjeet.marketit.model.AddProductUiState
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
import java.io.File

class AddProductViewModel(
    private val repository: AppRepository,
    private val ioDispatcher: CoroutineDispatcher,
    private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {
    
    private val errorFlow = MutableStateFlow<ResponseError?>(null)
    private val statusFlow = MutableStateFlow(Resource.Status.LOADING)
    
    val uiState = createUiStateFlow().asLiveData()
    
    private fun createUiStateFlow(): StateFlow<AddProductUiState> {
        return combine(
            statusFlow,
            errorFlow
        ){ status: Resource.Status, error: ResponseError? ->
            return@combine AddProductUiState(status, error)
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            AddProductUiState()
        )
    }
    
    fun addProduct(name: String, type: String, price: String, tax:String) {
        viewModelScope.launch(ioDispatcher) {
            
            withContext(defaultDispatcher){ statusFlow.emit(Resource.Status.LOADING) }
            
            val result = repository.addProduct(name, type, price, tax, File(""))
            
            withContext(defaultDispatcher){
                statusFlow.emit(result.status)
                when (result.status) {
                    Resource.Status.FAILED -> errorFlow.emit(result.error)
                    else -> Unit
                }
            }
        }
    }
    
    fun clearError() =
        viewModelScope.launch {
            errorFlow.emit(null)
        }
    
}