package com.example.storeapp.domain.usecase

import com.example.storeapp.domain.model.StoreCategory
import com.example.storeapp.domain.repository.StoreRepository
import javax.inject.Inject

class GetStoreCategoriesUseCase @Inject constructor(private val repository: StoreRepository) {

    suspend operator fun invoke(): List<StoreCategory> {

        return repository.getStoreCategories()
    }
}
