package com.example.storeapp.domain.usecase

import com.example.storeapp.domain.repository.StoreRepository
import javax.inject.Inject

class UpdateAmountForProductUseCase @Inject constructor(private val repository: StoreRepository) {

    suspend operator fun invoke(productId: Int, amount: Int): Int {

        return repository.updateAmountProductCart(productId, amount)
    }
}