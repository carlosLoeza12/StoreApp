package com.example.storeapp.domain.usecase

import com.example.storeapp.domain.model.ProductCart
import com.example.storeapp.domain.model.ProductCartBuilder
import com.example.storeapp.domain.repository.StoreRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetProductsCartUseCaseTest {

    @RelaxedMockK
    private lateinit var repository: StoreRepository

    private lateinit var getProductsCartUseCase: GetProductsCartUseCase

    @Before
    fun onBefore() {

        MockKAnnotations.init(this)

        this.getProductsCartUseCase = GetProductsCartUseCase(repository = repository)
    }

    @Test
    fun should_return_flowList_when_repository_is_called() = runTest {

        val productCart: ProductCart = ProductCartBuilder().build()
        val expectedProductList: List<ProductCart> = listOf(productCart)
        val listExpected: Flow<List<ProductCart>> = flowOf(expectedProductList)

        every { repository.getProductsCart() } returns listExpected

        val result: Flow<List<ProductCart>> = getProductsCartUseCase.invoke()

        // This test pass if:
        // 1.- the repository.getProductsCart is called once time
        // 2.- the result emitted is equal to expectedProductList
        verify(exactly = 1) { repository.getProductsCart() }
        assertThat(result.first()).isEqualTo(expectedProductList)
    }

    @Test
    fun should_throwException_when_repositoryThrowsException() {

        val messageExpected = "Error test"
        val expectedException = RuntimeException(messageExpected)

        every { repository.getProductsCart() } throws expectedException

        var thrown: Throwable? = null

        try {

           getProductsCartUseCase.invoke()
        } catch (e: Throwable) {

            thrown = e
        }

        // This tes pass if:
        // 1.- thrown is NOT null and is a instance of RuntimeException
        // 2.- thrown contains the message expected
        // 3.- repository.getProductsCart() is called once time
        assertThat(thrown).isNotNull()
        assertThat(thrown).isInstanceOf(RuntimeException::class.java)
        assertThat(thrown).hasMessageThat().isEqualTo(messageExpected)
        coVerify(exactly = 1) { repository.getProductsCart() }
    }
}