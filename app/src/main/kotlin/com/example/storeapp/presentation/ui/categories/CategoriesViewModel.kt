package com.example.storeapp.presentation.ui.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.core.util.ResponseResult
import com.example.storeapp.di.IoDispatcher
import com.example.storeapp.domain.model.StoreCategory
import com.example.storeapp.domain.usecase.GetStoreCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val getStoreCategoriesUseCase: GetStoreCategoriesUseCase,
    @param:IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _storeState: MutableStateFlow<ResponseResult<List<StoreCategory>>> = MutableStateFlow(ResponseResult.Loading)
    val storeState: StateFlow<ResponseResult<List<StoreCategory>>> = _storeState

    init {

        this.getStoreCategories()
    }

    private fun getStoreCategories() {

        viewModelScope.launch {

            _storeState.value = ResponseResult.Loading

            try {

                withContext(context = dispatcher) {

                    _storeState.value = ResponseResult.Success(getStoreCategoriesUseCase.invoke())
                }
            } catch (e: Exception) {

                _storeState.value = ResponseResult.Error(e.localizedMessage ?: "An unexpected error occurred")
            }
        }
    }
}