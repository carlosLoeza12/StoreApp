package com.example.storeapp.data.remote.api

import com.example.storeapp.data.remote.model.StoreCategoryResponse
import com.example.storeapp.data.remote.model.StoreProductResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("categories")
    suspend fun getStoreCategories(): List<StoreCategoryResponse>

    @GET("products/")
    suspend fun getProductsByCategory(
        @Query("categoryId") categoryId: Int
    ): List<StoreProductResponse>
}