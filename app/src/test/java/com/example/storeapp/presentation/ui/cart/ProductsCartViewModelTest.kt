package com.example.storeapp.presentation.ui.cart

import com.example.storeapp.core.util.ResponseResult
import com.example.storeapp.domain.model.ProductCart
import com.example.storeapp.domain.model.ProductCartBuilder
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class ProductsCartViewModelTest {

    @get:Rule
    val rule = ProductsCartViewModelTestRule()

    // region productsCart

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun should_EmitLoading_When_ProductsCartInitializes() = runTest(this.rule.testDispatcher) {

        rule.initViewModel()

        assertThat(rule.productsCartViewModel.productsCart.value).isEqualTo(ResponseResult.Loading)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun should_EmitLoadingThenSuccess_When_GetProductsCartUseCaseReturnsData() = runTest(this.rule.testDispatcher) {

        val productsCartExpected: List<ProductCart> = listOf(ProductCartBuilder().build())

        coEvery { rule.getProductsCartUseCase.invoke() } returns flowOf(productsCartExpected)

        val emittedStates: MutableList<ResponseResult<List<ProductCart>>> = mutableListOf()

        val job: Job = launch {

            rule.productsCartViewModel.productsCart.collect{

                emittedStates.add(it)
            }
        }

        rule.initViewModel()

        advanceUntilIdle()

        assertThat(emittedStates.size).isEqualTo(2)
        assertThat(emittedStates[0]).isEqualTo(ResponseResult.Loading)
        assertThat(emittedStates[1]).isEqualTo(ResponseResult.Success(productsCartExpected))

        coVerify(exactly = 1) { rule.getProductsCartUseCase.invoke() }

        job.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun should_EmitLoadingThenError_When_GetProductsCartUseCaseThrowsException() = runTest(this.rule.testDispatcher) {

       val errorMessage = "Error"

        coEvery { rule.getProductsCartUseCase.invoke() } returns flow { throw Exception(errorMessage) }

        val emittedStates: MutableList<ResponseResult<List<ProductCart>>> = mutableListOf()

        val job: Job = launch {

            rule.productsCartViewModel.productsCart.collect{

                emittedStates.add(it)
            }
        }

        rule.initViewModel()

        advanceUntilIdle()

        assertThat(emittedStates.size).isEqualTo(2)
        assertThat(emittedStates[0]).isEqualTo(ResponseResult.Loading)
        assertThat(emittedStates[1]).isEqualTo(ResponseResult.Error(errorMessage))

        coVerify(exactly = 1) { rule.getProductsCartUseCase.invoke() }

        job.cancel()
    }

    // endregion

    // region cartTotal

    @Test
    fun should_EmitZero_When_CartTotalInitializes() = runTest {

        rule.initViewModel()

        assertThat(rule.productsCartViewModel.cartTotal.value).isEqualTo(0.0)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun should_EmitZero_When_ProductsCartIsLoading() = runTest(this.rule.testDispatcher) {

        // Given that getProductsCartUseCase returns a flow that never completes
        // This keeps productsCart in ResponseResult.Loading indefinitely (after initial emissions).
        coEvery { rule.getProductsCartUseCase.invoke() } returns flow { kotlinx.coroutines.delay(Long.MAX_VALUE) }

        rule.initViewModel()

        val emittedTotals: MutableList<Double> = mutableListOf()

        val job: Job = launch {

            rule.productsCartViewModel.cartTotal.collect { total ->

                emittedTotals.add(total)
            }
        }

        advanceUntilIdle()

        // Then the cartTotal should emit only 0.0 as productsCart is in Loading state
        assertThat(emittedTotals).containsExactly(0.0).inOrder()

        coVerify(exactly = 1) { rule.getProductsCartUseCase.invoke() }

        job.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun should_EmitCorrectTotal_When_ProductsCartHasProducts() = runTest(this.rule.testDispatcher) {

        // We simulate that the flow productsCart returns this list from the useCase
        val products: List<ProductCart> = listOf(
            ProductCartBuilder().withId(1).withPrice(10.0).withAmountInCart(2).build(),
            ProductCartBuilder().withId(2).withPrice(5.0).withAmountInCart(1).build()
        )

        coEvery { rule.getProductsCartUseCase.invoke() } returns flowOf(products)

        rule.initViewModel()

        val emittedTotals: MutableList<Double> = mutableListOf()

        val job: Job = launch {

            rule.productsCartViewModel.cartTotal.collect { total ->

                emittedTotals.add(total)
            }
        }

        advanceUntilIdle()

        // the first value is 0.0 an then 25.0 that is the sum for the product list
        assertThat(emittedTotals).containsExactly(0.0, 25.0).inOrder()

        coVerify(exactly = 1) { rule.getProductsCartUseCase.invoke() }

        job.cancel()
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun should_EmitZero_When_ProductsCartHasNoProducts() = runTest(this.rule.testDispatcher) {

        // We simulate that the flow productsCart returns this list from the useCase
        val products: List<ProductCart> = listOf()

        coEvery { rule.getProductsCartUseCase.invoke() } returns flowOf(products)

        rule.initViewModel()

        val emittedTotals: MutableList<Double> = mutableListOf()

        val job: Job = launch {

            rule.productsCartViewModel.cartTotal.collect { total ->

                emittedTotals.add(total)
            }
        }

        advanceUntilIdle()

        assertThat(emittedTotals).containsExactly(0.0).inOrder()

        coVerify(exactly = 1) { rule.getProductsCartUseCase.invoke() }

        job.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun should_EmitZero_When_GetProductsCartUseCaseThrowsException() = runTest(this.rule.testDispatcher) {

        coEvery { rule.getProductsCartUseCase.invoke() } returns flow{ throw Exception("Error")}

        rule.initViewModel()

        val emittedTotals: MutableList<Double> = mutableListOf()

        val job: Job = launch {

            rule.productsCartViewModel.cartTotal.collect { total ->

                emittedTotals.add(total)
            }
        }

        advanceUntilIdle()

        // the first value is 0.0
        assertThat(emittedTotals).containsExactly(0.0).inOrder()

        coVerify(exactly = 1) { rule.getProductsCartUseCase.invoke() }

        job.cancel()
    }

    // endregion

    // region updateProductCart

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun should_EmitLoading_When_UpdateProductCartInitializes() = runTest(this.rule.testDispatcher) {

        val emittedRowsUpdated: MutableList<ResponseResult<Int>> = mutableListOf()

        val job: Job = launch {

            rule.productsCartViewModel.updateProductState.collect {

                emittedRowsUpdated.add(it)
            }
        }

        rule.initViewModel()

        advanceUntilIdle()

        assertThat(emittedRowsUpdated[0]).isEqualTo(ResponseResult.Loading)

        job.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun should_EmitSuccess_When_UpdateAmountForProductUseCaseReturnsRowsUpdated() = runTest(this.rule.testDispatcher) {

        val productIdTest = 100
        val amountTest = 5
        val rowsUpdated = 1

        // We returned 1, this means that the row was updated
        coEvery { rule.updateAmountForProductUseCase(productId = productIdTest, amount = amountTest) } returns rowsUpdated

        val emittedRowsUpdated: MutableList<ResponseResult<Int>> = mutableListOf()

        val job: Job = launch {

            rule.productsCartViewModel.updateProductState.collect {

                emittedRowsUpdated.add(it)
            }
        }

        rule.initViewModel()

        rule.productsCartViewModel.updateProductCart(productId = productIdTest, amount = amountTest)

        advanceUntilIdle()

        assertThat(emittedRowsUpdated[0]).isEqualTo(ResponseResult.Loading)
        assertThat(emittedRowsUpdated[1]).isEqualTo(ResponseResult.Success(rowsUpdated))

        coVerify(exactly = 1) { rule.updateAmountForProductUseCase(productId = productIdTest, amount = amountTest) }

        job.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun should_EmitError_When_UpdateAmountForProductUseCaseReturnsZeroRowsUpdated() = runTest(this.rule.testDispatcher) {

        val productIdTest = 100
        val amountTest = 5
        val rowsUpdated = 0

        // We returned 0, this means that no row was updated
        coEvery { rule.updateAmountForProductUseCase(productId = productIdTest, amount = amountTest) } returns rowsUpdated

        val emittedRowsUpdated: MutableList<ResponseResult<Int>> = mutableListOf()

        val job: Job = launch {

            rule.productsCartViewModel.updateProductState.collect {

                emittedRowsUpdated.add(it)
            }
        }

        rule.initViewModel()

        rule.productsCartViewModel.updateProductCart(productId = productIdTest, amount = amountTest)

        advanceUntilIdle()

        assertThat(emittedRowsUpdated[0]).isEqualTo(ResponseResult.Loading)
        assertThat(emittedRowsUpdated[1]).isEqualTo(ResponseResult.Error("An unexpected error occurred while updating product amount"))

        coVerify(exactly = 1) { rule.updateAmountForProductUseCase(productId = productIdTest, amount = amountTest) }

        job.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun should_EmitError_When_UpdateAmountForProductUseCaseThrowsException() = runTest(this.rule.testDispatcher) {

        val productIdTest = 100
        val amountTest = 5
        val errorMessage = "Error for update row"

        coEvery { rule.updateAmountForProductUseCase(productId = productIdTest, amount = amountTest) } throws Exception(errorMessage)

        val emittedRowsUpdated: MutableList<ResponseResult<Int>> = mutableListOf()

        val job: Job = launch {

            rule.productsCartViewModel.updateProductState.collect {

                emittedRowsUpdated.add(it)
            }
        }

        rule.initViewModel()

        rule.productsCartViewModel.updateProductCart(productId = productIdTest, amount = amountTest)

        advanceUntilIdle()

        assertThat(emittedRowsUpdated[0]).isEqualTo(ResponseResult.Loading)
        assertThat(emittedRowsUpdated[1]).isEqualTo(ResponseResult.Error(errorMessage))

        coVerify(exactly = 1) { rule.updateAmountForProductUseCase(productId = productIdTest, amount = amountTest) }

        job.cancel()
    }

    // endregion

    // region delete product

    @Test
    fun should_EmitLoading_When_DeleteProductCartInitializes() = runTest {

        rule.initViewModel()

        assertThat(rule.productsCartViewModel.deleteProductState.value).isEqualTo(ResponseResult.Loading)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun should_EmitSuccess_When_DeleteProductCartUseCaseReturnsRowsAffected() = runTest(this.rule.testDispatcher) {

        val productIdTest = 100
        val rowsAffected = 1

        coEvery { rule.deleteProductCartUseCase(productId = productIdTest) } returns rowsAffected

        val emittedRowsUpdated: MutableList<ResponseResult<Int>> = mutableListOf()

        val job: Job = launch {

            rule.productsCartViewModel.deleteProductState.collect {

                emittedRowsUpdated.add(it)
            }
        }

        rule.initViewModel()

        rule.productsCartViewModel.deleteProductCart(productIdTest)

        advanceUntilIdle()

        assertThat(emittedRowsUpdated[0]).isEqualTo(ResponseResult.Loading)
        assertThat(emittedRowsUpdated[1]).isEqualTo(ResponseResult.Success(rowsAffected))

        coVerify(exactly = 1) {

            rule.deleteProductCartUseCase(productId = productIdTest)
        }

        job.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun should_EmitError_When_DeleteProductCartUseCaseReturnsZeroRowsAffected() = runTest(this.rule.testDispatcher) {

        val productIdTest = 100
        val rowsAffected = 0

        coEvery { rule.deleteProductCartUseCase(productId = productIdTest) } returns rowsAffected

        val emittedRowsUpdated: MutableList<ResponseResult<Int>> = mutableListOf()

        val job: Job = launch {

            rule.productsCartViewModel.deleteProductState.collect {

                emittedRowsUpdated.add(it)
            }
        }

        rule.initViewModel()

        rule.productsCartViewModel.deleteProductCart(productIdTest)

        advanceUntilIdle()

        assertThat(emittedRowsUpdated[0]).isEqualTo(ResponseResult.Loading)
        assertThat(emittedRowsUpdated[1]).isEqualTo(ResponseResult.Error("An unexpected error occurred while deleting product"))

        coVerify(exactly = 1) {

            rule.deleteProductCartUseCase(productId = productIdTest)
        }

        job.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun should_EmitError_When_DeleteProductCartUseCaseThrowsException() = runTest(this.rule.testDispatcher) {

        val productIdTest = 100
        val errorMessage = "Error to delete product cart"

        coEvery { rule.deleteProductCartUseCase(productId = productIdTest) } throws Exception(errorMessage)

        val emittedRowsUpdated: MutableList<ResponseResult<Int>> = mutableListOf()

        val job: Job = launch {

            rule.productsCartViewModel.deleteProductState.collect {

                emittedRowsUpdated.add(it)
            }
        }

        rule.initViewModel()

        rule.productsCartViewModel.deleteProductCart(productIdTest)

        advanceUntilIdle()

        assertThat(emittedRowsUpdated[0]).isEqualTo(ResponseResult.Loading)
        assertThat(emittedRowsUpdated[1]).isEqualTo(ResponseResult.Error(errorMessage))

        coVerify(exactly = 1) {

            rule.deleteProductCartUseCase(productId = productIdTest)
        }

        job.cancel()
    }

    // endregion

    // region complete purchase

    @Test
    fun should_EmitLoading_When_PurchaseCartInitializes() = runTest {

        rule.initViewModel()

        assertThat(rule.productsCartViewModel.purchaseCartState.value).isEqualTo(ResponseResult.Loading)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun should_EmitSuccess_When_UpdateProductsAsPurchasedUseCaseReturnsRowsUpdated() = runTest(this.rule.testDispatcher) {

        val product01: ProductCart = ProductCartBuilder().withProductId(1).build()
        val product02: ProductCart = ProductCartBuilder().withProductId(2).build()
        val product03: ProductCart = ProductCartBuilder().withProductId(3).build()

        val products: List<ProductCart> = listOf(product01, product02, product03)
        val productsId: List<Int> = listOf(product01.productId, product02.productId, product03.productId)
        val rowsAffected = 3

        coEvery { rule.updateProductsAsPurchasedUseCase.invoke(productsId = productsId) } returns rowsAffected

        val emittedRowsUpdated: MutableList<ResponseResult<Int>> = mutableListOf()

        val job: Job = launch {

            rule.productsCartViewModel.purchaseCartState.collect {

                emittedRowsUpdated.add(it)
            }
        }

        rule.initViewModel()

        rule.productsCartViewModel.updateProductsAsPurchase(products = products)

        advanceUntilIdle()

        assertThat(emittedRowsUpdated[0]).isEqualTo(ResponseResult.Loading)
        assertThat(emittedRowsUpdated[1]).isEqualTo(ResponseResult.Success(rowsAffected))

        coVerify(exactly = 1) {

            rule.updateProductsAsPurchasedUseCase.invoke(productsId = productsId)
        }

        job.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun should_EmitError_When_UpdateProductsAsPurchasedUseCaseReturnsZeroRowsUpdated() = runTest(this.rule.testDispatcher) {

        val product01: ProductCart = ProductCartBuilder().withProductId(1).build()
        val product02: ProductCart = ProductCartBuilder().withProductId(2).build()
        val product03: ProductCart = ProductCartBuilder().withProductId(3).build()

        val products: List<ProductCart> = listOf(product01, product02, product03)
        val productsId: List<Int> = listOf(product01.productId, product02.productId, product03.productId)
        val rowsAffected = 0

        coEvery { rule.updateProductsAsPurchasedUseCase.invoke(productsId = productsId) } returns rowsAffected

        val emittedRowsUpdated: MutableList<ResponseResult<Int>> = mutableListOf()

        val job: Job = launch {

            rule.productsCartViewModel.purchaseCartState.collect {

                emittedRowsUpdated.add(it)
            }
        }

        rule.initViewModel()

        rule.productsCartViewModel.updateProductsAsPurchase(products = products)

        advanceUntilIdle()

        assertThat(emittedRowsUpdated[0]).isEqualTo(ResponseResult.Loading)
        assertThat(emittedRowsUpdated[1]).isEqualTo(ResponseResult.Error("An unexpected error occurred during purchase"))

        coVerify(exactly = 1) {

            rule.updateProductsAsPurchasedUseCase.invoke(productsId = productsId)
        }

        job.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun should_EmitError_When_UpdateProductsAsPurchasedUseCaseThrowsException() = runTest(this.rule.testDispatcher) {

        val product01: ProductCart = ProductCartBuilder().withProductId(1).build()
        val product02: ProductCart = ProductCartBuilder().withProductId(2).build()
        val product03: ProductCart = ProductCartBuilder().withProductId(3).build()

        val products: List<ProductCart> = listOf(product01, product02, product03)
        val productsId: List<Int> = listOf(product01.productId, product02.productId, product03.productId)

        val errorMessage = "Error to update products as purchase"

        coEvery { rule.updateProductsAsPurchasedUseCase.invoke(productsId = productsId) } throws Exception(errorMessage)

        val emittedRowsUpdated: MutableList<ResponseResult<Int>> = mutableListOf()

        val job: Job = launch {

            rule.productsCartViewModel.purchaseCartState.collect {

                emittedRowsUpdated.add(it)
            }
        }

        rule.initViewModel()

        rule.productsCartViewModel.updateProductsAsPurchase(products = products)

        advanceUntilIdle()

        assertThat(emittedRowsUpdated[0]).isEqualTo(ResponseResult.Loading)
        assertThat(emittedRowsUpdated[1]).isEqualTo(ResponseResult.Error(errorMessage))

        coVerify(exactly = 1) {

            rule.updateProductsAsPurchasedUseCase.invoke(productsId = productsId)
        }

        job.cancel()
    }

    // endregion

}