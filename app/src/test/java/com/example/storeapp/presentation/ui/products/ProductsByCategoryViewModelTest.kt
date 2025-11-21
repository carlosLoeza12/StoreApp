package com.example.storeapp.presentation.ui.products

import com.example.storeapp.core.util.ResponseResult
import com.example.storeapp.domain.model.StoreProduct
import com.example.storeapp.domain.model.StoreProductBuilder
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class ProductsByCategoryViewModelTest {

    @get:Rule
    val rule = ProductsByCategoryViewModelTestRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getProductsByCategory_emits_loading() = runTest(this.rule.testDispatcher) {

        rule.initViewModel()

        assertThat(rule.productsByCategoryViewModel.productsState.value).isEqualTo(ResponseResult.Loading)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getProductsByCategory_emits_loading_then_success_when_returns_data() = runTest(this.rule.testDispatcher) {

        val categoryIdTest = 1
        val storeProductsList: List<StoreProduct> = listOf(StoreProductBuilder().build())

        coEvery { rule.getStoreProductsByCategoryUseCase(categoryId = categoryIdTest) } returns storeProductsList

        val emittedStates: MutableList<ResponseResult<List<StoreProduct>>> = mutableListOf()

        val job: Job = launch {

            rule.productsByCategoryViewModel.productsState.collect {

                emittedStates.add(it)
            }
        }

        rule.initViewModel()

        rule.productsByCategoryViewModel.getProductsByCategory(categoryId = categoryIdTest)

        advanceUntilIdle()

        assertThat(emittedStates.size).isEqualTo(2)
        assertThat(emittedStates[0]).isEqualTo(ResponseResult.Loading)
        assertThat(emittedStates[1]).isEqualTo(ResponseResult.Success(storeProductsList))

        job.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getProductsByCategory_emits_loading_then_success_when_returns_data_same_categoryId() = runTest(this.rule.testDispatcher) {

        val categoryIdTest = 1
        val storeProductsList: List<StoreProduct> = listOf(StoreProductBuilder().build())

        coEvery { rule.getStoreProductsByCategoryUseCase(categoryId = categoryIdTest) } returns storeProductsList

        val emittedStates: MutableList<ResponseResult<List<StoreProduct>>> = mutableListOf()

        val job: Job = launch {

            rule.productsByCategoryViewModel.productsState.collect {

                emittedStates.add(it)
            }
        }

        rule.initViewModel()

        // first call
        rule.productsByCategoryViewModel.getProductsByCategory(categoryId = categoryIdTest)

        // second call
        rule.productsByCategoryViewModel.getProductsByCategory(categoryId = categoryIdTest)

        advanceUntilIdle()

        assertThat(emittedStates.size).isEqualTo(2)
        assertThat(emittedStates[0]).isEqualTo(ResponseResult.Loading)
        assertThat(emittedStates[1]).isEqualTo(ResponseResult.Success(storeProductsList))

        // The request should not be executed again on the second call since we have the same productId
        coVerify(exactly = 1) { rule.getStoreProductsByCategoryUseCase(categoryIdTest) }

        job.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getProductsByCategory_emits_loading_then_error_when_useCase_throws_exception() = runTest{

        val categoryIdTest = 1
        val errorMessage = "Network error"

        coEvery { rule.getStoreProductsByCategoryUseCase(categoryId = categoryIdTest) } throws Exception(errorMessage)

        val emittedStates: MutableList<ResponseResult<List<StoreProduct>>> = mutableListOf()

        val job: Job = launch {

            rule.productsByCategoryViewModel.productsState.collect { result->

                emittedStates.add(result)
            }
        }

        rule.initViewModel()

        rule.productsByCategoryViewModel.getProductsByCategory(categoryId = categoryIdTest)

        advanceUntilIdle()

        assertThat(emittedStates.size).isEqualTo(2)
        assertThat(emittedStates[0]).isEqualTo(ResponseResult.Loading)
        assertThat(emittedStates[1]).isEqualTo(ResponseResult.Error(errorMessage))

        job.cancel()
    }
}