package com.himal.mobile.ui.feed

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Hiking
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.himal.mobile.data.remote.dto.EkspedicijaResponse

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

    /*
     * Veliki spinner koristimo samo prilikom
     * inicijalnog učitavanja Feed-a.
     */
    if (
        state.isLoading &&
        state.expeditions.isEmpty()
    ) {

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

    /*
     * Box nam omogućava da FAB lebdi
     * iznad LazyColumn sadržaja.
     */
    Box(
        modifier =
            Modifier.fillMaxSize()
    ) {

        LazyColumn(
            modifier =
                Modifier.fillMaxSize(),

            contentPadding =
                PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 20.dp,

                    /*
                     * Dodatni prostor na dnu da FAB
                     * ne prekrije poslednju karticu.
                     */
                    bottom = 110.dp
                ),

            verticalArrangement =
                Arrangement.spacedBy(
                    14.dp
                )
        ) {

            /*
             * HIMAL HEADER
             */
            item {

                FeedHeader()
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
             * FILTERING PROGRESS
             */
            if (state.isLoading) {

                item {

                    LinearProgressIndicator(
                        modifier =
                            Modifier.fillMaxWidth()
                    )
                }
            }

            /*
             * ERROR / VALIDATION
             */
            state.errorMessage?.let { message ->

                item {

                    Surface(
                        modifier =
                            Modifier.fillMaxWidth(),

                        shape =
                            RoundedCornerShape(
                                12.dp
                            ),

                        color =
                            MaterialTheme
                                .colorScheme
                                .errorContainer
                    ) {

                        Text(
                            text = message,

                            color =
                                MaterialTheme
                                    .colorScheme
                                    .onErrorContainer,

                            modifier =
                                Modifier.padding(
                                    14.dp
                                )
                        )
                    }
                }
            }

            /*
             * RESULTS HEADER
             */
            if (
                state.expeditions.isNotEmpty()
            ) {

                item {

                    Row(
                        modifier =
                            Modifier.fillMaxWidth(),

                        horizontalArrangement =
                            Arrangement.SpaceBetween,

                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Text(
                            text =
                                "Ekspedicije",

                            style =
                                MaterialTheme
                                    .typography
                                    .titleLarge
                        )

                        Text(
                            text =
                                "${state.expeditions.size} rezultata",

                            style =
                                MaterialTheme
                                    .typography
                                    .bodySmall,

                            color =
                                MaterialTheme
                                    .colorScheme
                                    .onSurfaceVariant
                        )
                    }
                }
            }

            /*
             * EMPTY STATE
             */
            if (
                !state.isLoading &&
                state.expeditions.isEmpty()
            ) {

                item {

                    EmptyFeedState(
                        hasFilters =
                            hasActiveFilters(
                                state
                            ),

                        onClear =
                            viewModel::clearFilters
                    )
                }

            } else {

                /*
                 * EXPEDITION CARDS
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
                                expedition
                                    .idEkspedicije
                            )
                        }
                    )
                }
            }
        }

        /*
         * FLOATING ACTION BUTTON
         */
        ExtendedFloatingActionButton(
            onClick =
                onMyExpeditionsClick,

            icon = {

                Icon(
                    imageVector =
                        Icons.Filled.Hiking,

                    contentDescription =
                        null
                )
            },

            text = {

                Text(
                    text =
                        "Moje ekspedicije"
                )
            },

            modifier =
                Modifier
                    .align(
                        Alignment.BottomEnd
                    )
                    .padding(
                        end = 20.dp,
                        bottom = 20.dp
                    ),

            containerColor =
                MaterialTheme
                    .colorScheme
                    .primary,

            contentColor =
                MaterialTheme
                    .colorScheme
                    .onPrimary
        )
    }
}

@Composable
private fun FeedHeader() {

    Column {

        Text(
            text =
                "HIMAL",

            style =
                MaterialTheme
                    .typography
                    .headlineLarge,

            color =
                MaterialTheme
                    .colorScheme
                    .primary
        )

        Spacer(
            modifier =
                Modifier.height(
                    2.dp
                )
        )

        Text(
            text =
                "Pronađi sledeću avanturu.",

            style =
                MaterialTheme
                    .typography
                    .bodyLarge,

            color =
                MaterialTheme
                    .colorScheme
                    .onSurfaceVariant
        )
    }
}

@Composable
private fun SearchSection(
    state: FeedUiState,
    viewModel: FeedViewModel
) {

    Card(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(
                18.dp
            ),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    MaterialTheme
                        .colorScheme
                        .surface
            )
    ) {

        Column(
            modifier =
                Modifier.padding(
                    16.dp
                )
        ) {

            OutlinedTextField(
                value =
                    state.search,

                onValueChange =
                    viewModel::onSearchChange,

                label = {

                    Text(
                        "Pretraži ekspedicije"
                    )
                },

                placeholder = {

                    Text(
                        "Naziv ili opis"
                    )
                },

                leadingIcon = {

                    Icon(
                        imageVector =
                            Icons.Filled.Search,

                        contentDescription =
                            null
                    )
                },

                modifier =
                    Modifier.fillMaxWidth(),

                singleLine =
                    true,

                shape =
                    RoundedCornerShape(
                        14.dp
                    )
            )

            Spacer(
                modifier =
                    Modifier.height(
                        12.dp
                    )
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
                        Modifier.weight(
                            1f
                        )
                ) {

                    Text(
                        "Pretraži"
                    )
                }

                OutlinedButton(
                    onClick =
                        viewModel::toggleFilters,

                    modifier =
                        Modifier.weight(
                            1f
                        )
                ) {

                    Text(
                        text =
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

@OptIn(
    ExperimentalMaterial3Api::class
)
@Composable
private fun FilterSection(
    state: FeedUiState,
    viewModel: FeedViewModel
) {

    var difficultyExpanded by
    remember {
        mutableStateOf(
            false
        )
    }

    var typeExpanded by
    remember {
        mutableStateOf(
            false
        )
    }

    val selectedType =
        state.expeditionTypes
            .firstOrNull {

                it.idTipEkspedicije ==
                        state.selectedTypeId
            }

    ElevatedCard(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(
                18.dp
            ),

        colors =
            CardDefaults
                .elevatedCardColors(
                    containerColor =
                        MaterialTheme
                            .colorScheme
                            .surface
                )
    ) {

        Column(
            modifier =
                Modifier.padding(
                    16.dp
                )
        ) {

            Text(
                text =
                    "Filtriraj ekspedicije",

                style =
                    MaterialTheme
                        .typography
                        .titleMedium
            )

            Spacer(
                modifier =
                    Modifier.height(
                        16.dp
                    )
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

                    Text(
                        "Lokacija"
                    )
                },

                modifier =
                    Modifier.fillMaxWidth(),

                singleLine =
                    true,

                shape =
                    RoundedCornerShape(
                        14.dp
                    )
            )

            Spacer(
                modifier =
                    Modifier.height(
                        12.dp
                    )
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
                        difficultyLabel(
                            state.difficulty
                        ),

                    onValueChange = {},

                    readOnly =
                        true,

                    label = {

                        Text(
                            "Težina"
                        )
                    },

                    trailingIcon = {

                        ExposedDropdownMenuDefaults
                            .TrailingIcon(
                                expanded =
                                    difficultyExpanded
                            )
                    },

                    modifier =
                        Modifier
                            .menuAnchor()
                            .fillMaxWidth(),

                    shape =
                        RoundedCornerShape(
                            14.dp
                        )
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

                            Text(
                                "Sve"
                            )
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

                    DropdownMenuItem(
                        text = {

                            Text(
                                "Laka"
                            )
                        },

                        onClick = {

                            viewModel
                                .onDifficultySelected(
                                    "EASY"
                                )

                            difficultyExpanded =
                                false
                        }
                    )

                    DropdownMenuItem(
                        text = {

                            Text(
                                "Srednja"
                            )
                        },

                        onClick = {

                            viewModel
                                .onDifficultySelected(
                                    "MEDIUM"
                                )

                            difficultyExpanded =
                                false
                        }
                    )

                    DropdownMenuItem(
                        text = {

                            Text(
                                "Teška"
                            )
                        },

                        onClick = {

                            viewModel
                                .onDifficultySelected(
                                    "HARD"
                                )

                            difficultyExpanded =
                                false
                        }
                    )
                }
            }

            Spacer(
                modifier =
                    Modifier.height(
                        12.dp
                    )
            )

            /*
             * TYPE
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

                    readOnly =
                        true,

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

                    modifier =
                        Modifier
                            .menuAnchor()
                            .fillMaxWidth(),

                    shape =
                        RoundedCornerShape(
                            14.dp
                        )
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

                            Text(
                                "Sve"
                            )
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
                                            type
                                                .idTipEkspedicije
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
                    Modifier.height(
                        12.dp
                    )
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

                keyboardOptions =
                    KeyboardOptions(
                        keyboardType =
                            KeyboardType.Number
                    ),

                modifier =
                    Modifier.fillMaxWidth(),

                singleLine =
                    true,

                shape =
                    RoundedCornerShape(
                        14.dp
                    )
            )

            Spacer(
                modifier =
                    Modifier.height(
                        12.dp
                    )
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

                keyboardOptions =
                    KeyboardOptions(
                        keyboardType =
                            KeyboardType.Decimal
                    ),

                modifier =
                    Modifier.fillMaxWidth(),

                singleLine =
                    true,

                shape =
                    RoundedCornerShape(
                        14.dp
                    )
            )

            Spacer(
                modifier =
                    Modifier.height(
                        16.dp
                    )
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
                        Modifier.weight(
                            1f
                        )
                ) {

                    Text(
                        "Očisti"
                    )
                }

                Button(
                    onClick =
                        viewModel::applyFilters,

                    modifier =
                        Modifier.weight(
                            1f
                        )
                ) {

                    Text(
                        "Primeni"
                    )
                }
            }
        }
    }
}

@Composable
private fun ExpeditionCard(
    expedition:
    EkspedicijaResponse,

    onClick: () -> Unit
) {

    ElevatedCard(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable(
                    onClick =
                        onClick
                ),

        shape =
            RoundedCornerShape(
                18.dp
            ),

        colors =
            CardDefaults
                .elevatedCardColors(
                    containerColor =
                        MaterialTheme
                            .colorScheme
                            .surface
                )
    ) {

        Column(
            modifier =
                Modifier.padding(
                    18.dp
                )
        ) {

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.SpaceBetween,

                verticalAlignment =
                    Alignment.Top
            ) {

                Text(
                    text =
                        expedition.naziv,

                    style =
                        MaterialTheme
                            .typography
                            .titleLarge,

                    modifier =
                        Modifier.weight(
                            1f
                        )
                )

                Spacer(
                    modifier =
                        Modifier.padding(
                            horizontal =
                                4.dp
                        )
                )

                DifficultyBadge(
                    difficulty =
                        expedition.tezina
                )
            }

            Spacer(
                modifier =
                    Modifier.height(
                        6.dp
                    )
            )

            Text(
                text =
                    expedition.lokacija,

                style =
                    MaterialTheme
                        .typography
                        .bodyMedium,

                color =
                    MaterialTheme
                        .colorScheme
                        .primary
            )

            Spacer(
                modifier =
                    Modifier.height(
                        10.dp
                    )
            )

            Text(
                text =
                    expedition.opis,

                style =
                    MaterialTheme
                        .typography
                        .bodyMedium,

                color =
                    MaterialTheme
                        .colorScheme
                        .onSurfaceVariant,

                maxLines =
                    2,

                overflow =
                    TextOverflow.Ellipsis
            )

            Spacer(
                modifier =
                    Modifier.height(
                        14.dp
                    )
            )

            /*
             * TYPE CHIP
             */
            Surface(
                shape =
                    RoundedCornerShape(
                        50.dp
                    ),

                color =
                    MaterialTheme
                        .colorScheme
                        .secondaryContainer
            ) {

                Text(
                    text =
                        expedition
                            .tipEkspedicije,

                    style =
                        MaterialTheme
                            .typography
                            .labelMedium,

                    color =
                        MaterialTheme
                            .colorScheme
                            .onSecondaryContainer,

                    modifier =
                        Modifier.padding(
                            horizontal =
                                10.dp,

                            vertical =
                                5.dp
                        )
                )
            }

            Spacer(
                modifier =
                    Modifier.height(
                        14.dp
                    )
            )

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.spacedBy(
                        8.dp
                    )
            ) {

                MetricBox(
                    label =
                        "Dužina",

                    value =
                        "${expedition.duzinaKm} km",

                    modifier =
                        Modifier.weight(
                            1f
                        )
                )

                MetricBox(
                    label =
                        "Trajanje",

                    value =
                        "${expedition.trajanjeMin} min",

                    modifier =
                        Modifier.weight(
                            1f
                        )
                )
            }

            Spacer(
                modifier =
                    Modifier.height(
                        12.dp
                    )
            )

            Text(
                text =
                    "Autor: ${expedition.autorUsername}",

                style =
                    MaterialTheme
                        .typography
                        .bodySmall,

                color =
                    MaterialTheme
                        .colorScheme
                        .onSurfaceVariant
            )
        }
    }
}

@Composable
private fun DifficultyBadge(
    difficulty: String
) {

    val containerColor =
        when (difficulty) {

            "EASY" ->

                MaterialTheme
                    .colorScheme
                    .primaryContainer

            "MEDIUM" ->

                MaterialTheme
                    .colorScheme
                    .secondaryContainer

            "HARD" ->

                MaterialTheme
                    .colorScheme
                    .errorContainer

            else ->

                MaterialTheme
                    .colorScheme
                    .surfaceVariant
        }

    val contentColor =
        when (difficulty) {

            "EASY" ->

                MaterialTheme
                    .colorScheme
                    .onPrimaryContainer

            "MEDIUM" ->

                MaterialTheme
                    .colorScheme
                    .onSecondaryContainer

            "HARD" ->

                MaterialTheme
                    .colorScheme
                    .onErrorContainer

            else ->

                MaterialTheme
                    .colorScheme
                    .onSurfaceVariant
        }

    Surface(
        color =
            containerColor,

        shape =
            RoundedCornerShape(
                50.dp
            )
    ) {

        Text(
            text =
                difficultyLabel(
                    difficulty
                ),

            style =
                MaterialTheme
                    .typography
                    .labelMedium,

            color =
                contentColor,

            modifier =
                Modifier.padding(
                    horizontal =
                        10.dp,

                    vertical =
                        5.dp
                )
        )
    }
}

@Composable
private fun MetricBox(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {

    Surface(
        modifier =
            modifier,

        shape =
            RoundedCornerShape(
                12.dp
            ),

        color =
            MaterialTheme
                .colorScheme
                .surfaceVariant
    ) {

        Column(
            modifier =
                Modifier.padding(
                    horizontal =
                        12.dp,

                    vertical =
                        9.dp
                )
        ) {

            Text(
                text =
                    label,

                style =
                    MaterialTheme
                        .typography
                        .bodySmall,

                color =
                    MaterialTheme
                        .colorScheme
                        .onSurfaceVariant
            )

            Text(
                text =
                    value,

                style =
                    MaterialTheme
                        .typography
                        .labelLarge,

                color =
                    MaterialTheme
                        .colorScheme
                        .onSurface
            )
        }
    }
}

@Composable
private fun EmptyFeedState(
    hasFilters: Boolean,
    onClear: () -> Unit
) {

    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(
                    vertical =
                        40.dp
                ),

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Text(
            text =
                if (hasFilters) {

                    "Nema ekspedicija koje odgovaraju filterima."

                } else {

                    "Trenutno nema dostupnih ekspedicija."
                },

            style =
                MaterialTheme
                    .typography
                    .bodyLarge,

            color =
                MaterialTheme
                    .colorScheme
                    .onSurfaceVariant
        )

        if (hasFilters) {

            Spacer(
                modifier =
                    Modifier.height(
                        12.dp
                    )
            )

            OutlinedButton(
                onClick =
                    onClear
            ) {

                Text(
                    "Prikaži sve"
                )
            }
        }
    }
}

private fun difficultyLabel(
    difficulty: String?
): String {

    return when (difficulty) {

        "EASY" ->
            "Laka"

        "MEDIUM" ->
            "Srednja"

        "HARD" ->
            "Teška"

        else ->
            "Sve"
    }
}

private fun hasActiveFilters(
    state: FeedUiState
): Boolean {

    return state.search.isNotBlank() ||
            state.location.isNotBlank() ||
            state.difficulty != null ||
            state.selectedTypeId != null ||
            state.maxDuration.isNotBlank() ||
            state.maxDistance.isNotBlank()
}