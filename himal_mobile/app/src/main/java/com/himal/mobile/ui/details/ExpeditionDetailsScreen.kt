package com.himal.mobile.ui.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

@Composable
fun ExpeditionDetailsScreen(
    expeditionId: Long,
    viewModel: ExpeditionDetailsViewModel,
    onBack: () -> Unit,
    onSessionExpired: () -> Unit
) {

    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(expeditionId) {
        viewModel.loadExpedition(expeditionId)
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
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        }

        state.errorMessage != null -> {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = state.errorMessage
                        ?: "Nepoznata greška.",
                    color = MaterialTheme.colorScheme.error
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Button(
                    onClick = onBack
                ) {
                    Text("Nazad")
                }
            }
        }

        state.expedition != null -> {

            val expedition = state.expedition!!

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {

                item {

                    Button(
                        onClick = onBack
                    ) {
                        Text("← Nazad")
                    }
                }

                item {

                    Text(
                        text = expedition.naziv,
                        style =
                            MaterialTheme.typography.headlineMedium
                    )
                }

                item {

                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Column(
                            modifier =
                                Modifier.padding(16.dp)
                        ) {

                            Text(
                                text = expedition.opis
                            )

                            Spacer(
                                modifier = Modifier.height(16.dp)
                            )

                            Text(
                                "Lokacija: ${expedition.lokacija}"
                            )

                            Text(
                                "Težina: ${expedition.tezina}"
                            )

                            Text(
                                "Tip: ${expedition.tipEkspedicije}"
                            )

                            Text(
                                "Datum polaska: ${expedition.datumPolaska}"
                            )

                            Text(
                                "Trajanje: ${expedition.trajanjeMin} min"
                            )

                            Text(
                                "Dužina: ${expedition.duzinaKm} km"
                            )

                            Spacer(
                                modifier = Modifier.height(16.dp)
                            )

                            Text(
                                text =
                                    "Autor: ${expedition.autorUsername}",
                                style =
                                    MaterialTheme.typography.bodySmall
                            )

                            expedition.externalUrl?.let {

                                Spacer(
                                    modifier = Modifier.height(12.dp)
                                )

                                Text(
                                    text = "Ruta: $it"
                                )
                            }
                        }
                    }
                }

                item {

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text = "Potrebna oprema",
                        style =
                            MaterialTheme.typography.titleLarge
                    )
                }

                if (state.equipmentErrorMessage != null) {

                    item {

                        Text(
                            text =
                                state.equipmentErrorMessage
                                    ?: "Greška pri učitavanju opreme.",
                            color =
                                MaterialTheme.colorScheme.error
                        )
                    }

                } else if (state.equipment.isEmpty()) {

                    item {

                        Text(
                            text =
                                "Za ovu ekspediciju nije definisana oprema.",
                            style =
                                MaterialTheme.typography.bodyMedium
                        )
                    }

                } else {

                    items(
                        items = state.equipment,
                        key = {
                            it.idOpreme
                        }
                    ) { equipment ->

                        EquipmentCard(
                            equipment = equipment
                        )
                    }
                }
            }
        }
    }
}
@Composable
private fun EquipmentCard(
    equipment:
    com.himal.mobile.data.remote.dto.EkspedicijaOpremaResponse
) {

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = equipment.naziv,
                style =
                    MaterialTheme.typography.titleMedium
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text =
                    "Količina: ${equipment.kolicina}"
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
                        modifier = Modifier.height(8.dp)
                    )

                    Text(it)
                }
            }

            equipment.napomena?.let {

                if (it.isNotBlank()) {

                    Spacer(
                        modifier = Modifier.height(8.dp)
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