package com.example.storeapp.domain.model

data class ProductCart(
    val id: Int = 0,
    val productId: Int,
    val title: String,
    val price: Double,
    val image: String,
    val amountInCart: Int,
    val isPurchased: Boolean,
    val categoryName: String
)

class ProductCartBuilder {

    private var id: Int = 0
    private var productId: Int = 0
    private var title: String = ""
    private var price: Double = 0.0
    private var image: String = ""
    private var amountInCart: Int = 0
    private var isPurchased: Boolean = false
    private var categoryName: String = ""

    fun withId(id: Int) = apply { this.id = id }
    fun withProductId(productId: Int) = apply { this.productId = productId }
    fun withTitle(title: String) = apply { this.title = title }
    fun withPrice(price: Double) = apply { this.price = price }
    fun withImage(image: String) = apply { this.image = image }
    fun withAmountInCart(amountInCart: Int) = apply { this.amountInCart = amountInCart }
    fun withIsPurchased(isPurchased: Boolean) = apply { this.isPurchased = isPurchased }
    fun withCategoryName(categoryName: String) = apply { this.categoryName = categoryName }

    fun build() = ProductCart(
        id = id,
        productId = productId,
        title = title,
        price = price,
        image = image,
        amountInCart = amountInCart,
        isPurchased = isPurchased,
        categoryName = categoryName
    )
}
