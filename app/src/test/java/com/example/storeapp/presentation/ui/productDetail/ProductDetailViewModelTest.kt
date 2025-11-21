package com.example.storeapp.presentation.ui.productDetail

import com.example.storeapp.core.util.ResponseResult
import com.example.storeapp.domain.model.ProductCart
import com.example.storeapp.domain.model.ProductCartBuilder
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class ProductDetailViewModelTest {

    @get:Rule
    val rule = ProductDetailViewModelTestRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getProductsByCategory_emits_loading() = runTest(this.rule.testDispatcher) {

        rule.initViewModel()

        assertThat(rule.productDetailViewModel.productsDetailState.value).isEqualTo(ResponseResult.Loading)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun addProductToCart_emmit_loading_then_success_when_the_product_is_added_to_cart() = runTest(this.rule.testDispatcher) {

        val productCartTest: ProductCart = ProductCartBuilder().build()
        val productIdExpected = 1L

        coEvery { rule.addProductToCartUseCase(product = productCartTest) } returns productIdExpected

        val emittedStates: MutableList<ResponseResult<Long>> = mutableListOf()

        val job: Job = launch {

            rule.productDetailViewModel.productsDetailState.collect{

                emittedStates.add(it)
            }
        }

        rule.initViewModel()

        rule.productDetailViewModel.addProductToCart(productCart = productCartTest)

        advanceUntilIdle()

        assertThat(emittedStates.size).isEqualTo(2)
        assertThat(emittedStates[0]).isEqualTo(ResponseResult.Loading)
        assertThat(emittedStates[1]).isEqualTo(ResponseResult.Success(productIdExpected))

        job.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun addProductToCart_emmit_loading_then_error_when_the_product_is_not_added_to_cart() = runTest(this.rule.testDispatcher) {

        val productCartTest: ProductCart = ProductCartBuilder().build()
        val productIdExpected = 0L

        coEvery { rule.addProductToCartUseCase(product = productCartTest) } returns productIdExpected

        val emittedStates: MutableList<ResponseResult<Long>> = mutableListOf()

        val job: Job = launch {

            rule.productDetailViewModel.productsDetailState.collect{

                emittedStates.add(it)
            }
        }

        rule.initViewModel()

        rule.productDetailViewModel.addProductToCart(productCart = productCartTest)

        advanceUntilIdle()

        assertThat(emittedStates.size).isEqualTo(2)
        assertThat(emittedStates[0]).isEqualTo(ResponseResult.Loading)
        assertThat(emittedStates[1]).isEqualTo(ResponseResult.Error("An unexpected error occurred"))

        job.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun addProductToCart_emmit_loading_then_error_when_the_useCase_throws_exception() = runTest(this.rule.testDispatcher) {

        val productCartTest: ProductCart = ProductCartBuilder().build()
        val messageErrorTest = "Error to add product"

        coEvery { rule.addProductToCartUseCase(product = productCartTest) } throws Exception(messageErrorTest)

        val emittedStates: MutableList<ResponseResult<Long>> = mutableListOf()

        val job: Job = launch {

            rule.productDetailViewModel.productsDetailState.collect{

                emittedStates.add(it)
            }
        }

        rule.initViewModel()

        rule.productDetailViewModel.addProductToCart(productCart = productCartTest)

        advanceUntilIdle()

        assertThat(emittedStates.size).isEqualTo(2)
        assertThat(emittedStates[0]).isEqualTo(ResponseResult.Loading)
        assertThat(emittedStates[1]).isEqualTo(ResponseResult.Error(messageErrorTest))

        job.cancel()
    }
}