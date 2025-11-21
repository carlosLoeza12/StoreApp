package com.example.storeapp.domain.model

import kotlinx.serialization.Serializable
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Serializable
@Parcelize
data class StoreCategory(
    val id: Int,
    val name: String,
    val slug: String,
    val image: String
) : Parcelable

class StoreCategoryBuilder {

    private var id: Int = 0
    private var name: String = "name"
    private var slug: String = "slug"
    private var image: String = "image"

    fun id(id: Int) = apply { this.id = id }
    fun name(name: String) = apply { this.name = name }
    fun slug(slug: String) = apply { this.slug = slug }
    fun image(image: String) = apply { this.image = image }

    fun build(): StoreCategory {

        return StoreCategory(
            id = this.id,
            name = this.name,
            slug = this.slug,
            image = this.image
        )
    }
}
