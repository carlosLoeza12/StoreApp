package com.example.storeapp.domain.usecase

import com.example.storeapp.domain.model.StoreProduct
import com.example.storeapp.domain.repository.StoreRepository
import javax.inject.Inject

class GetStoreProductsByCategoryUseCase @Inject constructor(private val repository: StoreRepository) {

    suspend operator fun invoke(categoryId: Int): List<StoreProduct> {

        return repository.getProductsByCategory(categoryId)
    }
}
