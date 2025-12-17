package com.example.storeapp.domain.usecase

import com.example.storeapp.domain.repository.StoreRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(private val repository: StoreRepository) {

    suspend operator fun invoke(): Boolean {

        val logoutSuccessful: Boolean = repository.logout()

        if (logoutSuccessful) {

            repository.clearLocalData()
        }

        return logoutSuccessful
    }
}