package com.example.storeapp.domain.usecase

import com.example.storeapp.domain.repository.StoreRepository
import javax.inject.Inject

class DeleteProductCartUseCase @Inject constructor(private val repository: StoreRepository) {

    suspend operator fun invoke(productId: Int): Int {

       return repository.deleteProductFromCart(productId)
    }
}
