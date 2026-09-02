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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
        mutableStateOf<EkspedicijaResponse?>(null)
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

    expeditionToDelete?.let { expedition ->

        AlertDialog(
            onDismissRequest = {
                expeditionToDelete = null
            },

            title = {
                Text("Obriši ekspediciju")
            },

            text = {
                Text(
                    "Da li sigurno želiš da obrišeš " +
                            "\"${expedition.naziv}\"?"
                )
            },

            confirmButton = {

                Button(
                    onClick = {

                        expeditionToDelete = null

                        viewModel.deleteExpedition(
                            expedition.idEkspedicije
                        )
                    }
                ) {
                    Text("Obriši")
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {
                        expeditionToDelete = null
                    }
                ) {
                    Text("Otkaži")
                }
            }
        )
    }

    when {

        state.isLoading -> {

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

        else -> {

            Column(
                modifier =
                    Modifier.fillMaxSize()
            ) {

                Text(
                    text = "Moje ekspedicije",
                    style =
                        MaterialTheme.typography.headlineMedium,
                    modifier =
                        Modifier.padding(16.dp)
                )

                Button(
                    onClick = onCreateClick,
                    modifier =
                        Modifier.padding(
                            horizontal = 16.dp
                        )
                ) {
                    Text("+ Nova ekspedicija")
                }

                state.errorMessage?.let {

                    Text(
                        text = it,
                        color =
                            MaterialTheme.colorScheme.error,
                        modifier =
                            Modifier.padding(16.dp)
                    )
                }

                if (
                    state.expeditions.isEmpty()
                ) {

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
                            "Još nisi kreirao nijednu ekspediciju."
                        )
                    }

                } else {

                    LazyColumn(
                        modifier =
                            Modifier.fillMaxSize(),
                        contentPadding =
                            PaddingValues(16.dp),
                        verticalArrangement =
                            Arrangement.spacedBy(12.dp)
                    ) {

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
                                            expedition.idEkspedicije,

                                onOpen = {

                                    onOpenClick(
                                        expedition.idEkspedicije
                                    )
                                },

                                onEdit = {

                                    onEditClick(
                                        expedition.idEkspedicije
                                    )
                                },

                                onDelete = {

                                    expeditionToDelete =
                                        expedition
                                },

                                onEquipment = {

                                    onEquipmentClick(
                                        expedition.idEkspedicije
                                    )
                                }
                            )
                        }
                    }
                }
            }
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

    Card(
        modifier =
            Modifier.fillMaxWidth()
    ) {

        Column(
            modifier =
                Modifier.padding(16.dp)
        ) {

            Text(
                text = expedition.naziv,
                style =
                    MaterialTheme.typography.titleLarge
            )

            Spacer(
                modifier =
                    Modifier.height(6.dp)
            )

            Text(
                text = expedition.lokacija
            )

            Text(
                text =
                    "${expedition.tezina} • " +
                            "${expedition.duzinaKm} km • " +
                            "${expedition.trajanjeMin} min"
            )

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.spacedBy(8.dp)
            ) {

                OutlinedButton(
                    onClick = onOpen,
                    modifier =
                        Modifier.weight(1f)
                ) {
                    Text("Otvori")
                }

                OutlinedButton(
                    onClick = onEdit,
                    modifier =
                        Modifier.weight(1f)
                ) {
                    Text("Izmeni")
                }

                Button(
                    onClick = onDelete,
                    enabled =
                        !isDeleting,
                    modifier =
                        Modifier.weight(1f)
                ) {

                    if (isDeleting) {

                        CircularProgressIndicator()

                    } else {

                        Text("Obriši")
                    }
                }
            }

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            OutlinedButton(
                onClick =
                    onEquipment,
                modifier =
                    Modifier.fillMaxWidth()
            ) {

                Text(
                    "Upravljaj opremom"
                )
            }
        }
    }
}