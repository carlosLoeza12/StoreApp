package com.example.storeapp.domain.usecase

import com.example.storeapp.domain.model.User
import com.example.storeapp.domain.repository.StoreRepository
import javax.inject.Inject

class SaveSessionUseCase @Inject constructor(private val repository: StoreRepository) {

    suspend operator fun invoke(user: User) {

        repository.saveSession(user)
    }
}