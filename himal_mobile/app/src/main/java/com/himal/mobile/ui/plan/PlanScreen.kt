package com.himal.mobile.ui.plan

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
import com.himal.mobile.data.remote.dto.MojPlanResponse

@Composable
fun PlanScreen(
    viewModel: PlanViewModel,
    onExpeditionClick: (Long) -> Unit,
    onPackingListClick: () -> Unit,
    onSessionExpired: () -> Unit
) {

    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadPlan()
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
                    onClick = viewModel::loadPlan
                ) {
                    Text("Pokušaj ponovo")
                }
            }
        }

        state.items.isEmpty() -> {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Moj plan",
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Text(
                    text = "Još nemaš ekspedicija u planu."
                )
            }
        }

        else -> {

            Column(
                modifier = Modifier.fillMaxSize()
            ) {

                Text(
                    text = "Moj plan",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(
                        start = 16.dp,
                        top = 16.dp,
                        end = 16.dp
                    )
                )

                Button(
                    onClick = onPackingListClick,
                    modifier = Modifier.padding(
                        horizontal = 16.dp,
                        vertical = 8.dp
                    )
                ) {
                    Text("🎒 Packing lista")
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 16.dp
                    ),
                    verticalArrangement =
                        Arrangement.spacedBy(12.dp)
                ) {

                    items(
                        items = state.items,
                        key = {
                            it.ekspedicija.idEkspedicije
                        }
                    ) { item ->

                        PlanExpeditionCard(
                            item = item,
                            onClick = {
                                onExpeditionClick(
                                    item.ekspedicija.idEkspedicije
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
private fun PlanExpeditionCard(
    item: MojPlanResponse,
    onClick: () -> Unit
) {

    val expedition = item.ekspedicija

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick
            )
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = expedition.naziv,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(
                modifier = Modifier.height(8.dp)
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
                modifier = Modifier.height(12.dp)
            )

            Text(
                text =
                    if (item.status)
                        "✓ Oprema spremna"
                    else
                        "○ Oprema nije spremna",
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}