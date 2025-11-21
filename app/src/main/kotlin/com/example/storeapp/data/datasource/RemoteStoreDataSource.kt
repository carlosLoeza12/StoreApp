package com.example.storeapp.data.datasource

import com.example.storeapp.data.remote.api.ApiService
import com.example.storeapp.data.remote.model.StoreCategoryResponse
import com.example.storeapp.data.remote.model.StoreProductResponse
import javax.inject.Inject

class RemoteStoreDataSource @Inject constructor(private val apiService: ApiService) {

    suspend fun getStoreCategories(): List<StoreCategoryResponse> {

        return apiService.getStoreCategories()
    }

    suspend fun getProductsByCategory(categoryId: Int): List<StoreProductResponse> {

        return apiService.getProductsByCategory(categoryId)
    }
}