package com.example.storeapp.presentation.ui.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.core.util.ResponseResult
import com.example.storeapp.di.IoDispatcher
import com.example.storeapp.domain.model.StoreProduct
import com.example.storeapp.domain.usecase.GetStoreProductsByCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProductsByCategoryViewModel @Inject constructor(
    private val getStoreProductsByCategoryUseCase: GetStoreProductsByCategoryUseCase,
    @param:IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _productsState: MutableStateFlow<ResponseResult<List<StoreProduct>>> = MutableStateFlow(ResponseResult.Loading)
    val productsState: StateFlow<ResponseResult<List<StoreProduct>>> = _productsState

    private var currentCategoryId: Int? = null

    fun getProductsByCategory(categoryId: Int) {

        if (currentCategoryId != categoryId) {

            this.currentCategoryId = categoryId

            viewModelScope.launch {

                _productsState.value = ResponseResult.Loading

                try {

                    withContext(dispatcher) {

                        _productsState.value = ResponseResult.Success(getStoreProductsByCategoryUseCase.invoke(categoryId))
                    }
                } catch (e: Exception) {

                    _productsState.value = ResponseResult.Error(e.localizedMessage ?: "An unexpected error occurred")
                }
            }
        }
    }
}