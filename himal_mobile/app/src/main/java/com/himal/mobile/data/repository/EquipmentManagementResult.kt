package com.himal.mobile.data.repository

sealed class EquipmentManagementResult<out T> {

    data class Success<T>(
        val data: T
    ) : EquipmentManagementResult<T>()

    data class Error(
        val message: String
    ) : EquipmentManagementResult<Nothing>()

    data object Unauthorized :
        EquipmentManagementResult<Nothing>()
}