package com.example.storeapp.domain.usecase

import com.example.storeapp.domain.model.StoreCategory
import com.example.storeapp.domain.model.StoreCategoryBuilder
import com.example.storeapp.domain.repository.StoreRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetStoreCategoriesUseCaseTest {

    @RelaxedMockK
    private lateinit var repository: StoreRepository

    private lateinit var getStoreCategoriesUseCase: GetStoreCategoriesUseCase

    @Before
    fun onBefore() {

        MockKAnnotations.init(this)

        this.getStoreCategoriesUseCase = GetStoreCategoriesUseCase(repository = this.repository)
    }

    @Test
    fun invoke_should_return_list_of_store_categories_from_repository() = runTest {

        val expectedCategories: List<StoreCategory> = listOf(
            StoreCategoryBuilder().id(1).slug("slug01").name("name01").build(),
            StoreCategoryBuilder().id(2).slug("slug02").name("name02").build()
        )

        coEvery { repository.getStoreCategories() } returns expectedCategories

        val result: List<StoreCategory> = getStoreCategoriesUseCase.invoke()

        // This tes pass if:
        // 1.- repository.getStoreCategories() is called exactly once.
        // 2.- Calling repository.getStoreCategories() returns the expected categories.
        coVerify(exactly = 1) { repository.getStoreCategories() }
        assertThat(result).isEqualTo(expectedCategories)
    }

    @Test
    fun invoke_should_return_a_empty_list_of_storeCategories() = runTest {

        coEvery { repository.getStoreCategories() } returns emptyList()

        val result: List<StoreCategory> = getStoreCategoriesUseCase.invoke()

        // This tes pass if:
        // 1.- repository.getStoreCategories() is called exactly once.
        // 2.- Calling repository.getStoreCategories() returns a empty list
        coVerify(exactly = 1) { repository.getStoreCategories() }
        assertThat(result).isEmpty()
    }
}