package com.example.storeapp.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class StoreProductResponse(
    val id: Int = 0,
    val title: String = "",
    val slug: String = "",
    val price: Double = 0.0,
    val description: String = "",
    val category: StoreCategoryResponse = StoreCategoryResponse(0, "", "", ""),
    val images: List<String> = emptyList()
)
