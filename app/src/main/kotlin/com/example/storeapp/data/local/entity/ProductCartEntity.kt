package com.example.storeapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products_cart")
data class ProductCartEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val productId: Int,
    val title: String,
    val price: Double,
    val image: String,
    val amountInCart: Int,
    val isPurchased: Boolean,
    val categoryName: String
)