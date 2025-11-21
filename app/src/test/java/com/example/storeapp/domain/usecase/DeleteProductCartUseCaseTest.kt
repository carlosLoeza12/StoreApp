package com.example.storeapp.domain.usecase

import com.example.storeapp.domain.repository.StoreRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.test.runTest
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class DeleteProductCartUseCaseTest {

    @RelaxedMockK
    private lateinit var repository: StoreRepository

    private lateinit var deleteProductCartUseCase: DeleteProductCartUseCase

    @Before
    fun onBefore() {

        MockKAnnotations.init(this)

        this.deleteProductCartUseCase = DeleteProductCartUseCase(repository = repository)
    }

    @Test
    fun should_return_rows_affected_when_we_delete_one_product() = runTest {

        val productIdTest = 10
        val rowsAffectedTests = 1

        coEvery { repository.deleteProductFromCart(productId = productIdTest) } returns rowsAffectedTests

        val result: Int = deleteProductCartUseCase(productId = productIdTest)

        // This test if pass:
        // 1.- repository.deleteProductFromCart is called once time
        // 2.- the result is equal to rows affected
        coVerify(exactly = 1) { repository.deleteProductFromCart(productId = productIdTest) }
        assertThat(result).isEqualTo(rowsAffectedTests)
    }

    @Test
    fun should_throwException_when_repositoryThrowsException() = runTest {

        val productIdTest = 1
        val messageExpected = "Error test"
        val expectedException = RuntimeException(messageExpected)

        coEvery { repository.deleteProductFromCart(productId = productIdTest) } throws expectedException

        var thrown: Throwable? = null

        try {

            deleteProductCartUseCase.invoke(productId = productIdTest)
        } catch (e: Throwable) {

            thrown = e
        }

        // This tes pass if:
        // 1.- thrown is NOT null and is a instance of RuntimeException
        // 2.- thrown contains the message expected
        // 3.- repository.deleteProductFromCart is called once time
        assertThat(thrown).isNotNull()
        assertThat(thrown).isInstanceOf(RuntimeException::class.java)
        assertThat(thrown).hasMessageThat().isEqualTo(messageExpected)
        coVerify(exactly = 1) { repository.deleteProductFromCart(productId = productIdTest) }
    }
}