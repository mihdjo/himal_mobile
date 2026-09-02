package com.himal.mobile.ui.expeditionform

import com.himal.mobile.data.remote.dto.TipEkspedicijeResponse

data class ExpeditionFormUiState(
    val expeditionId: Long? = null,

    val naziv: String = "",
    val opis: String = "",
    val datumPolaska: String = "",
    val lokacija: String = "",
    val tezina: String = "MEDIUM",
    val trajanjeMin: String = "",
    val duzinaKm: String = "",
    val externalUrl: String = "",

    val expeditionTypes:
    List<TipEkspedicijeResponse> = emptyList(),

    val selectedTypeId: Long? = null,

    val isLoading: Boolean = false,
    val isLoadingTypes: Boolean = false,
    val isSaving: Boolean = false,

    val savedSuccessfully: Boolean = false,
    val errorMessage: String? = null,
    val sessionExpired: Boolean = false
)