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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.himal.mobile.data.remote.dto.EkspedicijaOpremaResponse

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

    state.editingEquipment
        ?.let {

            EditEquipmentDialog(
                state = state,
                viewModel = viewModel
            )
        }

    state.equipmentToDelete
        ?.let { equipment ->

            DeleteEquipmentDialog(
                equipment = equipment,
                isSubmitting =
                    state.isSubmitting,
                onConfirm =
                    viewModel::confirmDelete,
                onDismiss =
                    viewModel::cancelDelete
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

            EquipmentLoading()
        }

        else -> {

            LazyColumn(
                modifier =
                    Modifier.fillMaxSize(),

                contentPadding =
                    PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 8.dp,
                        bottom = 28.dp
                    ),

                verticalArrangement =
                    Arrangement.spacedBy(
                        14.dp
                    )
            ) {

                item {

                    TextButton(
                        onClick =
                            onBack
                    ) {

                        Icon(
                            imageVector =
                                Icons.Filled.ArrowBack,
                            contentDescription =
                                "Nazad"
                        )

                        Spacer(
                            modifier =
                                Modifier.size(
                                    6.dp
                                )
                        )

                        Text(
                            "Nazad"
                        )
                    }
                }

                item {

                    EquipmentHeader(
                        count =
                            state.expeditionEquipment.size
                    )
                }

                item {

                    EquipmentActions(
                        onAdd =
                            viewModel::openAddDialog,

                        onCreate =
                            viewModel::openCreateEquipment
                    )
                }

                state.errorMessage
                    ?.let { message ->

                        item {

                            EquipmentErrorMessage(
                                message =
                                    message
                            )
                        }
                    }

                if (
                    state.expeditionEquipment
                        .isEmpty()
                ) {

                    item {

                        EmptyEquipmentState(
                            onAdd =
                                viewModel::openAddDialog
                        )
                    }

                } else {

                    item {

                        Text(
                            text =
                                "Definisana oprema",

                            style =
                                MaterialTheme
                                    .typography
                                    .titleLarge
                        )
                    }

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

@Composable
private fun EquipmentHeader(
    count: Int
) {

    Column {

        Text(
            text =
                "Oprema ekspedicije",

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
                "Definiši šta je potrebno poneti na ovu avanturu.",

            style =
                MaterialTheme
                    .typography
                    .bodyLarge,

            color =
                MaterialTheme
                    .colorScheme
                    .onSurfaceVariant
        )

        if (
            count > 0
        ) {

            Spacer(
                modifier =
                    Modifier.height(
                        12.dp
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
                        .primaryContainer
            ) {

                Row(
                    modifier =
                        Modifier.padding(
                            horizontal =
                                11.dp,
                            vertical =
                                6.dp
                        ),

                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector =
                            Icons.Filled.Inventory2,

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
                                5.dp
                            )
                    )

                    Text(
                        text =
                            "$count stavki",

                        style =
                            MaterialTheme
                                .typography
                                .labelLarge,

                        color =
                            MaterialTheme
                                .colorScheme
                                .onPrimaryContainer
                    )
                }
            }
        }
    }
}

@Composable
private fun EquipmentActions(
    onAdd: () -> Unit,
    onCreate: () -> Unit
) {

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
                onAdd,

            modifier =
                Modifier.weight(
                    1f
                )
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
                "Dodaj"
            )
        }

        FilledTonalButton(
            onClick =
                onCreate,

            modifier =
                Modifier.weight(
                    1f
                )
        ) {

            Icon(
                imageVector =
                    Icons.Filled.Inventory2,

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
                "Nova oprema"
            )
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
                            .titleLarge,

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

            equipment.opis
                ?.takeIf {
                    it.isNotBlank()
                }
                ?.let { description ->

                    Spacer(
                        modifier =
                            Modifier.height(
                                8.dp
                            )
                    )

                    Text(
                        text =
                            description,

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

            Spacer(
                modifier =
                    Modifier.height(
                        12.dp
                    )
            )

            Surface(
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
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                12.dp
                            )
                ) {

                    Text(
                        text =
                            "Količina",

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
                            equipment.kolicina
                                .toString(),

                        style =
                            MaterialTheme
                                .typography
                                .labelLarge
                    )
                }
            }

            equipment.napomena
                ?.takeIf {
                    it.isNotBlank()
                }
                ?.let { note ->

                    Spacer(
                        modifier =
                            Modifier.height(
                                10.dp
                            )
                    )

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
                                .secondaryContainer
                    ) {

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
                                    .onSecondaryContainer,

                            modifier =
                                Modifier.padding(
                                    12.dp
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

                OutlinedButton(
                    onClick =
                        onEdit,

                    modifier =
                        Modifier.weight(
                            1f
                        )
                ) {

                    Icon(
                        imageVector =
                            Icons.Filled.Edit,

                        contentDescription =
                            null,

                        modifier =
                            Modifier.size(
                                17.dp
                            )
                    )

                    Spacer(
                        modifier =
                            Modifier.size(
                                5.dp
                            )
                    )

                    Text(
                        "Izmeni"
                    )
                }

                Button(
                    onClick =
                        onDelete,

                    modifier =
                        Modifier.weight(
                            1f
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

                    Icon(
                        imageVector =
                            Icons.Filled.Delete,

                        contentDescription =
                            null,

                        modifier =
                            Modifier.size(
                                17.dp
                            )
                    )

                    Spacer(
                        modifier =
                            Modifier.size(
                                5.dp
                            )
                    )

                    Text(
                        "Ukloni"
                    )
                }
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
                        10.dp,
                    vertical =
                        5.dp
                )
        )
    }
}

@Composable
private fun EmptyEquipmentState(
    onAdd: () -> Unit
) {

    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(
                    vertical =
                        42.dp
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
                    Icons.Filled.Inventory2,

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
                "Oprema još nije definisana",

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
                "Dodaj potrebnu opremu kako bi učesnici znali šta treba da pripreme.",

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
                onAdd
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
                "Dodaj opremu"
            )
        }
    }
}

