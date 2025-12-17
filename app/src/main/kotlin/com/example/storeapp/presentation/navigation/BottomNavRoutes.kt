package com.example.storeapp.presentation.navigation

import com.example.storeapp.domain.model.StoreProduct
import kotlinx.serialization.Serializable

@Serializable
object CategoriesGraph

@Serializable
object CategoriesTab

@Serializable
data class ProductsByCategoryTab(val categoryId: Int)

@Serializable
data class ProductDetailTab(val product: StoreProduct)

@Serializable
object CartTab

@Serializable
object ProfileTab

