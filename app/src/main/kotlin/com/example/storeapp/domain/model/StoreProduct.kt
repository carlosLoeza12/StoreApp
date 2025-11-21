package com.example.storeapp.domain.model

import kotlinx.serialization.Serializable
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Serializable
@Parcelize
data class StoreProduct(
    val id: Int,
    val title: String,
    val slug: String,
    val price: Double,
    val description: String,
    val category: StoreCategory,
    val images: List<String>
): Parcelable

class StoreProductBuilder {

    private var id: Int = 0
    private var title: String = ""
    private var slug: String = ""
    private var price: Double = 0.0
    private var description: String = ""
    private var category: StoreCategory = StoreCategory(0, "", "", "")
    private var images: List<String> = emptyList()

    fun id(id: Int) = apply { this.id = id }
    fun title(title: String) = apply { this.title = title }
    fun slug(slug: String) = apply { this.slug = slug }
    fun price(price: Double) = apply { this.price = price }
    fun description(description: String) = apply { this.description = description }
    fun category(category: StoreCategory) = apply { this.category = category }
    fun images(images: List<String>) = apply { this.images = images }

    fun build(): StoreProduct {

        return StoreProduct(
            id = this.id,
            title = this.title,
            slug =  this.slug,
            price = this.price,
            description = this.description,
            category = this.category,
            images = this.images
        )
    }
}