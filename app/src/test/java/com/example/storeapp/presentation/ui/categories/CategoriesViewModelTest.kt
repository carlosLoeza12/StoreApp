package com.example.storeapp.presentation.ui.categories

import com.example.storeapp.core.util.ResponseResult
import com.example.storeapp.domain.model.StoreCategory
import com.example.storeapp.domain.model.StoreCategoryBuilder
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class CategoriesViewModelTest {

    @get:Rule
    val rule = CategoriesViewModelTestRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getStoreCategories_emits_loading_then_success_when_useCase_returns_data() = runTest(this.rule.testDispatcher) {

        val storeCategoriesExpected: List<StoreCategory> = listOf(StoreCategoryBuilder().build())

        coEvery { rule.getCategoriesUseCase.invoke() } returns storeCategoriesExpected

        val emittedStates: MutableList<ResponseResult<List<StoreCategory>>> = mutableListOf()

        val job = launch {

            rule.categoriesViewModel.storeState.collect { result->

                emittedStates.add(result)
            }
        }

        rule.categoriesViewModel = CategoriesViewModel(
            getStoreCategoriesUseCase = rule.getCategoriesUseCase,
            dispatcher = rule.testDispatcher
        )

        advanceUntilIdle()

        assertThat(emittedStates[0]).isEqualTo(ResponseResult.Loading)
        assertThat(emittedStates[1]).isEqualTo(ResponseResult.Success(storeCategoriesExpected))

        job.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getStoreCategories_emits_loading_then_error_when_useCase_throws_exception() = runTest(this.rule.testDispatcher) {

        val errorMessage = "Network error"

        coEvery { rule.getCategoriesUseCase.invoke() } throws Exception(errorMessage)

        val emittedStates: MutableList<ResponseResult<List<StoreCategory>>> = mutableListOf()

        val job = launch {

            rule.categoriesViewModel.storeState.collect { result->

                emittedStates.add(result)
            }
        }

        rule.categoriesViewModel = CategoriesViewModel(
            getStoreCategoriesUseCase = rule.getCategoriesUseCase,
            dispatcher = rule.testDispatcher
        )

        advanceUntilIdle()

        assertThat(emittedStates[0]).isEqualTo(ResponseResult.Loading)
        assertThat(emittedStates[1]).isEqualTo(ResponseResult.Error(errorMessage))

        job.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getStoreCategories_emits_loading() = runTest(this.rule.testDispatcher) {

        rule.categoriesViewModel = CategoriesViewModel(
            getStoreCategoriesUseCase = rule.getCategoriesUseCase,
            dispatcher = rule.testDispatcher
        )

        assertThat(rule.categoriesViewModel.storeState.value).isEqualTo(ResponseResult.Loading)
    }
}