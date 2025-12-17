package com.example.storeapp.data.remote

import android.content.Context
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.example.storeapp.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GoogleAuthUiClient @Inject constructor(@param:ApplicationContext private val appContext: Context) {

    val credentialManager: CredentialManager = CredentialManager.create(appContext)

    private val signInRequest: GetCredentialRequest by lazy {

        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setServerClientId(appContext.getString(R.string.default_web_client_id))
            .setFilterByAuthorizedAccounts(false)
            .setAutoSelectEnabled(false)
            .build()

        GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
    }

    suspend fun getTokenId(activityContext: Context): String? {

        return try {

            val response: GetCredentialResponse = this.credentialManager.getCredential(
                request = signInRequest,
                context = activityContext
            )

            this.extractIdToken(response)

        } catch (e: Exception) {

            null
        }
    }

    private fun extractIdToken(response: GetCredentialResponse): String? {

        val credential: Credential = response.credential

        if (credential.type != TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) return null

        return runCatching {

            GoogleIdTokenCredential.createFrom(credential.data).idToken
        }.getOrNull()
    }
}