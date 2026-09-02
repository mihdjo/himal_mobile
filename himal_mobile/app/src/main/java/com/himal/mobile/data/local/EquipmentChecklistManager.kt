package com.himal.mobile.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.checklistDataStore by preferencesDataStore(
    name = "himal_equipment_checklist"
)

fun checklistKey(
    expeditionId: Long,
    equipmentId: Long
): String {
    return "$expeditionId:$equipmentId"
}

class EquipmentChecklistManager(
    private val context: Context
) {

    companion object {

        private val PREPARED_ITEMS =
            stringSetPreferencesKey(
                "prepared_equipment_items"
            )
    }

    val preparedItems: Flow<Set<String>> =
        context.checklistDataStore.data.map { preferences ->
            preferences[PREPARED_ITEMS]
                ?: emptySet()
        }

    suspend fun getPreparedItems(): Set<String> {
        return preparedItems.first()
    }

    suspend fun replacePreparedItems(
        items: Set<String>
    ) {

        context.checklistDataStore.edit { preferences ->

            preferences[PREPARED_ITEMS] =
                items
        }
    }

    suspend fun clear() {

        context.checklistDataStore.edit { preferences ->

            preferences.remove(PREPARED_ITEMS)
        }
    }

    suspend fun clearExpedition(
        expeditionId: Long
    ) {

        context.checklistDataStore.edit { preferences ->

            val currentItems =
                preferences[PREPARED_ITEMS]
                    ?: emptySet()

            val prefix = "$expeditionId:"

            val updatedItems =
                currentItems.filterNot { key ->
                    key.startsWith(prefix)
                }.toSet()

            preferences[PREPARED_ITEMS] =
                updatedItems
        }
    }
}