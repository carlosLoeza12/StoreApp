package com.example.storeapp.domain.repository

import com.example.storeapp.domain.model.ProductCart
import com.example.storeapp.domain.model.StoreCategory
import com.example.storeapp.domain.model.StoreProduct
import kotlinx.coroutines.flow.Flow

interface StoreRepository {

    suspend fun getStoreCategories(): List<StoreCategory>

    suspend fun getProductsByCategory(categoryId: Int): List<StoreProduct>

    fun getProductsCart(): Flow<List<ProductCart>>
    suspend fun addProductToCart(product: ProductCart): Long

    suspend fun updateAmountProductCart(productId: Int, amount: Int): Int

    suspend fun deleteProductFromCart(productId: Int): Int

    suspend fun updateProductsAsPurchased(productsId: List<Int>): Int
}