package com.example.storeapp.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class StoreCategoryResponse(
    val id: Int = 0,
    val name: String = "",
    val slug: String = "",
    val image: String = ""
)

