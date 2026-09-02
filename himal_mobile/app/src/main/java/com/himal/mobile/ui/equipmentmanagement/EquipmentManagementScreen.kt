package com.himal.mobile.ui.equipmentmanagement

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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import com.himal.mobile.data.remote.dto.EkspedicijaOpremaResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EquipmentManagementScreen(
    expeditionId: Long,
    viewModel: EquipmentManagementViewModel,
    onBack: () -> Unit,
    onSessionExpired: () -> Unit
) {

    val state by
    viewModel.uiState.collectAsState()

    LaunchedEffect(expeditionId) {
        viewModel.load(expeditionId)
    }

    LaunchedEffect(
        state.sessionExpired
    ) {

        if (state.sessionExpired) {
            onSessionExpired()
        }
    }

    if (state.showAddDialog) {

        AddEquipmentDialog(
            state = state,
            viewModel = viewModel
        )
    }

    state.editingEquipment?.let {

        EditEquipmentDialog(
            state = state,
            viewModel = viewModel
        )
    }

    state.equipmentToDelete?.let {
            equipment ->

        AlertDialog(
            onDismissRequest =
                viewModel::cancelDelete,

            title = {
                Text("Ukloni opremu")
            },

            text = {
                Text(
                    "Da li želiš da ukloniš " +
                            "\"${equipment.naziv}\" " +
                            "sa ekspedicije?"
                )
            },

            confirmButton = {

                Button(
                    onClick =
                        viewModel::confirmDelete,
                    enabled =
                        !state.isSubmitting
                ) {
                    Text("Ukloni")
                }
            },

            dismissButton = {

                TextButton(
                    onClick =
                        viewModel::cancelDelete
                ) {
                    Text("Otkaži")
                }
            }
        )
    }

    if (
        state.showCreateEquipmentDialog
    ) {

        CreateCatalogEquipmentDialog(
            state = state,
            viewModel = viewModel
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

                Column(
                    modifier =
                        Modifier.padding(16.dp)
                ) {

                    Text(
                        text = "Oprema ekspedicije",
                        style =
                            MaterialTheme
                                .typography
                                .headlineMedium
                    )

                    Spacer(
                        Modifier.height(12.dp)
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
                                viewModel::openAddDialog,
                            modifier =
                                Modifier.weight(1f)
                        ) {
                            Text("+ Dodaj")
                        }

                        OutlinedButton(
                            onClick =
                                viewModel::openCreateEquipment,
                            modifier =
                                Modifier.weight(1f)
                        ) {
                            Text("+ Nova oprema")
                        }
                    }

                    Spacer(
                        Modifier.height(8.dp)
                    )

                    OutlinedButton(
                        onClick = onBack,
                        modifier =
                            Modifier.fillMaxWidth()
                    ) {
                        Text("Nazad")
                    }

                    state.errorMessage?.let {

                        Spacer(
                            Modifier.height(12.dp)
                        )

                        Text(
                            text = it,
                            color =
                                MaterialTheme
                                    .colorScheme
                                    .error
                        )
                    }
                }

                if (
                    state.expeditionEquipment
                        .isEmpty()
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
                            "Ekspedicija još nema definisanu opremu."
                        )
                    }

                } else {

                    LazyColumn(
                        modifier =
                            Modifier.fillMaxSize(),
                        contentPadding =
                            PaddingValues(16.dp),
                        verticalArrangement =
                            Arrangement.spacedBy(
                                12.dp
                            )
                    ) {

                        items(
                            items =
                                state.expeditionEquipment,
                            key = {
                                it.idOpreme
                            }
                        ) { equipment ->

                            EquipmentManagementCard(
                                equipment =
                                    equipment,
                                onEdit = {
                                    viewModel
                                        .openEdit(
                                            equipment
                                        )
                                },
                                onDelete = {
                                    viewModel
                                        .requestDelete(
                                            equipment
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
private fun EquipmentManagementCard(
    equipment:
    EkspedicijaOpremaResponse,
    onEdit: () -> Unit,
    onDelete: () -> Unit
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
                text = equipment.naziv,
                style =
                    MaterialTheme
                        .typography
                        .titleLarge
            )

            equipment.opis?.let {

                if (it.isNotBlank()) {

                    Text(it)

                    Spacer(
                        Modifier.height(6.dp)
                    )
                }
            }

            Text(
                "Količina: ${equipment.kolicina}"
            )

            Text(
                if (equipment.obavezna)
                    "Obavezna: DA"
                else
                    "Obavezna: NE"
            )

            equipment.napomena?.let {

                if (it.isNotBlank()) {
                    Text(
                        "Napomena: $it"
                    )
                }
            }

            Spacer(
                Modifier.height(12.dp)
            )

            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.spacedBy(
                        8.dp
                    )
            ) {

                OutlinedButton(
                    onClick = onEdit,
                    modifier =
                        Modifier.weight(1f)
                ) {
                    Text("Izmeni")
                }

                Button(
                    onClick = onDelete,
                    modifier =
                        Modifier.weight(1f)
                ) {
                    Text("Ukloni")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddEquipmentDialog(
    state: EquipmentManagementUiState,
    viewModel: EquipmentManagementViewModel
) {

    var expanded by
    remember {
        mutableStateOf(false)
    }

    val alreadyAssignedIds =
        state.expeditionEquipment
            .map {
                it.idOpreme
            }
            .toSet()

    val availableEquipment =
        state.catalog.filter {
            it.idOpreme !in
                    alreadyAssignedIds
        }

    val selected =
        state.catalog
            .firstOrNull {
                it.idOpreme ==
                        state.selectedEquipmentId
            }

    AlertDialog(
        onDismissRequest =
            viewModel::closeAddDialog,

        title = {
            Text("Dodaj opremu")
        },

        text = {

            Column {

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = {
                        expanded = !expanded
                    }
                ) {

                    OutlinedTextField(
                        value =
                            selected?.naziv
                                .orEmpty(),
                        onValueChange = {},
                        readOnly = true,
                        label = {
                            Text("Oprema")
                        },
                        placeholder = {
                            Text(
                                "Izaberi opremu"
                            )
                        },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults
                                .TrailingIcon(
                                    expanded
                                )
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = {
                            expanded = false
                        }
                    ) {

                        availableEquipment
                            .forEach {
                                    equipment ->

                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            equipment.naziv
                                        )
                                    },
                                    onClick = {

                                        viewModel
                                            .selectEquipment(
                                                equipment
                                                    .idOpreme
                                            )

                                        expanded =
                                            false
                                    }
                                )
                            }
                    }
                }

                Spacer(
                    Modifier.height(12.dp)
                )

                OutlinedTextField(
                    value =
                        state.addQuantity,
                    onValueChange =
                        viewModel::
                        onAddQuantityChange,
                    label = {
                        Text("Količina")
                    },
                    modifier =
                        Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(
                    Modifier.height(8.dp)
                )

                Row(
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Checkbox(
                        checked =
                            state.addRequired,
                        onCheckedChange =
                            viewModel::
                            onAddRequiredChange
                    )

                    Text("Obavezna oprema")
                }

                Spacer(
                    Modifier.height(8.dp)
                )

                OutlinedTextField(
                    value =
                        state.addNote,
                    onValueChange =
                        viewModel::
                        onAddNoteChange,
                    label = {
                        Text(
                            "Napomena (opciono)"
                        )
                    },
                    modifier =
                        Modifier.fillMaxWidth()
                )
            }
        },

        confirmButton = {

            Button(
                onClick =
                    viewModel::addEquipment,
                enabled =
                    !state.isSubmitting
            ) {
                Text("Dodaj")
            }
        },

        dismissButton = {

            TextButton(
                onClick =
                    viewModel::closeAddDialog
            ) {
                Text("Otkaži")
            }
        }
    )
}

@Composable
private fun EditEquipmentDialog(
    state: EquipmentManagementUiState,
    viewModel: EquipmentManagementViewModel
) {

    val equipment =
        state.editingEquipment
            ?: return

    AlertDialog(
        onDismissRequest =
            viewModel::closeEdit,

        title = {
            Text(
                "Izmeni ${equipment.naziv}"
            )
        },

        text = {

            Column {

                OutlinedTextField(
                    value =
                        state.editQuantity,
                    onValueChange =
                        viewModel::
                        onEditQuantityChange,
                    label = {
                        Text("Količina")
                    },
                    modifier =
                        Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(
                    Modifier.height(8.dp)
                )

                Row(
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Checkbox(
                        checked =
                            state.editRequired,
                        onCheckedChange =
                            viewModel::
                            onEditRequiredChange
                    )

                    Text("Obavezna oprema")
                }

                Spacer(
                    Modifier.height(8.dp)
                )

                OutlinedTextField(
                    value =
                        state.editNote,
                    onValueChange =
                        viewModel::
                        onEditNoteChange,
                    label = {
                        Text("Napomena")
                    },
                    modifier =
                        Modifier.fillMaxWidth()
                )
            }
        },

        confirmButton = {

            Button(
                onClick =
                    viewModel::saveEdit,
                enabled =
                    !state.isSubmitting
            ) {
                Text("Sačuvaj")
            }
        },

        dismissButton = {

            TextButton(
                onClick =
                    viewModel::closeEdit
            ) {
                Text("Otkaži")
            }
        }
    )
}

@Composable
private fun CreateCatalogEquipmentDialog(
    state: EquipmentManagementUiState,
    viewModel: EquipmentManagementViewModel
) {

    AlertDialog(
        onDismissRequest =
            viewModel::closeCreateEquipment,

        title = {
            Text("Nova oprema")
        },

        text = {

            Column {

                OutlinedTextField(
                    value =
                        state.newEquipmentName,
                    onValueChange =
                        viewModel::
                        onNewEquipmentNameChange,
                    label = {
                        Text("Naziv")
                    },
                    modifier =
                        Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(
                    Modifier.height(12.dp)
                )

                OutlinedTextField(
                    value =
                        state.newEquipmentDescription,
                    onValueChange =
                        viewModel::
                        onNewEquipmentDescriptionChange,
                    label = {
                        Text("Opis")
                    },
                    modifier =
                        Modifier.fillMaxWidth()
                )
            }
        },

        confirmButton = {

            Button(
                onClick =
                    viewModel::
                    createCatalogEquipment,
                enabled =
                    !state.isSubmitting
            ) {
                Text("Kreiraj")
            }
        },

        dismissButton = {

            TextButton(
                onClick =
                    viewModel::
                    closeCreateEquipment
            ) {
                Text("Otkaži")
            }
        }
    )
}