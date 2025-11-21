package com.example.storeapp.presentation.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.core.util.ResponseResult
import com.example.storeapp.di.IoDispatcher
import com.example.storeapp.domain.model.ProductCart
import com.example.storeapp.domain.usecase.DeleteProductCartUseCase
import com.example.storeapp.domain.usecase.GetProductsCartUseCase
import com.example.storeapp.domain.usecase.UpdateAmountForProductUseCase
import com.example.storeapp.domain.usecase.UpdateProductsAsPurchasedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProductsCartViewModel @Inject constructor(
    private val getProductsCartUseCase: GetProductsCartUseCase,
    private val updateAmountForProductUseCase: UpdateAmountForProductUseCase,
    private val deleteProductCartUseCase: DeleteProductCartUseCase,
    private val updateProductsAsPurchasedUseCase: UpdateProductsAsPurchasedUseCase,
    @param:IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _updateProductState: MutableStateFlow<ResponseResult<Int>> = MutableStateFlow(ResponseResult.Loading)
    val updateProductState: StateFlow<ResponseResult<Int>> = this._updateProductState

    private val _deleteProductState: MutableStateFlow<ResponseResult<Int>> = MutableStateFlow(ResponseResult.Loading)
    val deleteProductState: StateFlow<ResponseResult<Int>> = this._deleteProductState

    private val _purchaseCartState: MutableStateFlow<ResponseResult<Int>> = MutableStateFlow(ResponseResult.Loading)
    val purchaseCartState: StateFlow<ResponseResult<Int>> = this._purchaseCartState

    val productsCart: StateFlow<ResponseResult<List<ProductCart>>> =
        getProductsCartUseCase.invoke()
            .map<List<ProductCart>, ResponseResult<List<ProductCart>>> { list: List<ProductCart> ->

                ResponseResult.Success(value = list)
            }
            .onStart { emit(value = ResponseResult.Loading) }
            .catch { e -> emit(value = ResponseResult.Error(e.message ?: "Unknown error")) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = ResponseResult.Loading
            )

    val cartTotal: StateFlow<Double> = productsCart.map { result ->

        when (result) {

            is ResponseResult.Success -> result.value.sumOf { it.price * it.amountInCart }

            else -> 0.0
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0.0
    )

    fun updateProductCart(productId: Int, amount: Int) {

        viewModelScope.launch {

            try {

                _updateProductState.value = ResponseResult.Loading

                val isUpdated: Int = withContext(context = dispatcher) {

                    updateAmountForProductUseCase.invoke(productId, amount)
                }

                if(isUpdated > 0) {

                    _updateProductState.value = ResponseResult.Success(isUpdated)
                } else {

                    _updateProductState.value = ResponseResult.Error("An unexpected error occurred while updating product amount")
                }
            } catch (e: Exception) {

                _updateProductState.value = ResponseResult.Error(e.localizedMessage ?: "An unexpected error occurred while updating product amount")
            }
        }
    }

    fun deleteProductCart(productId: Int) {

        viewModelScope.launch {

            try {

                _deleteProductState.value = ResponseResult.Loading

                val isDeleted: Int =  withContext(context = dispatcher) {

                   deleteProductCartUseCase.invoke(productId)
                }

                if(isDeleted > 0) {

                    _deleteProductState.value = ResponseResult.Success(isDeleted)
                } else {

                    _deleteProductState.value = ResponseResult.Error("An unexpected error occurred while deleting product")
                }
            } catch (e: Exception) {

                _deleteProductState.value = ResponseResult.Error(e.localizedMessage ?: "An unexpected error occurred while deleting product")
            }
        }
    }

    fun updateProductsAsPurchase(products: List<ProductCart>) {

        if (products.isNotEmpty()) {

            viewModelScope.launch {

                try {

                    _purchaseCartState.value = ResponseResult.Loading

                    val isUpdated: Int = withContext(context = dispatcher) {

                        updateProductsAsPurchasedUseCase.invoke(products.map(ProductCart::productId))
                    }

                    if(isUpdated > 0) {

                        _purchaseCartState.value = ResponseResult.Success(isUpdated)
                    } else {

                        _purchaseCartState.value = ResponseResult.Error("An unexpected error occurred during purchase")
                    }
                } catch (e: Exception) {

                    _purchaseCartState.value = ResponseResult.Error(e.localizedMessage ?: "An unexpected error occurred during purchase")
                }
            }
        }
    }
}
