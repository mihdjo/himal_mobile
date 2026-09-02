package com.himal.mobile.ui.expeditionform

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpeditionFormScreen(
    viewModel: ExpeditionFormViewModel,
    expeditionId: Long?,
    onSaved: () -> Unit,
    onCancel: () -> Unit,
    onSessionExpired: () -> Unit
) {

    val state by
    viewModel.uiState.collectAsState()

    var typeMenuExpanded by
    remember {
        mutableStateOf(false)
    }

    LaunchedEffect(expeditionId) {

        if (expeditionId == null) {

            viewModel.prepareCreate()

        } else {

            viewModel.loadForEdit(
                expeditionId
            )
        }
    }

    LaunchedEffect(
        state.savedSuccessfully
    ) {

        if (state.savedSuccessfully) {
            onSaved()
        }
    }

    LaunchedEffect(
        state.sessionExpired
    ) {

        if (state.sessionExpired) {
            onSessionExpired()
        }
    }

    if (state.isLoading) {

        Column(
            modifier =
                Modifier.fillMaxSize(),
            verticalArrangement =
                Arrangement.Center,
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }

        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState()
            )
            .padding(16.dp)
    ) {

        Text(
            text =
                if (expeditionId == null)
                    "Nova ekspedicija"
                else
                    "Izmeni ekspediciju",

            style =
                MaterialTheme.typography.headlineMedium
        )

        Spacer(
            modifier =
                Modifier.height(20.dp)
        )

        OutlinedTextField(
            value = state.naziv,
            onValueChange =
                viewModel::onNazivChange,
            label = {
                Text("Naziv")
            },
            modifier =
                Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = state.opis,
            onValueChange =
                viewModel::onOpisChange,
            label = {
                Text("Opis")
            },
            modifier =
                Modifier.fillMaxWidth(),
            minLines = 3
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = state.datumPolaska,
            onValueChange =
                viewModel::onDatumPolaskaChange,
            label = {
                Text("Datum polaska")
            },
            supportingText = {
                Text("YYYY-MM-DD")
            },
            modifier =
                Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = state.lokacija,
            onValueChange =
                viewModel::onLokacijaChange,
            label = {
                Text("Lokacija")
            },
            modifier =
                Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = "Težina",
            style =
                MaterialTheme.typography.titleMedium
        )

        Spacer(Modifier.height(8.dp))

        Row(
            modifier =
                Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.spacedBy(6.dp)
        ) {

            listOf(
                "EASY",
                "MEDIUM",
                "HARD"
            ).forEach { difficulty ->

                if (
                    state.tezina ==
                    difficulty
                ) {

                    Button(
                        onClick = {
                            viewModel
                                .onTezinaChange(
                                    difficulty
                                )
                        },
                        modifier =
                            Modifier.weight(1f)
                    ) {
                        Text(difficulty)
                    }

                } else {

                    OutlinedButton(
                        onClick = {
                            viewModel
                                .onTezinaChange(
                                    difficulty
                                )
                        },
                        modifier =
                            Modifier.weight(1f)
                    ) {
                        Text(difficulty)
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = state.trajanjeMin,
            onValueChange =
                viewModel::onTrajanjeMinChange,
            label = {
                Text("Trajanje u minutima")
            },
            modifier =
                Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = state.duzinaKm,
            onValueChange =
                viewModel::onDuzinaKmChange,
            label = {
                Text("Dužina u km")
            },
            modifier =
                Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(Modifier.height(12.dp))

        val selectedType =
            state.expeditionTypes
                .firstOrNull {
                    it.idTipEkspedicije ==
                            state.selectedTypeId
                }

        ExposedDropdownMenuBox(
            expanded = typeMenuExpanded,
            onExpandedChange = {

                if (!state.isLoadingTypes) {
                    typeMenuExpanded =
                        !typeMenuExpanded
                }
            }
        ) {

            OutlinedTextField(
                value =
                    selectedType?.tip.orEmpty(),

                onValueChange = {},

                readOnly = true,

                label = {
                    Text("Tip ekspedicije")
                },

                placeholder = {

                    Text(
                        if (state.isLoadingTypes)
                            "Učitavanje tipova..."
                        else
                            "Izaberi tip"
                    )
                },

                trailingIcon = {

                    ExposedDropdownMenuDefaults
                        .TrailingIcon(
                            expanded =
                                typeMenuExpanded
                        )
                },

                enabled =
                    !state.isLoadingTypes,

                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded =
                    typeMenuExpanded,

                onDismissRequest = {
                    typeMenuExpanded = false
                }
            ) {

                state.expeditionTypes
                    .forEach { type ->

                        DropdownMenuItem(
                            text = {
                                Text(type.tip)
                            },

                            onClick = {

                                viewModel.onTypeSelected(
                                    type.idTipEkspedicije
                                )

                                typeMenuExpanded = false
                            }
                        )
                    }
            }
        }

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value =
                state.externalUrl,
            onValueChange =
                viewModel::onExternalUrlChange,
            label = {
                Text("External URL (opciono)")
            },
            modifier =
                Modifier.fillMaxWidth(),
            singleLine = true
        )

        state.errorMessage?.let {

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            Text(
                text = it,
                color =
                    MaterialTheme.colorScheme.error
            )
        }

        Spacer(
            modifier =
                Modifier.height(20.dp)
        )

        Row(
            modifier =
                Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {

            OutlinedButton(
                onClick = onCancel,
                enabled =
                    !state.isSaving,
                modifier =
                    Modifier.weight(1f)
            ) {
                Text("Otkaži")
            }

            Button(
                onClick =
                    viewModel::save,
                enabled =
                    !state.isSaving,
                modifier =
                    Modifier.weight(1f)
            ) {

                if (state.isSaving) {

                    CircularProgressIndicator()

                } else {

                    Text(
                        if (
                            expeditionId == null
                        )
                            "Kreiraj"
                        else
                            "Sačuvaj"
                    )
                }
            }
        }

        Spacer(
            modifier =
                Modifier.height(32.dp)
        )
    }
}