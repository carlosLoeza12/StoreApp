package com.example.storeapp.data.local.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.storeapp.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferencesDataStore @Inject constructor(private val dataStore: DataStore<Preferences>) {

    companion object {

        val KEY_ID: Preferences.Key<String> = stringPreferencesKey("id")
        val KEY_NAME: Preferences.Key<String> = stringPreferencesKey("name")
        val KEY_EMAIL: Preferences.Key<String> = stringPreferencesKey("email")
        val KEY_PHONE: Preferences.Key<String> = stringPreferencesKey("phoneNumber")
        val KEY_PHOTO: Preferences.Key<String> = stringPreferencesKey("photoUrl")
    }

    val sessionFlow: Flow<User> = dataStore.data.map { prefs: Preferences ->

        val id: String = prefs[KEY_ID] ?: ""
        val name: String = prefs[KEY_NAME] ?: ""
        val email: String = prefs[KEY_EMAIL] ?: ""
        val phone: String = prefs[KEY_PHONE] ?: ""
        val photo: String = prefs[KEY_PHOTO] ?: ""

        User(
           id = id,
           name = name,
           email = email,
           phoneNumber = phone,
           photoUrl = photo
        )
    }

    suspend fun saveSession(user: User) {

        dataStore.edit { prefs ->

            prefs[KEY_ID] = user.id
            prefs[KEY_NAME] = user.name
            prefs[KEY_EMAIL] = user.email
            prefs[KEY_PHONE] = user.phoneNumber
            prefs[KEY_PHOTO] = user.photoUrl
        }
    }

    suspend fun clearSession(): Boolean {

        return try {

            dataStore.edit { it.clear() }

            dataStore.data.first().asMap().isEmpty()
        } catch (e: Exception) {

            false
        }
    }
}