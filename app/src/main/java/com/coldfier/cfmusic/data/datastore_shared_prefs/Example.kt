package com.coldfier.cfmusic.data.datastore_shared_prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

data class SettingsModel(var name: String = "", var phone: String = "", var address: String = "")

class PreferenceManager(private val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "example")

    companion object {
        val NAME = stringPreferencesKey("NAME")
        val PHONE_NUMBER = stringPreferencesKey("PHONE_NUMBER")
        val ADDRESS = stringPreferencesKey("ADDRESS")
    }

    suspend fun saveToDataStore(settingsModel: SettingsModel) {
        context.dataStore.edit {
            it[NAME] = settingsModel.name
            it[PHONE_NUMBER] = settingsModel.phone
            it[ADDRESS] = settingsModel.address
        }
    }

    fun getFromDataStore() = context.dataStore.data
        .catch {
            flowOf<Preferences>()
        }
        .map {
            SettingsModel(
                name = it[NAME] ?: "",
                phone = it[PHONE_NUMBER] ?: "",
                address = it[ADDRESS] ?: ""
            )
    }
}

