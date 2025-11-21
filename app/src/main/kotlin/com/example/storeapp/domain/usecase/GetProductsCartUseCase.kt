package com.example.storeapp.domain.usecase

import com.example.storeapp.domain.model.ProductCart
import com.example.storeapp.domain.repository.StoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductsCartUseCase @Inject constructor(private val repository: StoreRepository) {

    operator fun invoke(): Flow<List<ProductCart>> {

        return repository.getProductsCart()
    }
}