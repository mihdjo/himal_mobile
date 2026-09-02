package com.himal.mobile.ui.saved

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.himal.mobile.data.remote.dto.EkspedicijaResponse

@Composable
fun SavedScreen(
    viewModel: SavedViewModel,
    onExpeditionClick: (Long) -> Unit,
    onSessionExpired: () -> Unit
) {

    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadSaved()
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
                    onClick = viewModel::loadSaved
                ) {
                    Text("Pokušaj ponovo")
                }
            }
        }

        state.expeditions.isEmpty() -> {

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
                    text = "Sačuvane ekspedicije",
                    style =
                        MaterialTheme.typography.headlineMedium
                )

                Spacer(
                    modifier =
                        Modifier.height(16.dp)
                )

                Text(
                    text =
                        "Još nemaš sačuvanih ekspedicija."
                )
            }
        }

        else -> {

            Column(
                modifier = Modifier.fillMaxSize()
            ) {

                Text(
                    text = "Sačuvane ekspedicije",
                    style =
                        MaterialTheme.typography.headlineMedium,
                    modifier =
                        Modifier.padding(16.dp)
                )

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding =
                        PaddingValues(
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 16.dp
                        ),
                    verticalArrangement =
                        Arrangement.spacedBy(12.dp)
                ) {

                    items(
                        items = state.expeditions,
                        key = {
                            it.idEkspedicije
                        }
                    ) { expedition ->

                        SavedExpeditionCard(
                            expedition = expedition,
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
}

@Composable
private fun SavedExpeditionCard(
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
                text = expedition.naziv,
                style =
                    MaterialTheme.typography.titleLarge
            )

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            Text(
                text = expedition.lokacija
            )

            Text(
                text =
                    "Težina: ${expedition.tezina}"
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
                    MaterialTheme.typography.bodySmall
            )
        }
    }
}