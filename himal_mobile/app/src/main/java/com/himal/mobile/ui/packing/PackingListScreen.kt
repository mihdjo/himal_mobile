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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.himal.mobile.data.remote.dto.AgregiranaOpremaResponse
import com.himal.mobile.data.remote.dto.EkspedicijaOpremaResponse
import com.himal.mobile.data.remote.dto.GrupisanaOpremaResponse

@Composable
fun PackingListScreen(
    viewModel: PackingListViewModel,
    onSessionExpired: () -> Unit
) {

    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadPackingList()
    }

    LaunchedEffect(state.sessionExpired) {

        if (state.sessionExpired) {
            onSessionExpired()
        }
    }

    when {

        state.isLoading -> {

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement =
                    Arrangement.Center,
                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        }

        state.errorMessage != null -> {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement =
                    Arrangement.Center,
                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {

                Text(
                    text =
                        state.errorMessage
                            ?: "Nepoznata greška.",
                    color =
                        MaterialTheme.colorScheme.error
                )

                Spacer(
                    modifier =
                        Modifier.height(16.dp)
                )

                Button(
                    onClick =
                        viewModel::loadPackingList
                ) {
                    Text("Pokušaj ponovo")
                }
            }
        }

        else -> {

            Column(
                modifier = Modifier.fillMaxSize()
            ) {

                Text(
                    text = "Packing lista",
                    style =
                        MaterialTheme.typography.headlineMedium,
                    modifier =
                        Modifier.padding(16.dp)
                )

                PackingModeSelector(
                    selectedMode = state.viewMode,
                    onAllClick = viewModel::showAll,
                    onGroupedClick =
                        viewModel::showGrouped
                )

                when (state.viewMode) {

                    PackingViewMode.ALL -> {

                        AggregatedPackingList(
                            items = state.aggregated
                        )
                    }

                    PackingViewMode.GROUPED -> {

                        GroupedPackingList(
                            groups = state.grouped
                        )
                    }
                }
            }
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp
            ),
        horizontalArrangement =
            Arrangement.spacedBy(8.dp)
    ) {

        if (selectedMode == PackingViewMode.ALL) {

            Button(
                onClick = onAllClick,
                modifier = Modifier.weight(1f)
            ) {
                Text("Sva oprema")
            }

        } else {

            OutlinedButton(
                onClick = onAllClick,
                modifier = Modifier.weight(1f)
            ) {
                Text("Sva oprema")
            }
        }

        if (
            selectedMode ==
            PackingViewMode.GROUPED
        ) {

            Button(
                onClick = onGroupedClick,
                modifier = Modifier.weight(1f)
            ) {
                Text("Po ekspedicijama")
            }

        } else {

            OutlinedButton(
                onClick = onGroupedClick,
                modifier = Modifier.weight(1f)
            ) {
                Text("Po ekspedicijama")
            }
        }
    }
}

@Composable
private fun AggregatedPackingList(
    items: List<AgregiranaOpremaResponse>
) {

    if (items.isEmpty()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement =
                Arrangement.Center,
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Text(
                text =
                    "Packing lista je prazna."
            )

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            Text(
                text =
                    "Dodaj ekspedicije u Moj plan."
            )
        }

        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding =
            PaddingValues(16.dp),
        verticalArrangement =
            Arrangement.spacedBy(12.dp)
    ) {

        items(
            items = items,
            key = {
                it.idOpreme
            }
        ) { equipment ->

            AggregatedEquipmentCard(
                equipment = equipment
            )
        }
    }
}

@Composable
private fun AggregatedEquipmentCard(
    equipment: AgregiranaOpremaResponse
) {

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier =
                Modifier.padding(16.dp)
        ) {

            Text(
                text = equipment.naziv,
                style =
                    MaterialTheme.typography.titleMedium
            )

            Spacer(
                modifier =
                    Modifier.height(4.dp)
            )

            Text(
                text =
                    "Ukupna količina: " +
                            "${equipment.ukupnaKolicina}"
            )

            Text(
                text =
                    if (equipment.obavezna)
                        "Obavezna oprema"
                    else
                        "Opciona oprema"
            )

            equipment.opis?.let {

                if (it.isNotBlank()) {

                    Spacer(
                        modifier =
                            Modifier.height(8.dp)
                    )

                    Text(
                        text = it,
                        style =
                            MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
private fun GroupedPackingList(
    groups: List<GrupisanaOpremaResponse>
) {

    if (groups.isEmpty()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement =
                Arrangement.Center,
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Text(
                "Nema ekspedicija u planu."
            )
        }

        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding =
            PaddingValues(16.dp),
        verticalArrangement =
            Arrangement.spacedBy(20.dp)
    ) {

        items(
            items = groups,
            key = {
                it.idEkspedicije
            }
        ) { group ->

            ExpeditionEquipmentGroup(
                group = group
            )
        }
    }
}

@Composable
private fun ExpeditionEquipmentGroup(
    group: GrupisanaOpremaResponse
) {

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Text(
            text = group.nazivEkspedicije,
            style =
                MaterialTheme.typography.titleLarge
        )

        Spacer(
            modifier =
                Modifier.height(4.dp)
        )

        Text(
            text =
                if (group.status)
                    "✓ Oprema spremna"
                else
                    "○ Oprema nije spremna",
            style =
                MaterialTheme.typography.bodyMedium
        )

        Spacer(
            modifier =
                Modifier.height(8.dp)
        )

        if (group.oprema.isEmpty()) {

            Text(
                text =
                    "Oprema nije definisana."
            )

        } else {

            group.oprema.forEach { equipment ->

                GroupedEquipmentCard(
                    equipment = equipment
                )

                Spacer(
                    modifier =
                        Modifier.height(8.dp)
                )
            }
        }
    }
}

@Composable
private fun GroupedEquipmentCard(
    equipment: EkspedicijaOpremaResponse
) {

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier =
                Modifier.padding(12.dp)
        ) {

            Text(
                text = equipment.naziv,
                style =
                    MaterialTheme.typography.titleMedium
            )

            Text(
                text =
                    "Količina: ${equipment.kolicina}"
            )

            Text(
                text =
                    if (equipment.obavezna)
                        "Obavezna"
                    else
                        "Opciona"
            )

            equipment.napomena?.let {

                if (it.isNotBlank()) {

                    Spacer(
                        modifier =
                            Modifier.height(4.dp)
                    )

                    Text(
                        text = "Napomena: $it",
                        style =
                            MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}