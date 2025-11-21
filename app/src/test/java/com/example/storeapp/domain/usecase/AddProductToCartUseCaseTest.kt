package com.example.storeapp.domain.usecase

import com.example.storeapp.domain.model.ProductCart
import com.example.storeapp.domain.model.ProductCartBuilder
import com.example.storeapp.domain.repository.StoreRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class AddProductToCartUseCaseTest {

    @RelaxedMockK
    private lateinit var repository: StoreRepository

    private lateinit var addProductToCartUseCase: AddProductToCartUseCase

    @Before
    fun onBefore() {

        MockKAnnotations.init(this)

        this.addProductToCartUseCase = AddProductToCartUseCase(repository = repository)
    }

    @Test
    fun should_returnProductId_when_productIsAddedToCart() = runTest {

        val productTest: ProductCart = ProductCartBuilder().build()
        val resultExpected = 1L

        coEvery { repository.addProductToCart(product = productTest) } returns resultExpected

        val result: Long = addProductToCartUseCase.invoke(product = productTest)

        // This tes pass if:
        // 1.- repository.addProductToCart() is called exactly once
        // 2.- the result is the expected one
        coVerify(exactly = 1) { repository.addProductToCart(product = productTest) }
        assertThat(result).isEqualTo(resultExpected)
    }

    @Test
    fun should_throwException_when_repositoryThrowsException() = runTest {

        val productTest: ProductCart = ProductCartBuilder().build()
        val messageExpected = "Error test"
        val expectedException = RuntimeException(messageExpected)

        coEvery { repository.addProductToCart(product = productTest) } throws expectedException

        var thrown: Throwable? = null

        try {

            addProductToCartUseCase.invoke(product = productTest)
        } catch (e: Throwable) {

            thrown = e
        }

        // This tes pass if:
        // 1.- thrown is NOT null and is a instance of RuntimeException
        // 2.- thrown contains the message expected
        // 3.- repository.addProductToCart is called once time
        assertThat(thrown).isNotNull()
        assertThat(thrown).isInstanceOf(RuntimeException::class.java)
        assertThat(thrown).hasMessageThat().isEqualTo(messageExpected)
        coVerify(exactly = 1) { repository.addProductToCart(product = productTest) }
    }
}