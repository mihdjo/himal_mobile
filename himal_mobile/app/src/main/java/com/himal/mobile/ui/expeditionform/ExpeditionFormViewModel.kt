package com.himal.mobile.ui.expeditionform

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.himal.mobile.data.local.SessionManager
import com.himal.mobile.data.remote.RetrofitClient
import com.himal.mobile.data.remote.dto.EkspedicijaRequest
import com.himal.mobile.data.repository.ExpeditionCrudResult
import com.himal.mobile.data.repository.MyExpeditionsRepository
import com.himal.mobile.data.repository.ExpeditionTypesResult
import java.time.LocalDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ExpeditionFormViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository =
        MyExpeditionsRepository(
            api = RetrofitClient.api,
            sessionManager = SessionManager(
                application.applicationContext
            )
        )

    private val _uiState =
        MutableStateFlow(
            ExpeditionFormUiState()
        )

    val uiState: StateFlow<ExpeditionFormUiState> =
        _uiState.asStateFlow()

    fun prepareCreate() {

        _uiState.value =
            ExpeditionFormUiState(
                isLoadingTypes = true
            )

        viewModelScope.launch {

            when (
                val result =
                    repository.getExpeditionTypes()
            ) {

                is ExpeditionTypesResult.Success -> {

                    _uiState.value =
                        _uiState.value.copy(
                            expeditionTypes =
                                result.types,
                            isLoadingTypes = false
                        )
                }

                is ExpeditionTypesResult.Error -> {

                    _uiState.value =
                        _uiState.value.copy(
                            isLoadingTypes = false,
                            errorMessage =
                                result.message
                        )
                }

                ExpeditionTypesResult.Unauthorized -> {

                    _uiState.value =
                        ExpeditionFormUiState(
                            sessionExpired = true
                        )
                }
            }
        }
    }

    fun loadForEdit(
        expeditionId: Long
    ) {

        _uiState.value =
            ExpeditionFormUiState(
                expeditionId = expeditionId,
                isLoading = true,
                isLoadingTypes = true
            )

        viewModelScope.launch {

            val typesResult =
                repository.getExpeditionTypes()

            val types =
                when (typesResult) {

                    is ExpeditionTypesResult.Success -> {

                        typesResult.types
                    }

                    is ExpeditionTypesResult.Error -> {

                        _uiState.value =
                            ExpeditionFormUiState(
                                expeditionId =
                                    expeditionId,
                                errorMessage =
                                    typesResult.message
                            )

                        return@launch
                    }

                    ExpeditionTypesResult.Unauthorized -> {

                        _uiState.value =
                            ExpeditionFormUiState(
                                sessionExpired = true
                            )

                        return@launch
                    }
                }

            when (
                val result =
                    repository.getExpedition(
                        expeditionId
                    )
            ) {

                is ExpeditionCrudResult.Success -> {

                    val expedition =
                        result.expedition

                    _uiState.value =
                        ExpeditionFormUiState(
                            expeditionId =
                                expedition.idEkspedicije,

                            naziv =
                                expedition.naziv,

                            opis =
                                expedition.opis,

                            datumPolaska =
                                expedition.datumPolaska,

                            lokacija =
                                expedition.lokacija,

                            tezina =
                                expedition.tezina,

                            trajanjeMin =
                                expedition.trajanjeMin
                                    .toString(),

                            duzinaKm =
                                expedition.duzinaKm
                                    .toString(),

                            externalUrl =
                                expedition.externalUrl
                                    .orEmpty(),

                            expeditionTypes =
                                types,

                            selectedTypeId =
                                expedition.idTipEkspedicije
                        )
                }

                is ExpeditionCrudResult.Error -> {

                    _uiState.value =
                        _uiState.value.copy(
                            isLoading = false,
                            isLoadingTypes = false,
                            errorMessage =
                                result.message
                        )
                }

                ExpeditionCrudResult.Unauthorized -> {

                    _uiState.value =
                        ExpeditionFormUiState(
                            sessionExpired = true
                        )
                }
            }
        }
    }

    fun onNazivChange(value: String) {
        update {
            copy(
                naziv = value,
                errorMessage = null
            )
        }
    }

    fun onOpisChange(value: String) {
        update {
            copy(
                opis = value,
                errorMessage = null
            )
        }
    }

    fun onDatumPolaskaChange(value: String) {
        update {
            copy(
                datumPolaska = value,
                errorMessage = null
            )
        }
    }

    fun onLokacijaChange(value: String) {
        update {
            copy(
                lokacija = value,
                errorMessage = null
            )
        }
    }

    fun onTezinaChange(value: String) {
        update {
            copy(
                tezina = value,
                errorMessage = null
            )
        }
    }

    fun onTrajanjeMinChange(value: String) {
        update {
            copy(
                trajanjeMin = value,
                errorMessage = null
            )
        }
    }

    fun onDuzinaKmChange(value: String) {
        update {
            copy(
                duzinaKm = value,
                errorMessage = null
            )
        }
    }

    fun onExternalUrlChange(value: String) {
        update {
            copy(
                externalUrl = value,
                errorMessage = null
            )
        }
    }


    fun onTypeSelected(
        typeId: Long
    ) {

        _uiState.value =
            _uiState.value.copy(
                selectedTypeId = typeId,
                errorMessage = null
            )
    }

    fun save() {

        if (_uiState.value.isSaving) {
            return
        }

        val state =
            _uiState.value

        val validationError =
            validate(state)

        if (validationError != null) {

            _uiState.value =
                state.copy(
                    errorMessage =
                        validationError
                )

            return
        }

        val request =
            EkspedicijaRequest(
                naziv =
                    state.naziv.trim(),

                opis =
                    state.opis.trim(),

                datumPolaska =
                    state.datumPolaska.trim(),

                lokacija =
                    state.lokacija.trim(),

                tezina =
                    state.tezina,

                trajanjeMin =
                    state.trajanjeMin
                        .toInt(),

                duzinaKm =
                    state.duzinaKm
                        .replace(",", ".")
                        .toDouble(),

                externalUrl =
                    state.externalUrl
                        .trim()
                        .ifBlank {
                            null
                        },

                idTipEkspedicije =
                    state.selectedTypeId!!
            )

        _uiState.value =
            state.copy(
                isSaving = true,
                errorMessage = null
            )

        viewModelScope.launch {

            val result =
                if (
                    state.expeditionId == null
                ) {

                    repository.create(
                        request
                    )

                } else {

                    repository.update(
                        id =
                            state.expeditionId,
                        request =
                            request
                    )
                }

            when (result) {

                is ExpeditionCrudResult.Success -> {

                    _uiState.value =
                        _uiState.value.copy(
                            isSaving = false,
                            savedSuccessfully =
                                true
                        )
                }

                is ExpeditionCrudResult.Error -> {

                    _uiState.value =
                        _uiState.value.copy(
                            isSaving = false,
                            errorMessage =
                                result.message
                        )
                }

                ExpeditionCrudResult.Unauthorized -> {

                    _uiState.value =
                        ExpeditionFormUiState(
                            sessionExpired = true
                        )
                }
            }
        }
    }

    private fun validate(
        state: ExpeditionFormUiState
    ): String? {

        if (
            state.naziv.isBlank() ||
            state.opis.isBlank() ||
            state.datumPolaska.isBlank() ||
            state.lokacija.isBlank() ||
            state.trajanjeMin.isBlank() ||
            state.duzinaKm.isBlank()
        ) {
            return "Sva obavezna polja moraju biti popunjena."
        }

        if (state.naziv.trim().length > 120) {
            return "Naziv može imati najviše 120 karaktera."
        }

        if (state.lokacija.trim().length > 150) {
            return "Lokacija može imati najviše 150 karaktera."
        }

        if (state.externalUrl.trim().length > 500) {
            return "Link može imati najviše 500 karaktera."
        }

        val date =
            try {

                LocalDate.parse(
                    state.datumPolaska.trim()
                )

            } catch (_: Exception) {

                return "Datum mora biti u formatu YYYY-MM-DD."
            }

        if (date.isBefore(LocalDate.now())) {
            return "Datum polaska ne može biti u prošlosti."
        }

        if (
            state.tezina !in
            setOf(
                "EASY",
                "MEDIUM",
                "HARD"
            )
        ) {
            return "Izaberi težinu ekspedicije."
        }

        val duration =
            state.trajanjeMin.toIntOrNull()

        if (
            duration == null ||
            duration < 1
        ) {
            return "Trajanje mora biti pozitivan ceo broj."
        }

        val distance =
            state.duzinaKm
                .replace(",", ".")
                .toDoubleOrNull()

        if (
            distance == null ||
            distance < 0.01
        ) {
            return "Dužina mora biti najmanje 0.01 km."
        }

        if (state.selectedTypeId == null) {
            return "Izaberi tip ekspedicije."
        }

        return null
    }

    private fun update(
        transform:
        ExpeditionFormUiState.()
        -> ExpeditionFormUiState
    ) {

        _uiState.value =
            _uiState.value.transform()
    }

    fun reset() {

        _uiState.value =
            ExpeditionFormUiState()
    }
}