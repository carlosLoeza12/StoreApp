package com.example.storeapp.presentation.ui.productDetail

import com.example.storeapp.domain.usecase.AddProductToCartUseCase
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

class ProductDetailViewModelTestRule : TestWatcher() {

    val testDispatcher: TestDispatcher = StandardTestDispatcher()

    val addProductToCartUseCase: AddProductToCartUseCase = mockk(relaxed = true)

    lateinit var productDetailViewModel: ProductDetailViewModel

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

        this.productDetailViewModel = ProductDetailViewModel(
            addProductToCartUseCase = this.addProductToCartUseCase,
            dispatcher = this.testDispatcher
        )
    }
}