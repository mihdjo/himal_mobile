package com.himal.mobile.ui.packing

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
import androidx.compose.material.icons.filled.ViewAgenda
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.unit.dp
import com.himal.mobile.data.local.checklistKey
import com.himal.mobile.data.remote.dto.AgregiranaOpremaResponse
import com.himal.mobile.data.remote.dto.EkspedicijaOpremaResponse
import com.himal.mobile.data.remote.dto.GrupisanaOpremaResponse

@Composable
fun PackingListScreen(
    viewModel: PackingListViewModel,
    onSessionExpired: () -> Unit
) {

    val state by
    viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadPackingList()
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

            PackingLoading()
        }

        state.errorMessage != null -> {

            PackingError(
                message =
                    state.errorMessage
                        ?: "Nepoznata greška.",

                onRetry =
                    viewModel::loadPackingList
            )
        }

        else -> {

            Column(
                modifier =
                    Modifier.fillMaxSize()
            ) {

                Column(
                    modifier =
                        Modifier.padding(
                            start = 16.dp,
                            end = 16.dp,
                            top = 20.dp
                        )
                ) {

                    PackingHeader()

                    Spacer(
                        modifier =
                            Modifier.height(
                                16.dp
                            )
                    )

                    PackingSummary(
                        groups =
                            state.grouped,

                        preparedItems =
                            state.preparedItems
                    )

                    Spacer(
                        modifier =
                            Modifier.height(
                                14.dp
                            )
                    )

                    PackingModeSelector(
                        selectedMode =
                            state.viewMode,

                        onAllClick =
                            viewModel::showAll,

                        onGroupedClick =
                            viewModel::showGrouped
                    )

                    state.checklistErrorMessage
                        ?.let { error ->

                            Spacer(
                                modifier =
                                    Modifier.height(
                                        10.dp
                                    )
                            )

                            PackingErrorMessage(
                                message =
                                    error
                            )
                        }
                }

                Spacer(
                    modifier =
                        Modifier.height(
                            6.dp
                        )
                )

                when (
                    state.viewMode
                ) {

                    PackingViewMode.ALL -> {

                        AggregatedPackingList(
                            items =
                                state.aggregated,

                            groups =
                                state.grouped,

                            preparedItems =
                                state.preparedItems,

                            onCheckedChange =
                                viewModel::
                                toggleAggregatedEquipment,

                            modifier =
                                Modifier.weight(
                                    1f
                                )
                        )
                    }

                    PackingViewMode.GROUPED -> {

                        GroupedPackingList(
                            groups =
                                state.grouped,

                            preparedItems =
                                state.preparedItems,

                            onCheckedChange =
                                viewModel::
                                toggleEquipment,

                            modifier =
                                Modifier.weight(
                                    1f
                                )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PackingHeader() {

    Column {

        Text(
            text =
                "Packing lista",

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
                "Pripremi opremu za sve ekspedicije u planu.",

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
private fun PackingSummary(
    groups: List<GrupisanaOpremaResponse>,
    preparedItems: Set<String>
) {

    val requiredTotal =
        groups.sumOf { group ->

            group.oprema.count {
                it.obavezna
            }
        }

    val preparedRequired =
        groups.sumOf { group ->

            group.oprema.count { equipment ->

                equipment.obavezna &&
                        checklistKey(
                            group.idEkspedicije,
                            equipment.idOpreme
                        ) in preparedItems
            }
        }

    val readyExpeditions =
        groups.count {
            it.status
        }

    val progress =
        when {

            requiredTotal > 0 ->

                preparedRequired.toFloat() /
                        requiredTotal.toFloat()

            groups.isNotEmpty() ->

                1f

            else ->

                0f
        }

    Surface(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(
                18.dp
            ),

        color =
            if (
                groups.isNotEmpty() &&
                readyExpeditions ==
                groups.size
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
                    Alignment.CenterVertically
            ) {

                Column {

                    Text(
                        text =
                            "Spremnost opreme",

                        style =
                            MaterialTheme
                                .typography
                                .titleMedium
                    )

                    Spacer(
                        modifier =
                            Modifier.height(
                                2.dp
                            )
                    )

                    Text(
                        text =
                            if (
                                requiredTotal > 0
                            ) {

                                "$preparedRequired od $requiredTotal obaveznih stavki"

                            } else {

                                "Nema obavezne opreme"
                            },

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

                    Text(
                        text =
                            "$readyExpeditions/${groups.size} spremno",

                        style =
                            MaterialTheme
                                .typography
                                .labelMedium,

                        color =
                            MaterialTheme
                                .colorScheme
                                .primary,

                        modifier =
                            Modifier.padding(
                                horizontal =
                                    10.dp,

                                vertical =
                                    6.dp
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

            LinearProgressIndicator(
                progress = {
                    progress
                },

                modifier =
                    Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun PackingModeSelector(
    selectedMode: PackingViewMode,
    onAllClick: () -> Unit,
    onGroupedClick: () -> Unit
) {

    Row(
        modifier =
            Modifier.fillMaxWidth(),

        horizontalArrangement =
            Arrangement.spacedBy(
                8.dp
            )
    ) {

        if (
            selectedMode ==
            PackingViewMode.ALL
        ) {

            FilledTonalButton(
                onClick =
                    onAllClick,

                modifier =
                    Modifier.weight(
                        1f
                    )
            ) {

                Icon(
                    imageVector =
                        Icons.Filled.List,

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
                            6.dp
                        )
                )

                Text(
                    "Sva oprema"
                )
            }

        } else {

            OutlinedButton(
                onClick =
                    onAllClick,

                modifier =
                    Modifier.weight(
                        1f
                    )
            ) {

                Text(
                    "Sva oprema"
                )
            }
        }

        if (
            selectedMode ==
            PackingViewMode.GROUPED
        ) {

            FilledTonalButton(
                onClick =
                    onGroupedClick,

                modifier =
                    Modifier.weight(
                        1f
                    )
            ) {

                Icon(
                    imageVector =
                        Icons.Filled.ViewAgenda,

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
                            6.dp
                        )
                )

                Text(
                    "Po turama"
                )
            }

        } else {

            OutlinedButton(
                onClick =
                    onGroupedClick,

                modifier =
                    Modifier.weight(
                        1f
                    )
            ) {

                Text(
                    "Po turama"
                )
            }
        }
    }
}

@Composable
private fun AggregatedPackingList(
    items: List<AgregiranaOpremaResponse>,
    groups: List<GrupisanaOpremaResponse>,
    preparedItems: Set<String>,
    onCheckedChange: (
        Long,
        Boolean
    ) -> Unit,
    modifier: Modifier = Modifier
) {

    if (
        items.isEmpty()
    ) {

        PackingEmptyState(
            modifier =
                modifier,

            title =
                "Packing lista je prazna",

            message =
                "Dodaj ekspedicije u Moj plan kako bi se ovde pojavila potrebna oprema."
        )

        return
    }

    LazyColumn(
        modifier =
            modifier,

        contentPadding =
            PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 10.dp,
                bottom = 24.dp
            ),

        verticalArrangement =
            Arrangement.spacedBy(
                10.dp
            )
    ) {

        item {

            Text(
                text =
                    "Ukupna oprema",

                style =
                    MaterialTheme
                        .typography
                        .titleLarge
            )
        }

        items(
            items =
                items,

            key = {
                it.idOpreme
            }
        ) { equipment ->

            val relatedGroups =
                groups.filter { group ->

                    group.oprema.any {
                        it.idOpreme ==
                                equipment.idOpreme
                    }
                }

            val checked =
                relatedGroups.isNotEmpty() &&
                        relatedGroups.all { group ->

                            checklistKey(
                                group.idEkspedicije,
                                equipment.idOpreme
                            ) in preparedItems
                        }

            AggregatedEquipmentCard(
                equipment =
                    equipment,

                checked =
                    checked,

                onCheckedChange = {
                        value ->

                    onCheckedChange(
                        equipment.idOpreme,
                        value
                    )
                }
            )
        }
    }
}

@Composable
private fun AggregatedEquipmentCard(
    equipment:
    AgregiranaOpremaResponse,

    checked: Boolean,

    onCheckedChange:
        (Boolean) -> Unit
) {

    ElevatedCard(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(
                16.dp
            ),

        colors =
            CardDefaults
                .elevatedCardColors(
                    containerColor =
                        if (checked) {

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

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        14.dp
                    ),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Checkbox(
                checked =
                    checked,

                onCheckedChange =
                    onCheckedChange
            )

            Spacer(
                modifier =
                    Modifier.size(
                        6.dp
                    )
            )

            Column(
                modifier =
                    Modifier.weight(
                        1f
                    )
            ) {

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
                            equipment.naziv,

                        style =
                            MaterialTheme
                                .typography
                                .titleMedium,

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

                    RequirementBadge(
                        required =
                            equipment.obavezna
                    )
                }

                Spacer(
                    modifier =
                        Modifier.height(
                            7.dp
                        )
                )

                Text(
                    text =
                        "Ukupna količina: ${equipment.ukupnaKolicina}",

                    style =
                        MaterialTheme
                            .typography
                            .labelLarge
                )

                equipment.opis
                    ?.takeIf {
                        it.isNotBlank()
                    }
                    ?.let { description ->

                        Spacer(
                            modifier =
                                Modifier.height(
                                    5.dp
                                )
                        )

                        Text(
                            text =
                                description,

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
    }
}

@Composable
private fun GroupedPackingList(
    groups:
    List<GrupisanaOpremaResponse>,

    preparedItems:
    Set<String>,

    onCheckedChange:
        (
        Long,
        Long,
        Boolean
    ) -> Unit,

    modifier:
    Modifier = Modifier
) {

    if (
        groups.isEmpty()
    ) {

        PackingEmptyState(
            modifier =
                modifier,

            title =
                "Nema ekspedicija u planu",

            message =
                "Dodaj ekspediciju u Moj plan da bi priprema opreme mogla da počne."
        )

        return
    }

    LazyColumn(
        modifier =
            modifier,

        contentPadding =
            PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 10.dp,
                bottom = 24.dp
            ),

        verticalArrangement =
            Arrangement.spacedBy(
                14.dp
            )
    ) {

        item {

            Text(
                text =
                    "Oprema po ekspedicijama",

                style =
                    MaterialTheme
                        .typography
                        .titleLarge
            )
        }

        items(
            items =
                groups,

            key = {
                it.idEkspedicije
            }
        ) { group ->

            ExpeditionEquipmentGroup(
                group =
                    group,

                preparedItems =
                    preparedItems,

                onCheckedChange =
                    onCheckedChange
            )
        }
    }
}

@Composable
private fun ExpeditionEquipmentGroup(
    group:
    GrupisanaOpremaResponse,

    preparedItems:
    Set<String>,

    onCheckedChange:
        (
        Long,
        Long,
        Boolean
    ) -> Unit
) {

    val requiredEquipment =
        group.oprema.filter {
            it.obavezna
        }

    val preparedRequired =
        requiredEquipment.count {
                equipment ->

            checklistKey(
                group.idEkspedicije,
                equipment.idOpreme
            ) in preparedItems
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
                        if (
                            group.status
                        ) {

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
                    16.dp
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

                Column(
                    modifier =
                        Modifier.weight(
                            1f
                        )
                ) {

                    Text(
                        text =
                            group.nazivEkspedicije,

                        style =
                            MaterialTheme
                                .typography
                                .titleLarge
                    )

                    Spacer(
                        modifier =
                            Modifier.height(
                                3.dp
                            )
                    )

                    Text(
                        text =
                            if (
                                requiredEquipment.isEmpty()
                            ) {

                                "Nema obavezne opreme"

                            } else {

                                "$preparedRequired od ${requiredEquipment.size} obaveznih stavki"
                            },

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

                Spacer(
                    modifier =
                        Modifier.size(
                            8.dp
                        )
                )

                GroupStatusBadge(
                    ready =
                        group.status
                )
            }

            if (
                group.oprema.isEmpty()
            ) {

                Spacer(
                    modifier =
                        Modifier.height(
                            12.dp
                        )
                )

                Text(
                    text =
                        "Oprema nije definisana.",

                    style =
                        MaterialTheme
                            .typography
                            .bodyMedium,

                    color =
                        MaterialTheme
                            .colorScheme
                            .onSurfaceVariant
                )

            } else {

                Spacer(
                    modifier =
                        Modifier.height(
                            14.dp
                        )
                )

                HorizontalDivider()

                group.oprema
                    .forEachIndexed {
                            index,
                            equipment ->

                        GroupedEquipmentRow(
                            equipment =
                                equipment,

                            checked =
                                checklistKey(
                                    group.idEkspedicije,
                                    equipment.idOpreme
                                ) in preparedItems,

                            onCheckedChange = {
                                    checked ->

                                onCheckedChange(
                                    group.idEkspedicije,
                                    equipment.idOpreme,
                                    checked
                                )
                            }
                        )

                        if (
                            index <
                            group.oprema.lastIndex
                        ) {

                            HorizontalDivider()
                        }
                    }
            }
        }
    }
}

@Composable
private fun GroupedEquipmentRow(
    equipment:
    EkspedicijaOpremaResponse,

    checked: Boolean,

    onCheckedChange:
        (Boolean) -> Unit
) {

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(
                    vertical =
                        10.dp
                ),

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Checkbox(
            checked =
                checked,

            onCheckedChange =
                onCheckedChange
        )

        Spacer(
            modifier =
                Modifier.size(
                    6.dp
                )
        )

        Column(
            modifier =
                Modifier.weight(
                    1f
                )
        ) {

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
                        equipment.naziv,

                    style =
                        MaterialTheme
                            .typography
                            .titleMedium,

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

                RequirementBadge(
                    required =
                        equipment.obavezna
                )
            }

            Spacer(
                modifier =
                    Modifier.height(
                        4.dp
                    )
            )

            Text(
                text =
                    "Količina: ${equipment.kolicina}",

                style =
                    MaterialTheme
                        .typography
                        .bodyMedium
            )

            equipment.napomena
                ?.takeIf {
                    it.isNotBlank()
                }
                ?.let { note ->

                    Spacer(
                        modifier =
                            Modifier.height(
                                4.dp
                            )
                    )

                    Text(
                        text =
                            "Napomena: $note",

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
}

@Composable
private fun RequirementBadge(
    required: Boolean
) {

    Surface(
        shape =
            RoundedCornerShape(
                50.dp
            ),

        color =
            if (required) {

                MaterialTheme
                    .colorScheme
                    .primaryContainer

            } else {

                MaterialTheme
                    .colorScheme
                    .secondaryContainer
            }
    ) {

        Text(
            text =
                if (required) {
                    "Obavezna"
                } else {
                    "Opciona"
                },

            style =
                MaterialTheme
                    .typography
                    .labelMedium,

            color =
                if (required) {

                    MaterialTheme
                        .colorScheme
                        .onPrimaryContainer

                } else {

                    MaterialTheme
                        .colorScheme
                        .onSecondaryContainer
                },

            modifier =
                Modifier.padding(
                    horizontal =
                        9.dp,

                    vertical =
                        4.dp
                )
        )
    }
}

@Composable
private fun GroupStatusBadge(
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

        Row(
            modifier =
                Modifier.padding(
                    horizontal =
                        10.dp,

                    vertical =
                        5.dp
                ),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            if (ready) {

                Icon(
                    imageVector =
                        Icons.Filled.CheckCircle,

                    contentDescription =
                        null,

                    tint =
                        MaterialTheme
                            .colorScheme
                            .onPrimary,

                    modifier =
                        Modifier.size(
                            14.dp
                        )
                )

                Spacer(
                    modifier =
                        Modifier.size(
                            4.dp
                        )
                )
            }

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
                    }
            )
        }
    }
}

@Composable
private fun PackingErrorMessage(
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
                    12.dp
                )
        )
    }
}

@Composable
private fun PackingEmptyState(
    modifier: Modifier = Modifier,
    title: String,
    message: String
) {

    Column(
        modifier =
            modifier
                .fillMaxWidth()
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
                    16.dp
                )
        )

        Text(
            text =
                title,

            style =
                MaterialTheme
                    .typography
                    .titleLarge
        )

        Spacer(
            modifier =
                Modifier.height(
                    5.dp
                )
        )

        Text(
            text =
                message,

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
private fun PackingLoading() {

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
private fun PackingError(
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