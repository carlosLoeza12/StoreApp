package com.example.storeapp.presentation.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.core.util.ResponseResult
import com.example.storeapp.di.IoDispatcher
import com.example.storeapp.domain.model.User
import com.example.storeapp.domain.usecase.GetSessionUseCase
import com.example.storeapp.domain.usecase.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getSessionUseCase : GetSessionUseCase,
    private val logoutUseCase: LogoutUseCase,
    @param:IoDispatcher private val dispatcher: CoroutineDispatcher
): ViewModel() {

    private val _profileState: MutableStateFlow<ResponseResult<User>> = MutableStateFlow(ResponseResult.Loading)
    val profileState: StateFlow<ResponseResult<User>> = this._profileState

    private val _logoutState: MutableStateFlow<ResponseResult<Boolean>?> = MutableStateFlow(null)
    val logoutState: StateFlow<ResponseResult<Boolean>?> = this._logoutState

    init {

        viewModelScope.launch {

            try {

                _profileState.value = ResponseResult.Loading

                val userData: User? = withContext(dispatcher) { getSessionUseCase().firstOrNull() }

                if (userData == null || userData.id.isEmpty()) {

                    _profileState.value = ResponseResult.Error(message = "User not found")
                } else {

                    _profileState.value = ResponseResult.Success(value = userData)
                }

            } catch (e: Exception) {

                _profileState.value = ResponseResult.Error(message = e.localizedMessage ?: "An unexpected error occurred")
            }
        }
    }

    fun logout() {

        viewModelScope.launch {

            try {

                _logoutState.value = ResponseResult.Loading

                val isLoggedOut: Boolean = withContext(dispatcher) { logoutUseCase() }

                _logoutState.value = ResponseResult.Success(value = isLoggedOut)
            } catch (e: Exception) {

                _logoutState.value = ResponseResult.Error(message = e.localizedMessage ?: "An unexpected error occurred")
            }
        }
    }
}