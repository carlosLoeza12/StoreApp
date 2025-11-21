package com.example.storeapp.domain.usecase

import com.example.storeapp.domain.model.StoreProduct
import com.example.storeapp.domain.model.StoreProductBuilder
import com.example.storeapp.domain.repository.StoreRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetStoreProductsByCategoryUseCaseTest {

    @RelaxedMockK
    private lateinit var repository: StoreRepository

    private lateinit var getStoreProductsByCategoryUseCase: GetStoreProductsByCategoryUseCase

    @Before
    fun onBefore() {

        MockKAnnotations.init(this)

        this.getStoreProductsByCategoryUseCase = GetStoreProductsByCategoryUseCase(repository = this.repository)
    }

    @Test
    fun should_return_products_by_categories_when_repository_is_called() = runTest {

        val categoryIdTest = 1
        val productsExpected: List<StoreProduct> = listOf(StoreProductBuilder().build())

        coEvery { repository.getProductsByCategory(categoryIdTest) } returns productsExpected

        val result: List<StoreProduct> = getStoreProductsByCategoryUseCase(categoryIdTest)

        // This test pass if:
        // 1.- repository.getProductsByCategory() is called exactly once.
        // 2.- Calling repository.getProductsByCategory() returns the expected products.
        coVerify(exactly = 1) { repository.getProductsByCategory(categoryIdTest) }
        assertThat(result).isEqualTo(productsExpected)
    }

    @Test
    fun should_throwException_when_repositoryThrowsException() = runTest {

        val categoryIdTest = 1
        val messageExpected = "Error test"
        val expectedException = RuntimeException(messageExpected)

        coEvery { repository.getProductsByCategory(categoryIdTest) } throws expectedException

        var thrown: Throwable? = null

        try {

            getStoreProductsByCategoryUseCase.invoke(categoryIdTest)
        } catch (e: Throwable) {

            thrown = e
        }

        // This test pass if:
        // 1.- thrown is NOT null and is a instance of RuntimeException
        // 2.- thrown contains the message expected
        // 3.- repository.getProductsByCategory is called once time
        assertThat(thrown).isNotNull()
        assertThat(thrown).isInstanceOf(RuntimeException::class.java)
        assertThat(thrown).hasMessageThat().isEqualTo(messageExpected)
        coVerify(exactly = 1) { repository.getProductsByCategory(categoryIdTest) }
    }
}