package com.example.storeapp.data.remote.model

import com.example.storeapp.domain.model.StoreCategory
import com.example.storeapp.domain.model.StoreProduct

fun StoreCategoryResponse.toDomain(): StoreCategory {
    return StoreCategory(
        id = this.id,
        name = this.name,
        slug = this.slug,
        image = this.image
    )
}

fun StoreProductResponse.toDomain(): StoreProduct {
    return StoreProduct(
        id = this.id,
        title = this.title,
        slug = this.slug,
        price = this.price,
        description = this.description,
        category = this.category.toDomain(),
        images = this.images
    )
}
