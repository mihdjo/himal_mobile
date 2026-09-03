package com.himal.mobile.ui.feed

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.himal.mobile.data.remote.dto.EkspedicijaResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    viewModel: FeedViewModel,
    onLogout: () -> Unit,
    onExpeditionClick: (Long) -> Unit,
    onMyExpeditionsClick: () -> Unit
) {

    val state by
    viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadFeed()
    }

    LaunchedEffect(
        state.sessionExpired
    ) {

        if (state.sessionExpired) {
            onLogout()
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

    LazyColumn(
        modifier =
            Modifier.fillMaxSize(),

        contentPadding =
            PaddingValues(16.dp),

        verticalArrangement =
            Arrangement.spacedBy(12.dp)
    ) {

        /*
         * HEADER
         */
        item {

            Text(
                text = "HIMAL Expeditions",
                style =
                    MaterialTheme
                        .typography
                        .headlineMedium
            )

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            Button(
                onClick =
                    onMyExpeditionsClick,
                modifier =
                    Modifier.fillMaxWidth()
            ) {

                Text("Moje ekspedicije")
            }

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )
        }

        /*
         * SEARCH
         */
        item {

            SearchSection(
                state = state,
                viewModel = viewModel
            )
        }

        /*
         * FILTERS
         */
        if (state.showFilters) {

            item {

                FilterSection(
                    state = state,
                    viewModel = viewModel
                )
            }
        }

        /*
         * ERROR / VALIDATION MESSAGE
         */
        state.errorMessage?.let { message ->

            item {

                Card(
                    modifier =
                        Modifier.fillMaxWidth()
                ) {

                    Text(
                        text = message,
                        color =
                            MaterialTheme
                                .colorScheme
                                .error,
                        modifier =
                            Modifier.padding(16.dp)
                    )
                }
            }
        }

        /*
         * EMPTY RESULT
         */
        if (
            state.expeditions.isEmpty()
        ) {

            item {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = 32.dp
                        ),
                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {

                    Text(
                        text =
                            if (
                                state.search.isNotBlank() ||
                                state.location.isNotBlank() ||
                                state.difficulty != null ||
                                state.selectedTypeId != null ||
                                state.maxDuration.isNotBlank() ||
                                state.maxDistance.isNotBlank()
                            ) {
                                "Nema ekspedicija koje odgovaraju pretrazi."
                            } else {
                                "Trenutno nema dostupnih ekspedicija."
                            }
                    )

                    Spacer(
                        modifier =
                            Modifier.height(12.dp)
                    )

                    OutlinedButton(
                        onClick =
                            viewModel::clearFilters
                    ) {

                        Text(
                            "Prikaži sve ekspedicije"
                        )
                    }
                }
            }

        } else {

            /*
             * EXPEDITION RESULTS
             */
            items(
                items =
                    state.expeditions,

                key = {
                    it.idEkspedicije
                }
            ) { expedition ->

                ExpeditionCard(
                    expedition =
                        expedition,

                    onClick = {

                        onExpeditionClick(
                            expedition.idEkspedicije
                        )
                    }
                )
            }
        }

        item {

            Spacer(
                modifier =
                    Modifier.height(16.dp)
            )
        }
    }
}

