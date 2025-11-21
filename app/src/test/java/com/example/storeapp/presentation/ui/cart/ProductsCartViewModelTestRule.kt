package com.example.storeapp.presentation.ui.cart

import com.example.storeapp.domain.usecase.DeleteProductCartUseCase
import com.example.storeapp.domain.usecase.GetProductsCartUseCase
import com.example.storeapp.domain.usecase.UpdateAmountForProductUseCase
import com.example.storeapp.domain.usecase.UpdateProductsAsPurchasedUseCase
import io.mockk.MockKAnnotations
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class ProductsCartViewModelTestRule: TestWatcher() {

    val testDispatcher: TestDispatcher = StandardTestDispatcher()

    val getProductsCartUseCase: GetProductsCartUseCase = mockk(relaxed = true)
    val updateAmountForProductUseCase: UpdateAmountForProductUseCase = mockk(relaxed = true)
    val deleteProductCartUseCase: DeleteProductCartUseCase = mockk(relaxed = true)
    val updateProductsAsPurchasedUseCase: UpdateProductsAsPurchasedUseCase = mockk(relaxed = true)

    lateinit var productsCartViewModel: ProductsCartViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun starting(description: Description?) {
        super.starting(description)

        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun finished(description: Description?) {
        super.finished(description)

        Dispatchers.resetMain()
    }

    fun initViewModel() {

        this.productsCartViewModel = ProductsCartViewModel(
            getProductsCartUseCase = this.getProductsCartUseCase,
            updateAmountForProductUseCase = this.updateAmountForProductUseCase,
            deleteProductCartUseCase = this.deleteProductCartUseCase,
            updateProductsAsPurchasedUseCase = this.updateProductsAsPurchasedUseCase,
            dispatcher = this.testDispatcher
        )
    }
}