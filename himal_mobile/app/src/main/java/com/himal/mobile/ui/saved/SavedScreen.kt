package com.himal.mobile.ui.saved

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.himal.mobile.data.remote.dto.EkspedicijaResponse

@Composable
fun SavedScreen(
    viewModel: SavedViewModel,
    onExpeditionClick: (Long) -> Unit,
    onSessionExpired: () -> Unit
) {

    val state by
    viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadSaved()
    }

    LaunchedEffect(
        state.sessionExpired
    ) {

        if (state.sessionExpired) {
            onSessionExpired()
        }
    }

    when {

        state.isLoading -> {

            SavedLoading()
        }

        state.errorMessage != null -> {

            SavedError(
                message =
                    state.errorMessage
                        ?: "Nepoznata greška.",

                onRetry =
                    viewModel::loadSaved
            )
        }

        state.expeditions.isEmpty() -> {

            EmptySavedState()
        }

        else -> {

            LazyColumn(
                modifier =
                    Modifier.fillMaxSize(),

                contentPadding =
                    PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 20.dp,
                        bottom = 28.dp
                    ),

                verticalArrangement =
                    Arrangement.spacedBy(
                        14.dp
                    )
            ) {

                item {

                    SavedHeader(
                        count =
                            state.expeditions.size
                    )
                }

                items(
                    items =
                        state.expeditions,

                    key = {
                        it.idEkspedicije
                    }
                ) { expedition ->

                    SavedExpeditionCard(
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
        }
    }
}

@Composable
private fun SavedHeader(
    count: Int
) {

    Column {

        Text(
            text =
                "Sačuvane",

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
                "Ekspedicije koje želiš da sačuvaš za kasnije.",

            style =
                MaterialTheme
                    .typography
                    .bodyLarge,

            color =
                MaterialTheme
                    .colorScheme
                    .onSurfaceVariant
        )

        Spacer(
            modifier =
                Modifier.height(
                    14.dp
                )
        )

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

            Row(
                modifier =
                    Modifier.padding(
                        horizontal = 12.dp,
                        vertical = 7.dp
                    ),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Icon(
                    imageVector =
                        Icons.Filled.Favorite,

                    contentDescription =
                        null,

                    tint =
                        MaterialTheme
                            .colorScheme
                            .secondary,

                    modifier =
                        Modifier.size(
                            17.dp
                        )
                )

                Spacer(
                    modifier =
                        Modifier.size(
                            6.dp
                        )
                )

                Text(
                    text =
                        "$count sačuvanih",

                    style =
                        MaterialTheme
                            .typography
                            .labelLarge,

                    color =
                        MaterialTheme
                            .colorScheme
                            .onSecondaryContainer
                )
            }
        }
    }
}

@Composable
private fun SavedExpeditionCard(
    expedition: EkspedicijaResponse,
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

                    maxLines =
                        2,

                    overflow =
                        TextOverflow.Ellipsis,

                    modifier =
                        Modifier.weight(
                            1f
                        )
                )

                Spacer(
                    modifier =
                        Modifier.size(
                            8.dp
                        )
                )

                Icon(
                    imageVector =
                        Icons.Filled.Favorite,

                    contentDescription =
                        null,

                    tint =
                        MaterialTheme
                            .colorScheme
                            .secondary,

                    modifier =
                        Modifier.size(
                            20.dp
                        )
                )
            }

            Spacer(
                modifier =
                    Modifier.height(
                        8.dp
                    )
            )

            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Icon(
                    imageVector =
                        Icons.Filled.LocationOn,

                    contentDescription =
                        null,

                    tint =
                        MaterialTheme
                            .colorScheme
                            .primary,

                    modifier =
                        Modifier.size(
                            17.dp
                        )
                )

                Spacer(
                    modifier =
                        Modifier.size(
                            4.dp
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
            }

            Spacer(
                modifier =
                    Modifier.height(
                        12.dp
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

            Row(
                horizontalArrangement =
                    Arrangement.spacedBy(
                        8.dp
                    )
            ) {

                DifficultyBadge(
                    difficulty =
                        expedition.tezina
                )

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

                SavedMetric(
                    label =
                        "Dužina",

                    value =
                        "${expedition.duzinaKm} km",

                    modifier =
                        Modifier.weight(
                            1f
                        )
                )

                SavedMetric(
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
                        10.dp
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
private fun SavedMetric(
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
                        .labelLarge
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
        shape =
            RoundedCornerShape(
                50.dp
            ),

        color =
            containerColor
    ) {

        Text(
            text =
                when (difficulty) {

                    "EASY" ->
                        "Laka"

                    "MEDIUM" ->
                        "Srednja"

                    "HARD" ->
                        "Teška"

                    else ->
                        difficulty
                },

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
private fun EmptySavedState() {

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(
                    32.dp
                ),

        verticalArrangement =
            Arrangement.Center,

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

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

            Icon(
                imageVector =
                    Icons.Filled.Favorite,

                contentDescription =
                    null,

                tint =
                    MaterialTheme
                        .colorScheme
                        .secondary,

                modifier =
                    Modifier.padding(
                        18.dp
                    )
            )
        }

        Spacer(
            modifier =
                Modifier.height(
                    18.dp
                )
        )

        Text(
            text =
                "Nema sačuvanih ekspedicija",

            style =
                MaterialTheme
                    .typography
                    .headlineSmall
        )

        Spacer(
            modifier =
                Modifier.height(
                    6.dp
                )
        )

        Text(
            text =
                "Ekspedicije koje sačuvaš pojaviće se ovde.",

            style =
                MaterialTheme
                    .typography
                    .bodyMedium,

            color =
                MaterialTheme
                    .colorScheme
                    .onSurfaceVariant
        )
    }
}

@Composable
private fun SavedLoading() {

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
}

@Composable
private fun SavedError(
    message: String,
    onRetry: () -> Unit
) {

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(
                    24.dp
                ),

        verticalArrangement =
            Arrangement.Center,

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Text(
            text =
                message,

            color =
                MaterialTheme
                    .colorScheme
                    .error
        )

        Spacer(
            modifier =
                Modifier.height(
                    16.dp
                )
        )

        Button(
            onClick =
                onRetry
        ) {

            Text(
                "Pokušaj ponovo"
            )
        }
    }
}