package com.example.storeapp.domain.usecase

import com.example.storeapp.domain.repository.StoreRepository
import javax.inject.Inject

class UpdateProductsAsPurchasedUseCase @Inject constructor(private val repository: StoreRepository) {

    suspend fun invoke(productsId: List<Int>): Int {

        return repository.updateProductsAsPurchased(productsId)
    }
}