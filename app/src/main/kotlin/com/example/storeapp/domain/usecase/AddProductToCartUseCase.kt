package com.example.storeapp.domain.usecase

import com.example.storeapp.domain.model.ProductCart
import com.example.storeapp.domain.repository.StoreRepository
import javax.inject.Inject

class AddProductToCartUseCase @Inject constructor(private val repository: StoreRepository) {

    suspend operator fun invoke(product: ProductCart): Long {

        return repository.addProductToCart(product = product)
    }
}