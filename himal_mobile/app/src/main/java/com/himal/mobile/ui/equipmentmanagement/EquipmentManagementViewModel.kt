package com.himal.mobile.ui.equipmentmanagement

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.himal.mobile.data.local.SessionManager
import com.himal.mobile.data.remote.RetrofitClient
import com.himal.mobile.data.remote.dto.AddEkspedicijaOpremaRequest
import com.himal.mobile.data.remote.dto.CreateOpremaRequest
import com.himal.mobile.data.remote.dto.EkspedicijaOpremaResponse
import com.himal.mobile.data.remote.dto.UpdateEkspedicijaOpremaRequest
import com.himal.mobile.data.repository.EquipmentManagementRepository
import com.himal.mobile.data.repository.EquipmentManagementResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EquipmentManagementViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository =
        EquipmentManagementRepository(
            api = RetrofitClient.api,
            sessionManager =
                SessionManager(
                    application.applicationContext
                )
        )

    private val _uiState =
        MutableStateFlow(
            EquipmentManagementUiState()
        )

    val uiState:
            StateFlow<EquipmentManagementUiState> =
        _uiState.asStateFlow()

    fun load(
        expeditionId: Long
    ) {

        _uiState.value =
            EquipmentManagementUiState(
                expeditionId = expeditionId,
                isLoading = true
            )

        viewModelScope.launch {

            when (
                val catalogResult =
                    repository.getCatalog()
            ) {

                is EquipmentManagementResult.Success -> {

                    val catalog =
                        catalogResult.data

                    when (
                        val equipmentResult =
                            repository
                                .getExpeditionEquipment(
                                    expeditionId
                                )
                    ) {

                        is EquipmentManagementResult.Success -> {

                            _uiState.value =
                                EquipmentManagementUiState(
                                    expeditionId =
                                        expeditionId,
                                    catalog =
                                        catalog,
                                    expeditionEquipment =
                                        equipmentResult.data
                                )
                        }

                        is EquipmentManagementResult.Error -> {

                            fail(
                                equipmentResult.message
                            )
                        }

                        EquipmentManagementResult.Unauthorized -> {
                            unauthorized()
                        }
                    }
                }

                is EquipmentManagementResult.Error -> {

                    fail(
                        catalogResult.message
                    )
                }

                EquipmentManagementResult.Unauthorized -> {
                    unauthorized()
                }
            }
        }
    }

    fun openAddDialog() {

        _uiState.value =
            _uiState.value.copy(
                showAddDialog = true,
                selectedEquipmentId = null,
                addQuantity = "1",
                addRequired = true,
                addNote = "",
                errorMessage = null
            )
    }

    fun closeAddDialog() {

        if (_uiState.value.isSubmitting) {
            return
        }

        _uiState.value =
            _uiState.value.copy(
                showAddDialog = false,
                selectedEquipmentId = null
            )
    }

    fun selectEquipment(
        equipmentId: Long
    ) {

        _uiState.value =
            _uiState.value.copy(
                selectedEquipmentId =
                    equipmentId,
                errorMessage = null
            )
    }

    fun onAddQuantityChange(
        value: String
    ) {

        _uiState.value =
            _uiState.value.copy(
                addQuantity = value,
                errorMessage = null
            )
    }

    fun onAddRequiredChange(
        value: Boolean
    ) {

        _uiState.value =
            _uiState.value.copy(
                addRequired = value
            )
    }

    fun onAddNoteChange(
        value: String
    ) {

        _uiState.value =
            _uiState.value.copy(
                addNote = value,
                errorMessage = null
            )
    }

    fun addEquipment() {

        val state = _uiState.value

        if (state.isSubmitting) {
            return
        }

        val expeditionId =
            state.expeditionId
                ?: return

        val equipmentId =
            state.selectedEquipmentId

        if (equipmentId == null) {

            fail(
                "Izaberi opremu."
            )

            return
        }

        val quantity =
            state.addQuantity.toIntOrNull()

        if (
            quantity == null ||
            quantity < 1
        ) {

            fail(
                "Količina mora biti najmanje 1."
            )

            return
        }

        if (
            state.addNote.length > 255
        ) {

            fail(
                "Napomena može imati najviše 255 karaktera."
            )

            return
        }

        _uiState.value =
            state.copy(
                isSubmitting = true,
                errorMessage = null
            )

        viewModelScope.launch {

            val request =
                AddEkspedicijaOpremaRequest(
                    idOpreme = equipmentId,
                    obavezna =
                        state.addRequired,
                    kolicina =
                        quantity,
                    napomena =
                        state.addNote
                            .trim()
                            .ifBlank {
                                null
                            }
                )

            when (
                val result =
                    repository.addEquipment(
                        expeditionId,
                        request
                    )
            ) {

                is EquipmentManagementResult.Success -> {

                    _uiState.value =
                        _uiState.value.copy(
                            isSubmitting = false,
                            showAddDialog = false,
                            selectedEquipmentId = null,
                            expeditionEquipment =
                                _uiState.value
                                    .expeditionEquipment +
                                        result.data
                        )
                }

                is EquipmentManagementResult.Error -> {

                    submittingError(
                        result.message
                    )
                }

                EquipmentManagementResult.Unauthorized -> {
                    unauthorized()
                }
            }
        }
    }

    fun openEdit(
        equipment:
        EkspedicijaOpremaResponse
    ) {

        _uiState.value =
            _uiState.value.copy(
                editingEquipment =
                    equipment,
                editQuantity =
                    equipment.kolicina
                        .toString(),
                editRequired =
                    equipment.obavezna,
                editNote =
                    equipment.napomena
                        .orEmpty(),
                errorMessage = null
            )
    }

    fun closeEdit() {

        if (_uiState.value.isSubmitting) {
            return
        }

        _uiState.value =
            _uiState.value.copy(
                editingEquipment = null
            )
    }

    fun onEditQuantityChange(
        value: String
    ) {

        _uiState.value =
            _uiState.value.copy(
                editQuantity = value,
                errorMessage = null
            )
    }

    fun onEditRequiredChange(
        value: Boolean
    ) {

        _uiState.value =
            _uiState.value.copy(
                editRequired = value
            )
    }

    fun onEditNoteChange(
        value: String
    ) {

        _uiState.value =
            _uiState.value.copy(
                editNote = value,
                errorMessage = null
            )
    }

    fun saveEdit() {

        val state = _uiState.value

        if (state.isSubmitting) {
            return
        }

        val expeditionId =
            state.expeditionId
                ?: return

        val equipment =
            state.editingEquipment
                ?: return

        val quantity =
            state.editQuantity
                .toIntOrNull()

        if (
            quantity == null ||
            quantity < 1
        ) {

            fail(
                "Količina mora biti najmanje 1."
            )

            return
        }

        if (
            state.editNote.length > 255
        ) {

            fail(
                "Napomena može imati najviše 255 karaktera."
            )

            return
        }

        _uiState.value =
            state.copy(
                isSubmitting = true,
                errorMessage = null
            )

        viewModelScope.launch {

            val request =
                UpdateEkspedicijaOpremaRequest(
                    obavezna =
                        state.editRequired,
                    kolicina =
                        quantity,
                    napomena =
                        state.editNote
                            .trim()
                            .ifBlank {
                                null
                            }
                )

            when (
                val result =
                    repository.updateEquipment(
                        expeditionId =
                            expeditionId,
                        equipmentId =
                            equipment.idOpreme,
                        request =
                            request
                    )
            ) {

                is EquipmentManagementResult.Success -> {

                    val updated =
                        result.data

                    _uiState.value =
                        _uiState.value.copy(
                            isSubmitting = false,
                            editingEquipment = null,
                            expeditionEquipment =
                                _uiState.value
                                    .expeditionEquipment
                                    .map {
                                        if (
                                            it.idOpreme ==
                                            updated.idOpreme
                                        ) {
                                            updated
                                        } else {
                                            it
                                        }
                                    }
                        )
                }

                is EquipmentManagementResult.Error -> {

                    submittingError(
                        result.message
                    )
                }

                EquipmentManagementResult.Unauthorized -> {
                    unauthorized()
                }
            }
        }
    }

    fun requestDelete(
        equipment:
        EkspedicijaOpremaResponse
    ) {

        _uiState.value =
            _uiState.value.copy(
                equipmentToDelete =
                    equipment
            )
    }

    fun cancelDelete() {

        _uiState.value =
            _uiState.value.copy(
                equipmentToDelete = null
            )
    }

    fun confirmDelete() {

        val state =
            _uiState.value

        val expeditionId =
            state.expeditionId
                ?: return

        val equipment =
            state.equipmentToDelete
                ?: return

        if (state.isSubmitting) {
            return
        }

        _uiState.value =
            state.copy(
                isSubmitting = true,
                errorMessage = null
            )

        viewModelScope.launch {

            when (
                val result =
                    repository.deleteEquipment(
                        expeditionId =
                            expeditionId,
                        equipmentId =
                            equipment.idOpreme
                    )
            ) {

                is EquipmentManagementResult.Success -> {

                    _uiState.value =
                        _uiState.value.copy(
                            isSubmitting = false,
                            equipmentToDelete = null,
                            expeditionEquipment =
                                _uiState.value
                                    .expeditionEquipment
                                    .filterNot {
                                        it.idOpreme ==
                                                equipment.idOpreme
                                    }
                        )
                }

                is EquipmentManagementResult.Error -> {

                    submittingError(
                        result.message
                    )
                }

                EquipmentManagementResult.Unauthorized -> {
                    unauthorized()
                }
            }
        }
    }

    fun openCreateEquipment() {

        _uiState.value =
            _uiState.value.copy(
                showCreateEquipmentDialog =
                    true,
                newEquipmentName = "",
                newEquipmentDescription = "",
                errorMessage = null
            )
    }

    fun closeCreateEquipment() {

        if (_uiState.value.isSubmitting) {
            return
        }

        _uiState.value =
            _uiState.value.copy(
                showCreateEquipmentDialog =
                    false
            )
    }

    fun onNewEquipmentNameChange(
        value: String
    ) {

        _uiState.value =
            _uiState.value.copy(
                newEquipmentName = value,
                errorMessage = null
            )
    }

    fun onNewEquipmentDescriptionChange(
        value: String
    ) {

        _uiState.value =
            _uiState.value.copy(
                newEquipmentDescription =
                    value,
                errorMessage = null
            )
    }

    fun createCatalogEquipment() {

        val state =
            _uiState.value

        if (state.isSubmitting) {
            return
        }

        val name =
            state.newEquipmentName.trim()

        val description =
            state.newEquipmentDescription
                .trim()

        if (name.isBlank()) {

            fail(
                "Naziv opreme je obavezan."
            )

            return
        }

        if (name.length > 100) {

            fail(
                "Naziv može imati najviše 100 karaktera."
            )

            return
        }

        if (description.length > 255) {

            fail(
                "Opis može imati najviše 255 karaktera."
            )

            return
        }

        _uiState.value =
            state.copy(
                isSubmitting = true,
                errorMessage = null
            )

        viewModelScope.launch {

            val request =
                CreateOpremaRequest(
                    naziv = name,
                    opis =
                        description.ifBlank {
                            null
                        }
                )

            when (
                val result =
                    repository.createEquipment(
                        request
                    )
            ) {

                is EquipmentManagementResult.Success -> {

                    val created =
                        result.data

                    _uiState.value =
                        _uiState.value.copy(
                            isSubmitting = false,

                            catalog =
                                _uiState.value
                                    .catalog +
                                        created,

                            showCreateEquipmentDialog =
                                false,

                            showAddDialog =
                                true,

                            selectedEquipmentId =
                                created.idOpreme,

                            newEquipmentName = "",
                            newEquipmentDescription = ""
                        )
                }

                is EquipmentManagementResult.Error -> {

                    submittingError(
                        result.message
                    )
                }

                EquipmentManagementResult.Unauthorized -> {
                    unauthorized()
                }
            }
        }
    }

    private fun fail(
        message: String
    ) {

        _uiState.value =
            _uiState.value.copy(
                isLoading = false,
                errorMessage = message
            )
    }

    private fun submittingError(
        message: String
    ) {

        _uiState.value =
            _uiState.value.copy(
                isSubmitting = false,
                errorMessage = message
            )
    }

    private fun unauthorized() {

        _uiState.value =
            EquipmentManagementUiState(
                sessionExpired = true
            )
    }

    fun reset() {

        _uiState.value =
            EquipmentManagementUiState()
    }
}