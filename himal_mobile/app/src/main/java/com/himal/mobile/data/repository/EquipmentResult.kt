package com.himal.mobile.data.repository

import com.himal.mobile.data.remote.dto.EkspedicijaOpremaResponse

sealed class EquipmentResult {

    data class Success(
        val equipment: List<EkspedicijaOpremaResponse>
    ) : EquipmentResult()

    data class Error(
        val message: String
    ) : EquipmentResult()

    data object Unauthorized : EquipmentResult()
}