@OptIn(
    ExperimentalMaterial3Api::class
)
@Composable
private fun AddEquipmentDialog(
    state:
    EquipmentManagementUiState,

    viewModel:
    EquipmentManagementViewModel
) {

    var expanded by
    remember {
        mutableStateOf(
            false
        )
    }

    val alreadyAssignedIds =
        state.expeditionEquipment
            .map {
                it.idOpreme
            }
            .toSet()

    val availableEquipment =
        state.catalog
            .filter {

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

        icon = {

            Icon(
                imageVector =
                    Icons.Filled.Add,

                contentDescription =
                    null,

                tint =
                    MaterialTheme
                        .colorScheme
                        .primary
            )
        },

        title = {

            Text(
                "Dodaj opremu"
            )
        },

        text = {

            Column {

                Text(
                    text =
                        "Izaberi stavku iz kataloga i definiši potrebnu količinu.",

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
                            14.dp
                        )
                )

                ExposedDropdownMenuBox(
                    expanded =
                        expanded,

                    onExpandedChange = {

                        expanded =
                            !expanded
                    }
                ) {

                    OutlinedTextField(
                        value =
                            selected?.naziv
                                .orEmpty(),

                        onValueChange = {},

                        readOnly =
                            true,

                        label = {

                            Text(
                                "Oprema"
                            )
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

                        modifier =
                            Modifier
                                .menuAnchor()
                                .fillMaxWidth(),

                        shape =
                            RoundedCornerShape(
                                14.dp
                            )
                    )

                    ExposedDropdownMenu(
                        expanded =
                            expanded,

                        onDismissRequest = {

                            expanded =
                                false
                        }
                    ) {

                        availableEquipment
                            .forEach { equipment ->

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
                    modifier =
                        Modifier.height(
                            12.dp
                        )
                )

                OutlinedTextField(
                    value =
                        state.addQuantity,

                    onValueChange =
                        viewModel::
                        onAddQuantityChange,

                    label = {

                        Text(
                            "Količina"
                        )
                    },

                    keyboardOptions =
                        KeyboardOptions(
                            keyboardType =
                                KeyboardType.Number
                        ),

                    modifier =
                        Modifier.fillMaxWidth(),

                    singleLine =
                        true,

                    shape =
                        RoundedCornerShape(
                            14.dp
                        )
                )

                Spacer(
                    modifier =
                        Modifier.height(
                            6.dp
                        )
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

                    Column {

                        Text(
                            text =
                                "Obavezna oprema",

                            style =
                                MaterialTheme
                                    .typography
                                    .labelLarge
                        )

                        Text(
                            text =
                                "Utiče na status spremnosti ekspedicije.",

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

                Spacer(
                    modifier =
                        Modifier.height(
                            8.dp
                        )
                )

                OutlinedTextField(
                    value =
                        state.addNote,

                    onValueChange =
                        viewModel::
                        onAddNoteChange,

                    label = {

                        Text(
                            "Napomena"
                        )
                    },

                    placeholder = {

                        Text(
                            "Opciono"
                        )
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    shape =
                        RoundedCornerShape(
                            14.dp
                        )
                )

                state.errorMessage
                    ?.let { message ->

                        Spacer(
                            modifier =
                                Modifier.height(
                                    10.dp
                                )
                        )

                        InlineDialogError(
                            message =
                                message
                        )
                    }
            }
        },

        confirmButton = {

            Button(
                onClick =
                    viewModel::addEquipment,

                enabled =
                    !state.isSubmitting
            ) {

                if (
                    state.isSubmitting
                ) {

                    SmallProgress()

                } else {

                    Text(
                        "Dodaj"
                    )
                }
            }
        },

        dismissButton = {

            TextButton(
                onClick =
                    viewModel::closeAddDialog
            ) {

                Text(
                    "Otkaži"
                )
            }
        }
    )
}

@Composable
private fun EditEquipmentDialog(
    state:
    EquipmentManagementUiState,

    viewModel:
    EquipmentManagementViewModel
) {

    val equipment =
        state.editingEquipment
            ?: return

    AlertDialog(
        onDismissRequest =
            viewModel::closeEdit,

        icon = {

            Icon(
                imageVector =
                    Icons.Filled.Edit,

                contentDescription =
                    null,

                tint =
                    MaterialTheme
                        .colorScheme
                        .primary
            )
        },

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

                        Text(
                            "Količina"
                        )
                    },

                    keyboardOptions =
                        KeyboardOptions(
                            keyboardType =
                                KeyboardType.Number
                        ),

                    modifier =
                        Modifier.fillMaxWidth(),

                    singleLine =
                        true,

                    shape =
                        RoundedCornerShape(
                            14.dp
                        )
                )

                Spacer(
                    modifier =
                        Modifier.height(
                            6.dp
                        )
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

                    Column {

                        Text(
                            text =
                                "Obavezna oprema",

                            style =
                                MaterialTheme
                                    .typography
                                    .labelLarge
                        )

                        Text(
                            text =
                                "Utiče na status spremnosti ekspedicije.",

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

                Spacer(
                    modifier =
                        Modifier.height(
                            8.dp
                        )
                )

                OutlinedTextField(
                    value =
                        state.editNote,

                    onValueChange =
                        viewModel::
                        onEditNoteChange,

                    label = {

                        Text(
                            "Napomena"
                        )
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    shape =
                        RoundedCornerShape(
                            14.dp
                        )
                )

                state.errorMessage
                    ?.let { message ->

                        Spacer(
                            modifier =
                                Modifier.height(
                                    10.dp
                                )
                        )

                        InlineDialogError(
                            message =
                                message
                        )
                    }
            }
        },

        confirmButton = {

            Button(
                onClick =
                    viewModel::saveEdit,

                enabled =
                    !state.isSubmitting
            ) {

                if (
                    state.isSubmitting
                ) {

                    SmallProgress()

                } else {

                    Text(
                        "Sačuvaj"
                    )
                }
            }
        },

        dismissButton = {

            TextButton(
                onClick =
                    viewModel::closeEdit
            ) {

                Text(
                    "Otkaži"
                )
            }
        }
    )
}

@Composable
private fun CreateCatalogEquipmentDialog(
    state:
    EquipmentManagementUiState,

    viewModel:
    EquipmentManagementViewModel
) {

    AlertDialog(
        onDismissRequest =
            viewModel::closeCreateEquipment,

        icon = {

            Icon(
                imageVector =
                    Icons.Filled.Inventory2,

                contentDescription =
                    null,

                tint =
                    MaterialTheme
                        .colorScheme
                        .primary
            )
        },

        title = {

            Text(
                "Nova oprema"
            )
        },

        text = {

            Column {

                Text(
                    text =
                        "Dodaj novu vrstu opreme u HIMAL katalog.",

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
                            14.dp
                        )
                )

                OutlinedTextField(
                    value =
                        state.newEquipmentName,

                    onValueChange =
                        viewModel::
                        onNewEquipmentNameChange,

                    label = {

                        Text(
                            "Naziv"
                        )
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    singleLine =
                        true,

                    shape =
                        RoundedCornerShape(
                            14.dp
                        )
                )

                Spacer(
                    modifier =
                        Modifier.height(
                            12.dp
                        )
                )

                OutlinedTextField(
                    value =
                        state.newEquipmentDescription,

                    onValueChange =
                        viewModel::
                        onNewEquipmentDescriptionChange,

                    label = {

                        Text(
                            "Opis"
                        )
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    shape =
                        RoundedCornerShape(
                            14.dp
                        )
                )

                state.errorMessage
                    ?.let { message ->

                        Spacer(
                            modifier =
                                Modifier.height(
                                    10.dp
                                )
                        )

                        InlineDialogError(
                            message =
                                message
                        )
                    }
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

                if (
                    state.isSubmitting
                ) {

                    SmallProgress()

                } else {

                    Text(
                        "Kreiraj"
                    )
                }
            }
        },

        dismissButton = {

            TextButton(
                onClick =
                    viewModel::
                    closeCreateEquipment
            ) {

                Text(
                    "Otkaži"
                )
            }
        }
    )
}

@Composable
private fun DeleteEquipmentDialog(
    equipment:
    EkspedicijaOpremaResponse,

    isSubmitting: Boolean,

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
                "Ukloni opremu"
            )
        },

        text = {

            Text(
                text =
                    "Da li želiš da ukloniš " +
                            "\"${equipment.naziv}\" " +
                            "sa ove ekspedicije?"
            )
        },

        confirmButton = {

            Button(
                onClick =
                    onConfirm,

                enabled =
                    !isSubmitting,

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

                if (
                    isSubmitting
                ) {

                    SmallProgress()

                } else {

                    Text(
                        "Ukloni"
                    )
                }
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
private fun InlineDialogError(
    message: String
) {

    Surface(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(
                10.dp
            ),

        color =
            MaterialTheme
                .colorScheme
                .errorContainer
    ) {

        Text(
            text =
                message,

            style =
                MaterialTheme
                    .typography
                    .bodySmall,

            color =
                MaterialTheme
                    .colorScheme
                    .onErrorContainer,

            modifier =
                Modifier.padding(
                    10.dp
                )
        )
    }
}

@Composable
private fun EquipmentErrorMessage(
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
private fun SmallProgress() {

    CircularProgressIndicator(
        modifier =
            Modifier.size(
                18.dp
            ),

        strokeWidth =
            2.dp
    )
}

@Composable
private fun EquipmentLoading() {

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