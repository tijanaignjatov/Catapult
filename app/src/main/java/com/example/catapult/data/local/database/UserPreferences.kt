package com.example.catapult.data.local.database


import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences(private val context: Context) {

    companion object {
        private val Context.dataStore by preferencesDataStore(name = "user_prefs")

        private val KEY_NAME = stringPreferencesKey("name")
        private val KEY_NICKNAME = stringPreferencesKey("nickname")
        private val KEY_EMAIL = stringPreferencesKey("email")
    }

    suspend fun saveUser(name: String, nickname: String, email: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_NAME] = name
            prefs[KEY_NICKNAME] = nickname
            prefs[KEY_EMAIL] = email
        }
    }

    val name: Flow<String?> = context.dataStore.data.map { it[KEY_NAME] }
    val nickname: Flow<String?> = context.dataStore.data.map { it[KEY_NICKNAME] }
    val email: Flow<String?> = context.dataStore.data.map { it[KEY_EMAIL] }

    suspend fun clearUser() {
        context.dataStore.edit { it.clear() }
    }
}