@Composable
private fun SearchSection(
    state: FeedUiState,
    viewModel: FeedViewModel
) {

    Card(
        modifier =
            Modifier.fillMaxWidth()
    ) {

        Column(
            modifier =
                Modifier.padding(16.dp)
        ) {

            Text(
                text = "Pretraga",
                style =
                    MaterialTheme
                        .typography
                        .titleLarge
            )

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            OutlinedTextField(
                value =
                    state.search,

                onValueChange =
                    viewModel::onSearchChange,

                label = {
                    Text(
                        "Naziv ili opis"
                    )
                },

                modifier =
                    Modifier.fillMaxWidth(),

                singleLine = true
            )

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.spacedBy(
                        8.dp
                    )
            ) {

                Button(
                    onClick =
                        viewModel::applyFilters,

                    modifier =
                        Modifier.weight(1f)
                ) {

                    Text("Pretraži")
                }

                OutlinedButton(
                    onClick =
                        viewModel::toggleFilters,

                    modifier =
                        Modifier.weight(1f)
                ) {

                    Text(
                        if (
                            state.showFilters
                        ) {
                            "Sakrij filtere"
                        } else {
                            "Filteri"
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterSection(
    state: FeedUiState,
    viewModel: FeedViewModel
) {

    var difficultyExpanded by
    remember {
        mutableStateOf(false)
    }

    var typeExpanded by
    remember {
        mutableStateOf(false)
    }

    val selectedType =
        state.expeditionTypes
            .firstOrNull {
                it.idTipEkspedicije ==
                        state.selectedTypeId
            }

    Card(
        modifier =
            Modifier.fillMaxWidth()
    ) {

        Column(
            modifier =
                Modifier.padding(16.dp)
        ) {

            Text(
                text = "Filteri",
                style =
                    MaterialTheme
                        .typography
                        .titleLarge
            )

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            /*
             * LOCATION
             */
            OutlinedTextField(
                value =
                    state.location,

                onValueChange =
                    viewModel::onLocationChange,

                label = {
                    Text("Lokacija")
                },

                modifier =
                    Modifier.fillMaxWidth(),

                singleLine = true
            )

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            /*
             * DIFFICULTY
             */
            ExposedDropdownMenuBox(
                expanded =
                    difficultyExpanded,

                onExpandedChange = {

                    difficultyExpanded =
                        !difficultyExpanded
                }
            ) {

                OutlinedTextField(
                    value =
                        state.difficulty
                            ?: "Sve",

                    onValueChange = {},

                    readOnly = true,

                    label = {
                        Text("Težina")
                    },

                    trailingIcon = {

                        ExposedDropdownMenuDefaults
                            .TrailingIcon(
                                expanded =
                                    difficultyExpanded
                            )
                    },

                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded =
                        difficultyExpanded,

                    onDismissRequest = {

                        difficultyExpanded =
                            false
                    }
                ) {

                    DropdownMenuItem(
                        text = {
                            Text("Sve")
                        },

                        onClick = {

                            viewModel
                                .onDifficultySelected(
                                    null
                                )

                            difficultyExpanded =
                                false
                        }
                    )

                    listOf(
                        "EASY",
                        "MEDIUM",
                        "HARD"
                    ).forEach {
                            difficulty ->

                        DropdownMenuItem(
                            text = {

                                Text(
                                    difficulty
                                )
                            },

                            onClick = {

                                viewModel
                                    .onDifficultySelected(
                                        difficulty
                                    )

                                difficultyExpanded =
                                    false
                            }
                        )
                    }
                }
            }

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            /*
             * EXPEDITION TYPE
             */
            ExposedDropdownMenuBox(
                expanded =
                    typeExpanded,

                onExpandedChange = {

                    typeExpanded =
                        !typeExpanded
                }
            ) {

                OutlinedTextField(
                    value =
                        selectedType?.tip
                            ?: "Sve",

                    onValueChange = {},

                    readOnly = true,

                    label = {

                        Text(
                            "Tip ekspedicije"
                        )
                    },

                    trailingIcon = {

                        ExposedDropdownMenuDefaults
                            .TrailingIcon(
                                expanded =
                                    typeExpanded
                            )
                    },

                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded =
                        typeExpanded,

                    onDismissRequest = {

                        typeExpanded =
                            false
                    }
                ) {

                    DropdownMenuItem(
                        text = {

                            Text("Sve")
                        },

                        onClick = {

                            viewModel
                                .onTypeSelected(
                                    null
                                )

                            typeExpanded =
                                false
                        }
                    )

                    state.expeditionTypes
                        .forEach { type ->

                            DropdownMenuItem(
                                text = {

                                    Text(
                                        type.tip
                                    )
                                },

                                onClick = {

                                    viewModel
                                        .onTypeSelected(
                                            type.idTipEkspedicije
                                        )

                                    typeExpanded =
                                        false
                                }
                            )
                        }
                }
            }

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            /*
             * MAX DURATION
             */
            OutlinedTextField(
                value =
                    state.maxDuration,

                onValueChange =
                    viewModel::
                    onMaxDurationChange,

                label = {

                    Text(
                        "Maks. trajanje (min)"
                    )
                },

                modifier =
                    Modifier.fillMaxWidth(),

                singleLine = true
            )

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            /*
             * MAX DISTANCE
             */
            OutlinedTextField(
                value =
                    state.maxDistance,

                onValueChange =
                    viewModel::
                    onMaxDistanceChange,

                label = {

                    Text(
                        "Maks. dužina (km)"
                    )
                },

                modifier =
                    Modifier.fillMaxWidth(),

                singleLine = true
            )

            Spacer(
                modifier =
                    Modifier.height(16.dp)
            )

            /*
             * ACTIONS
             */
            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.spacedBy(
                        8.dp
                    )
            ) {

                OutlinedButton(
                    onClick =
                        viewModel::clearFilters,

                    modifier =
                        Modifier.weight(1f)
                ) {

                    Text("Očisti")
                }

                Button(
                    onClick =
                        viewModel::applyFilters,

                    modifier =
                        Modifier.weight(1f)
                ) {

                    Text("Primeni")
                }
            }
        }
    }
}

@Composable
private fun ExpeditionCard(
    expedition: EkspedicijaResponse,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick
            )
    ) {

        Column(
            modifier =
                Modifier.padding(16.dp)
        ) {

            Text(
                text =
                    expedition.naziv,

                style =
                    MaterialTheme
                        .typography
                        .titleLarge
            )

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            Text(
                text =
                    expedition.lokacija
            )

            Spacer(
                modifier =
                    Modifier.height(4.dp)
            )

            Text(
                text =
                    "Težina: ${expedition.tezina}"
            )

            Text(
                text =
                    "Tip: ${expedition.tipEkspedicije}"
            )

            Text(
                text =
                    "Trajanje: ${expedition.trajanjeMin} min"
            )

            Text(
                text =
                    "Dužina: ${expedition.duzinaKm} km"
            )

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            Text(
                text =
                    "Autor: ${expedition.autorUsername}",

                style =
                    MaterialTheme
                        .typography
                        .bodySmall
            )
        }
    }
}