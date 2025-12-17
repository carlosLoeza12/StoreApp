package com.example.storeapp.data.remote.model

import com.example.storeapp.domain.model.StoreCategory
import com.example.storeapp.domain.model.StoreProduct
import com.example.storeapp.domain.model.User
import com.google.firebase.auth.FirebaseUser

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

fun FirebaseUser.toDomain(): User {
    return User(
        id = this.uid,
        name = this.displayName ?: "",
        email = this.email ?: "",
        phoneNumber = this.phoneNumber ?: "",
        photoUrl = this.photoUrl.toString()
    )
}