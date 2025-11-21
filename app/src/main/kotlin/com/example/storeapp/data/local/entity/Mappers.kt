package com.example.storeapp.data.local.entity

import com.example.storeapp.domain.model.ProductCart

fun ProductCartEntity.toDomain() : ProductCart {

    return ProductCart(
        id = this.id,
        productId = this.productId,
        title = this.title,
        price = this.price,
        image = this.image,
        amountInCart = this.amountInCart,
        isPurchased = this.isPurchased,
        categoryName = this.categoryName
    )
}


fun ProductCart.toEntity() : ProductCartEntity {

    return ProductCartEntity(
        id = this.id,
        productId = this.productId,
        title = this.title,
        price = this.price,
        image = this.image,
        amountInCart = this.amountInCart,
        isPurchased = this.isPurchased,
        categoryName = this.categoryName
    )
}