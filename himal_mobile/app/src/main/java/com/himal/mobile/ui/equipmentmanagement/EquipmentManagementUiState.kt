package com.himal.mobile.ui.equipmentmanagement

import com.himal.mobile.data.remote.dto.EkspedicijaOpremaResponse
import com.himal.mobile.data.remote.dto.OpremaResponse

data class EquipmentManagementUiState(
    val expeditionId: Long? = null,

    val catalog: List<OpremaResponse> = emptyList(),

    val expeditionEquipment:
    List<EkspedicijaOpremaResponse> = emptyList(),

    val isLoading: Boolean = false,
    val isSubmitting: Boolean = false,

    val showAddDialog: Boolean = false,
    val selectedEquipmentId: Long? = null,
    val addQuantity: String = "1",
    val addRequired: Boolean = true,
    val addNote: String = "",

    val editingEquipment:
    EkspedicijaOpremaResponse? = null,
    val editQuantity: String = "1",
    val editRequired: Boolean = true,
    val editNote: String = "",

    val equipmentToDelete:
    EkspedicijaOpremaResponse? = null,

    val showCreateEquipmentDialog: Boolean = false,
    val newEquipmentName: String = "",
    val newEquipmentDescription: String = "",

    val errorMessage: String? = null,
    val sessionExpired: Boolean = false
)