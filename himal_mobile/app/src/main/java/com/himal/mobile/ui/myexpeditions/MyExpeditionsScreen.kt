package com.himal.mobile.ui.myexpeditions

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Hiking
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.himal.mobile.data.remote.dto.EkspedicijaResponse

@Composable
fun MyExpeditionsScreen(
    viewModel: MyExpeditionsViewModel,
    onCreateClick: () -> Unit,
    onOpenClick: (Long) -> Unit,
    onEditClick: (Long) -> Unit,
    onEquipmentClick: (Long) -> Unit,
    onSessionExpired: () -> Unit
) {

    val state by
    viewModel.uiState.collectAsState()

    var expeditionToDelete by
    remember {
        mutableStateOf<EkspedicijaResponse?>(
            null
        )
    }

    LaunchedEffect(Unit) {
        viewModel.loadExpeditions()
    }

    LaunchedEffect(
        state.sessionExpired
    ) {

        if (state.sessionExpired) {
            onSessionExpired()
        }
    }

    expeditionToDelete
        ?.let { expedition ->

            DeleteExpeditionDialog(
                expedition =
                    expedition,

                onConfirm = {

                    expeditionToDelete =
                        null

                    viewModel
                        .deleteExpedition(
                            expedition
                                .idEkspedicije
                        )
                },

                onDismiss = {

                    expeditionToDelete =
                        null
                }
            )
        }

    when {

        state.isLoading -> {

            MyExpeditionsLoading()
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

                    MyExpeditionsHeader(
                        count =
                            state.expeditions.size,

                        onCreateClick =
                            onCreateClick
                    )
                }

                state.errorMessage
                    ?.let { error ->

                        item {

                            ErrorMessage(
                                message =
                                    error
                            )
                        }
                    }

                if (
                    state.expeditions.isEmpty()
                ) {

                    item {

                        EmptyMyExpeditions(
                            onCreateClick =
                                onCreateClick
                        )
                    }

                } else {

                    item {

                        Text(
                            text =
                                "Objavljene ekspedicije",

                            style =
                                MaterialTheme
                                    .typography
                                    .titleLarge
                        )
                    }

                    items(
                        items =
                            state.expeditions,

                        key = {
                            it.idEkspedicije
                        }
                    ) { expedition ->

                        MyExpeditionCard(
                            expedition =
                                expedition,

                            isDeleting =
                                state.deletingId ==
                                        expedition
                                            .idEkspedicije,

                            onOpen = {

                                onOpenClick(
                                    expedition
                                        .idEkspedicije
                                )
                            },

                            onEdit = {

                                onEditClick(
                                    expedition
                                        .idEkspedicije
                                )
                            },

                            onDelete = {

                                expeditionToDelete =
                                    expedition
                            },

                            onEquipment = {

                                onEquipmentClick(
                                    expedition
                                        .idEkspedicije
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MyExpeditionsHeader(
    count: Int,
    onCreateClick: () -> Unit
) {

    Column {

        Text(
            text =
                "Moje ekspedicije",

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
                "Kreiraj i upravljaj svojim avanturama.",

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
                    16.dp
                )
        )

        Button(
            onClick =
                onCreateClick,

            modifier =
                Modifier.fillMaxWidth()
        ) {

            Icon(
                imageVector =
                    Icons.Filled.Add,

                contentDescription =
                    null
            )

            Spacer(
                modifier =
                    Modifier.size(
                        7.dp
                    )
            )

            Text(
                "Nova ekspedicija"
            )
        }

        if (
            count > 0
        ) {

            Spacer(
                modifier =
                    Modifier.height(
                        12.dp
                    )
            )

            Text(
                text =
                    "$count ${
                        expeditionCountLabel(
                            count
                        )
                    }",

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
private fun MyExpeditionCard(
    expedition: EkspedicijaResponse,
    isDeleting: Boolean,
    onOpen: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onEquipment: () -> Unit
) {

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
                    18.dp
                )
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
                    TextOverflow.Ellipsis
            )

            Spacer(
                modifier =
                    Modifier.height(
                        7.dp
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

                OwnerMetric(
                    label =
                        "Dužina",

                    value =
                        "${expedition.duzinaKm} km",

                    modifier =
                        Modifier.weight(
                            1f
                        )
                )

                OwnerMetric(
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
                        14.dp
                    )
            )

            /*
             * PRIMARY ACTION
             */
            FilledTonalButton(
                onClick =
                    onOpen,

                modifier =
                    Modifier.fillMaxWidth()
            ) {

                Icon(
                    imageVector =
                        Icons.Filled.Hiking,

                    contentDescription =
                        null,

                    modifier =
                        Modifier.size(
                            18.dp
                        )
                )

                Spacer(
                    modifier =
                        Modifier.size(
                            7.dp
                        )
                )

                Text(
                    "Otvori detalje"
                )
            }

            Spacer(
                modifier =
                    Modifier.height(
                        8.dp
                    )
            )

            /*
            * OWNER ACTIONS
             */
            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.spacedBy(
                        6.dp
                    )
            ) {

                OutlinedButton(
                    onClick =
                        onEdit,

                    modifier =
                        Modifier.weight(
                            1f
                        ),

                    contentPadding =
                        PaddingValues(
                            horizontal = 6.dp,
                            vertical = 0.dp
                        )
                ) {

                    Icon(
                        imageVector =
                            Icons.Filled.Edit,

                        contentDescription =
                            null,

                        modifier =
                            Modifier.size(
                                15.dp
                            )
                    )

                    Spacer(
                        modifier =
                            Modifier.size(
                                3.dp
                            )
                    )

                    Text(
                        text =
                            "Izmeni",

                        style =
                            MaterialTheme
                                .typography
                                .labelMedium,

                        maxLines =
                            1
                    )
                }

                OutlinedButton(
                    onClick =
                        onEquipment,

                    modifier =
                        Modifier.weight(
                            1f
                        ),

                    contentPadding =
                        PaddingValues(
                            horizontal = 6.dp,
                            vertical = 0.dp
                        )
                ) {

                    Icon(
                        imageVector =
                            Icons.Filled.Inventory2,

                        contentDescription =
                            null,

                        modifier =
                            Modifier.size(
                                15.dp
                            )
                    )

                    Spacer(
                        modifier =
                            Modifier.size(
                                3.dp
                            )
                    )

                    Text(
                        text =
                            "Oprema",

                        style =
                            MaterialTheme
                                .typography
                                .labelMedium,

                        maxLines =
                            1
                    )
                }

                Button(
                    onClick =
                        onDelete,

                    enabled =
                        !isDeleting,

                    modifier =
                        Modifier.weight(
                            1f
                        ),

                    contentPadding =
                        PaddingValues(
                            horizontal = 6.dp,
                            vertical = 0.dp
                        ),

                    colors =
                        ButtonDefaults
                            .buttonColors(
                                containerColor =
                                    MaterialTheme
                                        .colorScheme
                                        .error,

                                contentColor =
                                    MaterialTheme
                                        .colorScheme
                                        .onError
                            )
                ) {

                    if (isDeleting) {

                        CircularProgressIndicator(
                            modifier =
                                Modifier.size(
                                    17.dp
                                ),

                            strokeWidth =
                                2.dp,

                            color =
                                MaterialTheme
                                    .colorScheme
                                    .onError
                        )

                    } else {

                        Icon(
                            imageVector =
                                Icons.Filled.Delete,

                            contentDescription =
                                "Obriši",

                            modifier =
                                Modifier.size(
                                    17.dp
                                )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun OwnerMetric(
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
private fun DeleteExpeditionDialog(
    expedition: EkspedicijaResponse,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {

    AlertDialog(
        onDismissRequest =
            onDismiss,

        icon = {

            Icon(
                imageVector =
                    Icons.Filled.Delete,

                contentDescription =
                    null,

                tint =
                    MaterialTheme
                        .colorScheme
                        .error
            )
        },

        title = {

            Text(
                "Obriši ekspediciju"
            )
        },

        text = {

            Text(
                text =
                    "Da li sigurno želiš da obrišeš " +
                            "\"${expedition.naziv}\"? " +
                            "Ova akcija se ne može poništiti."
            )
        },

        confirmButton = {

            Button(
                onClick =
                    onConfirm,

                colors =
                    ButtonDefaults
                        .buttonColors(
                            containerColor =
                                MaterialTheme
                                    .colorScheme
                                    .error,

                            contentColor =
                                MaterialTheme
                                    .colorScheme
                                    .onError
                        )
            ) {

                Text(
                    "Obriši"
                )
            }
        },

        dismissButton = {

            TextButton(
                onClick =
                    onDismiss
            ) {

                Text(
                    "Otkaži"
                )
            }
        }
    )
}

@Composable
private fun EmptyMyExpeditions(
    onCreateClick: () -> Unit
) {

    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(
                    vertical =
                        48.dp
                ),

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
                    Icons.Filled.Hiking,

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
                "Još nemaš ekspedicije",

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
                "Kreiraj svoju prvu ekspediciju i podeli je sa HIMAL zajednicom.",

            style =
                MaterialTheme
                    .typography
                    .bodyMedium,

            color =
                MaterialTheme
                    .colorScheme
                    .onSurfaceVariant
        )

        Spacer(
            modifier =
                Modifier.height(
                    16.dp
                )
        )

        Button(
            onClick =
                onCreateClick
        ) {

            Icon(
                imageVector =
                    Icons.Filled.Add,

                contentDescription =
                    null
            )

            Spacer(
                modifier =
                    Modifier.size(
                        6.dp
                    )
            )

            Text(
                "Kreiraj ekspediciju"
            )
        }
    }
}

@Composable
private fun ErrorMessage(
    message: String
) {

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
            text =
                message,

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

@Composable
private fun MyExpeditionsLoading() {

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

private fun expeditionCountLabel(
    count: Int
): String {

    return when {

        count % 10 == 1 &&
                count % 100 != 11 ->
            "ekspedicija"

        count % 10 in 2..4 &&
                count % 100 !in 12..14 ->
            "ekspedicije"

        else ->
            "ekspedicija"
    }
}