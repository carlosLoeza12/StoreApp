package com.example.storeapp.domain.model

data class User(
    val id: String,
    val name: String,
    val email: String,
    val phoneNumber: String,
    val photoUrl: String
)

class UserBuilder {

    private var id: String = ""
    private var name: String = ""
    private var email: String = ""
    private var phoneNumber: String = ""
    private var photoUrl: String = ""

    fun id(id: String) = apply { this.id = id }
    fun name(name: String) = apply { this.name = name }
    fun email(email: String) = apply { this.email = email }
    fun phoneNumber(phoneNumber: String) = apply { this.phoneNumber = phoneNumber }
    fun photoUrl(photoUrl: String) = apply { this.photoUrl = photoUrl }

    fun build(): User {

        return User(
            id = this.id,
            name = this.name,
            email = this.email,
            phoneNumber = this.phoneNumber,
            photoUrl = this.photoUrl
        )
    }
}