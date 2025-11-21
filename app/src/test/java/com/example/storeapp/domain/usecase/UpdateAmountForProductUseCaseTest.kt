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

class UpdateAmountForProductUseCaseTest {

    @RelaxedMockK
    private lateinit var repository: StoreRepository

    private lateinit var updateAmountForProductUseCase: UpdateAmountForProductUseCase

    @Before
    fun onBefore() {

        MockKAnnotations.init(this)

        updateAmountForProductUseCase = UpdateAmountForProductUseCase(repository)
    }

    @Test
    fun should_return_rows_affected_when_updated_the_amount_product_for_cart() = runTest{

        val productIdTest = 10
        val rowsAffectedTests = 1
        val amountTest = 5

        coEvery { repository.updateAmountProductCart(productId = productIdTest, amount = amountTest) } returns rowsAffectedTests

        val result: Int = updateAmountForProductUseCase(productId = productIdTest, amount = amountTest)

        // This test pass if:
        // 1.- repository.updateAmountProductCart is called once time
        // 2.- the result is equal to rowsAffectedTests
        coVerify(exactly = 1) { repository.updateAmountProductCart(productId = productIdTest, amount = amountTest) }
        assertThat(result).isEqualTo(rowsAffectedTests)
    }

    @Test
    fun should_throwException_when_repositoryThrowsException() = runTest {

        val productIdTest = 10
        val amountTest = 5
        val messageExpected = "Error test"
        val expectedException = RuntimeException(messageExpected)

        coEvery { repository.updateAmountProductCart(productId = productIdTest, amount = amountTest) } throws expectedException

        var thrown: Throwable? = null

        try {

            updateAmountForProductUseCase.invoke(productId = productIdTest, amount = amountTest)
        } catch (e: Throwable) {

            thrown = e
        }

        // This test pass if:
        // 1.- thrown is NOT null and is a instance of RuntimeException
        // 2.- thrown contains the message expected
        // 3.- repository.updateAmountProductCart is called once time
        assertThat(thrown).isNotNull()
        assertThat(thrown).isInstanceOf(RuntimeException::class.java)
        assertThat(thrown).hasMessageThat().isEqualTo(messageExpected)
        coVerify(exactly = 1) { repository.updateAmountProductCart(productIdTest, amountTest) }
    }
}