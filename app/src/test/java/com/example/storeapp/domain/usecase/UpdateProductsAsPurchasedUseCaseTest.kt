package com.example.storeapp.domain.usecase

import com.example.storeapp.domain.repository.StoreRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UpdateProductsAsPurchasedUseCaseTest {

    @RelaxedMockK
    private lateinit var repository: StoreRepository

    private lateinit var updateProductsAsPurchasedUseCase: UpdateProductsAsPurchasedUseCase

    @Before
    fun onBefore() {

        MockKAnnotations.init(this)

        updateProductsAsPurchasedUseCase = UpdateProductsAsPurchasedUseCase(repository = this.repository)
    }

    @Test
    fun should_return_rows_affected_when_updated_the_products_to_purchased() = runTest {

        val productsId: List<Int> = listOf(1, 2, 3)
        val rowsAffectedTests = 1

        coEvery { repository.updateProductsAsPurchased(productsId) } returns rowsAffectedTests

        val result = updateProductsAsPurchasedUseCase.invoke(productsId = productsId)

        // This test pass if:
        // 1.- repository.updateProductsAsPurchased is called once time
        // 2.- the result is equal to rows affected
        coVerify(exactly = 1) { repository.updateProductsAsPurchased(productsId) }
        assertThat(result).isEqualTo(rowsAffectedTests)
    }

    @Test
    fun should_throwException_when_repositoryThrowsException() = runTest {

        val productsId: List<Int> = listOf(1, 2, 3)
        val messageExpected = "Error test"
        val expectedException = RuntimeException(messageExpected)

        coEvery { repository.updateProductsAsPurchased(productsId) } throws expectedException

        var thrown: Throwable? = null

        try {

            updateProductsAsPurchasedUseCase.invoke(productsId)
        } catch (e: Throwable) {

            thrown = e
        }

        // This test pass if:
        // 1.- thrown is NOT null and is a instance of RuntimeException
        // 2.- thrown contains the message expected
        // 3.- repository.updateProductsAsPurchased is called once time
        assertThat(thrown).isNotNull()
        assertThat(thrown).isInstanceOf(RuntimeException::class.java)
        assertThat(thrown).hasMessageThat().isEqualTo(messageExpected)
        coVerify(exactly = 1) { repository.updateProductsAsPurchased(productsId) }
    }
}