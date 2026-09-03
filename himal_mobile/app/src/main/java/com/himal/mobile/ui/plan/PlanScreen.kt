package com.himal.mobile.ui.plan

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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.himal.mobile.data.remote.dto.MojPlanResponse

@Composable
fun PlanScreen(
    viewModel: PlanViewModel,
    onExpeditionClick: (Long) -> Unit,
    onPackingListClick: () -> Unit,
    onSessionExpired: () -> Unit
) {

    val state by
    viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadPlan()
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

            LoadingPlan()
        }

        state.errorMessage != null -> {

            PlanError(
                message =
                    state.errorMessage
                        ?: "Nepoznata greška.",
                onRetry =
                    viewModel::loadPlan
            )
        }

        state.items.isEmpty() -> {

            EmptyPlan()
        }

        else -> {

            val readyCount =
                state.items.count {
                    it.status
                }

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

                    PlanHeader()
                }

                item {

                    PlanSummary(
                        readyCount =
                            readyCount,

                        totalCount =
                            state.items.size
                    )
                }

                item {

                    FilledTonalButton(
                        onClick =
                            onPackingListClick,

                        modifier =
                            Modifier.fillMaxWidth()
                    ) {

                        Icon(
                            imageVector =
                                Icons.Filled.List,

                            contentDescription =
                                null
                        )

                        Spacer(
                            modifier =
                                Modifier.size(
                                    8.dp
                                )
                        )

                        Text(
                            "Otvori packing listu"
                        )
                    }
                }

                item {

                    Text(
                        text =
                            "Planirane ekspedicije",

                        style =
                            MaterialTheme
                                .typography
                                .titleLarge
                    )
                }

                items(
                    items =
                        state.items,

                    key = {
                        it.ekspedicija
                            .idEkspedicije
                    }
                ) { item ->

                    PlanExpeditionCard(
                        item =
                            item,

                        onClick = {

                            onExpeditionClick(
                                item
                                    .ekspedicija
                                    .idEkspedicije
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun PlanHeader() {

    Column {

        Text(
            text =
                "Moj plan",

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
                "Prati spremnost svojih sledećih avantura.",

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
private fun PlanSummary(
    readyCount: Int,
    totalCount: Int
) {

    Surface(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(
                18.dp
            ),

        color =
            if (
                readyCount == totalCount
            ) {

                MaterialTheme
                    .colorScheme
                    .primaryContainer

            } else {

                MaterialTheme
                    .colorScheme
                    .surfaceVariant
            }
    ) {

        Row(
            modifier =
                Modifier.padding(
                    18.dp
                ),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Surface(
                shape =
                    RoundedCornerShape(
                        50.dp
                    ),

                color =
                    MaterialTheme
                        .colorScheme
                        .surface
            ) {

                Icon(
                    imageVector =
                        Icons.Filled.CheckCircle,

                    contentDescription =
                        null,

                    tint =
                        MaterialTheme
                            .colorScheme
                            .primary,

                    modifier =
                        Modifier.padding(
                            10.dp
                        )
                )
            }

            Spacer(
                modifier =
                    Modifier.size(
                        14.dp
                    )
            )

            Column {

                Text(
                    text =
                        "$readyCount od $totalCount spremno",

                    style =
                        MaterialTheme
                            .typography
                            .titleMedium
                )

                Text(
                    text =
                        if (
                            readyCount ==
                            totalCount
                        ) {
                            "Sve planirane ekspedicije su spremne."
                        } else {
                            "Proveri obaveznu opremu pre polaska."
                        },

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
    }
}

@Composable
private fun PlanExpeditionCard(
    item: MojPlanResponse,
    onClick: () -> Unit
) {

    val expedition =
        item.ekspedicija

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
                        if (item.status) {

                            MaterialTheme
                                .colorScheme
                                .primaryContainer

                        } else {

                            MaterialTheme
                                .colorScheme
                                .surface
                        }
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

                ReadinessBadge(
                    ready =
                        item.status
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

                PlanMetric(
                    label =
                        "Dužina",

                    value =
                        "${expedition.duzinaKm} km",

                    modifier =
                        Modifier.weight(
                            1f
                        )
                )

                PlanMetric(
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
        }
    }
}

@Composable
private fun ReadinessBadge(
    ready: Boolean
) {

    Surface(
        shape =
            RoundedCornerShape(
                50.dp
            ),

        color =
            if (ready) {

                MaterialTheme
                    .colorScheme
                    .primary

            } else {

                MaterialTheme
                    .colorScheme
                    .secondaryContainer
            }
    ) {

        Text(
            text =
                if (ready) {
                    "Spremna"
                } else {
                    "Priprema"
                },

            style =
                MaterialTheme
                    .typography
                    .labelMedium,

            color =
                if (ready) {

                    MaterialTheme
                        .colorScheme
                        .onPrimary

                } else {

                    MaterialTheme
                        .colorScheme
                        .onSecondaryContainer
                },

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
private fun PlanMetric(
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
                .surface
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
private fun EmptyPlan() {

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
                    .primaryContainer
        ) {

            Icon(
                imageVector =
                    Icons.Filled.List,

                contentDescription =
                    null,

                tint =
                    MaterialTheme
                        .colorScheme
                        .primary,

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
                "Moj plan je prazan",

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
                "Dodaj ekspedicije u plan i ovde prati spremnost opreme.",

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
private fun LoadingPlan() {

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
private fun PlanError(
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