package com.example.storeapp.presentation.ui.productDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.core.util.ResponseResult
import com.example.storeapp.di.IoDispatcher
import com.example.storeapp.domain.model.ProductCart
import com.example.storeapp.domain.usecase.AddProductToCartUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val addProductToCartUseCase: AddProductToCartUseCase,
    @param:IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _productsDetailState: MutableStateFlow<ResponseResult<Long>> = MutableStateFlow(ResponseResult.Loading)
    val productsDetailState: StateFlow<ResponseResult<Long>> = _productsDetailState

    fun addProductToCart(productCart: ProductCart) {

        viewModelScope.launch {

            try {

                _productsDetailState.value = ResponseResult.Loading

                val isAdded: Long = withContext(context = dispatcher) {

                    addProductToCartUseCase.invoke(product = productCart)
                }

                if(isAdded > 0) {

                    _productsDetailState.value = ResponseResult.Success(isAdded)
                }  else {

                    _productsDetailState.value = ResponseResult.Error("An unexpected error occurred")
                }
            } catch (e: Exception) {

                _productsDetailState.value = ResponseResult.Error(e.localizedMessage ?: "An unexpected error occurred")
            }
        }
    }
}