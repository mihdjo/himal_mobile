package com.himal.mobile.ui.expeditionform

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
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
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpeditionFormScreen(
    viewModel: ExpeditionFormViewModel,
    expeditionId: Long?,
    onSaved: () -> Unit,
    onCancel: () -> Unit,
    onSessionExpired: () -> Unit
) {

    val state by
    viewModel.uiState.collectAsState()

    var typeMenuExpanded by
    remember {
        mutableStateOf(false)
    }

    var showDatePicker by
    remember {
        mutableStateOf(false)
    }

    LaunchedEffect(expeditionId) {

        if (expeditionId == null) {

            viewModel.prepareCreate()

        } else {

            viewModel.loadForEdit(
                expeditionId
            )
        }
    }

    LaunchedEffect(
        state.savedSuccessfully
    ) {

        if (state.savedSuccessfully) {
            onSaved()
        }
    }

    LaunchedEffect(
        state.sessionExpired
    ) {

        if (state.sessionExpired) {
            onSessionExpired()
        }
    }

    /*
     * DATE PICKER
     */
    if (showDatePicker) {

        ExpeditionDatePicker(
            currentDate =
                state.datumPolaska,

            onDateSelected = { date ->

                viewModel
                    .onDatumPolaskaChange(
                        date
                    )

                showDatePicker =
                    false
            },

            onDismiss = {

                showDatePicker =
                    false
            }
        )
    }

    if (state.isLoading) {

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

        return
    }

    val isCreate =
        expeditionId == null

    val selectedType =
        state.expeditionTypes
            .firstOrNull {

                it.idTipEkspedicije ==
                        state.selectedTypeId
            }

    LazyColumn(
        modifier =
            Modifier.fillMaxSize(),

        contentPadding =
            PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 8.dp,
                bottom = 32.dp
            ),

        verticalArrangement =
            Arrangement.spacedBy(
                16.dp
            )
    ) {

        /*
         * BACK
         */
        item {

            TextButton(
                onClick =
                    onCancel,

                enabled =
                    !state.isSaving
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

        /*
         * HEADER
         */
        item {

            FormHeader(
                isCreate =
                    isCreate
            )
        }

        /*
         * BASIC INFORMATION
         */
        item {

            FormSection(
                title =
                    "Osnovne informacije",

                subtitle =
                    "Predstavi ekspediciju HIMAL zajednici."
            ) {

                OutlinedTextField(
                    value =
                        state.naziv,

                    onValueChange =
                        viewModel::onNazivChange,

                    label = {
                        Text(
                            "Naziv ekspedicije"
                        )
                    },

                    supportingText = {

                        Text(
                            "${state.naziv.length}/120"
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
                        state.opis,

                    onValueChange =
                        viewModel::onOpisChange,

                    label = {
                        Text(
                            "Opis"
                        )
                    },

                    placeholder = {

                        Text(
                            "Opiši rutu, teren i ono što učesnici mogu da očekuju..."
                        )
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    minLines =
                        4,

                    shape =
                        RoundedCornerShape(
                            14.dp
                        )
                )
            }
        }

        /*
         * PLACE AND DATE
         */
        item {

            FormSection(
                title =
                    "Mesto i polazak",

                subtitle =
                    "Gde i kada počinje avantura?"
            ) {

                OutlinedTextField(
                    value =
                        state.lokacija,

                    onValueChange =
                        viewModel::onLokacijaChange,

                    label = {
                        Text(
                            "Lokacija"
                        )
                    },

                    leadingIcon = {

                        Icon(
                            imageVector =
                                Icons.Filled.LocationOn,

                            contentDescription =
                                null
                        )
                    },

                    supportingText = {

                        Text(
                            "${state.lokacija.length}/150"
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

                OutlinedButton(
                    onClick = {

                        showDatePicker =
                            true
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    shape =
                        RoundedCornerShape(
                            14.dp
                        ),

                    contentPadding =
                        PaddingValues(
                            horizontal =
                                16.dp,

                            vertical =
                                14.dp
                        )
                ) {

                    Icon(
                        imageVector =
                            Icons.Filled.CalendarMonth,

                        contentDescription =
                            null
                    )

                    Spacer(
                        modifier =
                            Modifier.size(
                                10.dp
                            )
                    )

                    Column(
                        modifier =
                            Modifier.weight(
                                1f
                            )
                    ) {

                        Text(
                            text =
                                "Datum polaska",

                            style =
                                MaterialTheme
                                    .typography
                                    .bodySmall
                        )

                        Text(
                            text =
                                state.datumPolaska
                                    .ifBlank {
                                        "Izaberi datum"
                                    },

                            style =
                                MaterialTheme
                                    .typography
                                    .labelLarge
                        )
                    }
                }
            }
        }

        /*
         * CATEGORY
         */
        item {

            FormSection(
                title =
                    "Vrsta ekspedicije",

                subtitle =
                    "Odredi zahtevnost i tip avanture."
            ) {

                Text(
                    text =
                        "Težina",

                    style =
                        MaterialTheme
                            .typography
                            .titleMedium
                )

                Spacer(
                    modifier =
                        Modifier.height(
                            8.dp
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

                    DifficultyChip(
                        text =
                            "Laka",

                        selected =
                            state.tezina ==
                                    "EASY",

                        onClick = {

                            viewModel
                                .onTezinaChange(
                                    "EASY"
                                )
                        },

                        modifier =
                            Modifier.weight(
                                1f
                            )
                    )

                    DifficultyChip(
                        text =
                            "Srednja",

                        selected =
                            state.tezina ==
                                    "MEDIUM",

                        onClick = {

                            viewModel
                                .onTezinaChange(
                                    "MEDIUM"
                                )
                        },

                        modifier =
                            Modifier.weight(
                                1f
                            )
                    )

                    DifficultyChip(
                        text =
                            "Teška",

                        selected =
                            state.tezina ==
                                    "HARD",

                        onClick = {

                            viewModel
                                .onTezinaChange(
                                    "HARD"
                                )
                        },

                        modifier =
                            Modifier.weight(
                                1f
                            )
                    )
                }

                Spacer(
                    modifier =
                        Modifier.height(
                            16.dp
                        )
                )

                ExposedDropdownMenuBox(
                    expanded =
                        typeMenuExpanded,

                    onExpandedChange = {

                        if (
                            !state.isLoadingTypes
                        ) {

                            typeMenuExpanded =
                                !typeMenuExpanded
                        }
                    }
                ) {

                    OutlinedTextField(
                        value =
                            selectedType
                                ?.tip
                                .orEmpty(),

                        onValueChange = {},

                        readOnly =
                            true,

                        label = {

                            Text(
                                "Tip ekspedicije"
                            )
                        },

                        placeholder = {

                            Text(
                                if (
                                    state.isLoadingTypes
                                ) {
                                    "Učitavanje tipova..."
                                } else {
                                    "Izaberi tip"
                                }
                            )
                        },

                        trailingIcon = {

                            if (
                                state.isLoadingTypes
                            ) {

                                CircularProgressIndicator(
                                    modifier =
                                        Modifier.size(
                                            18.dp
                                        ),

                                    strokeWidth =
                                        2.dp
                                )

                            } else {

                                ExposedDropdownMenuDefaults
                                    .TrailingIcon(
                                        expanded =
                                            typeMenuExpanded
                                    )
                            }
                        },

                        enabled =
                            !state.isLoadingTypes,

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
                            typeMenuExpanded,

                        onDismissRequest = {

                            typeMenuExpanded =
                                false
                        }
                    ) {

                        state.expeditionTypes
                            .forEach { type ->

                                DropdownMenuItem(
                                    text = {

                                        Text(
                                            type.tip
                                        )
                                    },

                                    onClick = {

                                        viewModel
                                            .onTypeSelected(
                                                type.idTipEkspedicije
                                            )

                                        typeMenuExpanded =
                                            false
                                    }
                                )
                            }
                    }
                }
            }
        }

        /*
         * METRICS
         */
        item {

            FormSection(
                title =
                    "Trajanje i dužina",

                subtitle =
                    "Unesi približne vrednosti cele ekspedicije."
            ) {

                Row(
                    modifier =
                        Modifier.fillMaxWidth(),

                    horizontalArrangement =
                        Arrangement.spacedBy(
                            10.dp
                        )
                ) {

                    OutlinedTextField(
                        value =
                            state.trajanjeMin,

                        onValueChange =
                            viewModel::
                            onTrajanjeMinChange,

                        label = {

                            Text(
                                "Trajanje"
                            )
                        },

                        suffix = {

                            Text(
                                "min"
                            )
                        },

                        leadingIcon = {

                            Icon(
                                imageVector =
                                    Icons.Filled.Schedule,

                                contentDescription =
                                    null
                            )
                        },

                        keyboardOptions =
                            KeyboardOptions(
                                keyboardType =
                                    KeyboardType.Number
                            ),

                        modifier =
                            Modifier.weight(
                                1f
                            ),

                        singleLine =
                            true,

                        shape =
                            RoundedCornerShape(
                                14.dp
                            )
                    )

                    OutlinedTextField(
                        value =
                            state.duzinaKm,

                        onValueChange =
                            viewModel::
                            onDuzinaKmChange,

                        label = {

                            Text(
                                "Dužina"
                            )
                        },

                        suffix = {

                            Text(
                                "km"
                            )
                        },

                        leadingIcon = {

                            Icon(
                                imageVector =
                                    Icons.Filled.Straighten,

                                contentDescription =
                                    null
                            )
                        },

                        keyboardOptions =
                            KeyboardOptions(
                                keyboardType =
                                    KeyboardType.Decimal
                            ),

                        modifier =
                            Modifier.weight(
                                1f
                            ),

                        singleLine =
                            true,

                        shape =
                            RoundedCornerShape(
                                14.dp
                            )
                    )
                }
            }
        }

        /*
         * EXTERNAL ROUTE
         */
        item {

            FormSection(
                title =
                    "Spoljna ruta",

                subtitle =
                    "Opcionalno dodaj link do mape ili detaljnije rute."
            ) {

                OutlinedTextField(
                    value =
                        state.externalUrl,

                    onValueChange =
                        viewModel::
                        onExternalUrlChange,

                    label = {

                        Text(
                            "URL rute"
                        )
                    },

                    placeholder = {

                        Text(
                            "https://..."
                        )
                    },

                    leadingIcon = {

                        Icon(
                            imageVector =
                                Icons.Filled.Link,

                            contentDescription =
                                null
                        )
                    },

                    supportingText = {

                        Text(
                            "Opciono • do 500 karaktera"
                        )
                    },

                    keyboardOptions =
                        KeyboardOptions(
                            keyboardType =
                                KeyboardType.Uri
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
            }
        }

        /*
         * ERROR
         */
        state.errorMessage
            ?.let { error ->

                item {

                    FormErrorMessage(
                        message =
                            error
                    )
                }
            }

        /*
         * SAVE / CANCEL
         */
        item {

            FormActions(
                isCreate =
                    isCreate,

                isSaving =
                    state.isSaving,

                onCancel =
                    onCancel,

                onSave =
                    viewModel::save
            )
        }
    }
}

@Composable
private fun FormHeader(
    isCreate: Boolean
) {

    Column {

        Text(
            text =
                if (isCreate) {
                    "Nova ekspedicija"
                } else {
                    "Izmeni ekspediciju"
                },

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
                    3.dp
                )
        )

        Text(
            text =
                if (isCreate) {

                    "Podeli sledeću avanturu sa HIMAL zajednicom."

                } else {

                    "Ažuriraj informacije o svojoj ekspediciji."
                },

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
private fun FormSection(
    title: String,
    subtitle: String,
    content: @Composable () -> Unit
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
                    title,

                style =
                    MaterialTheme
                        .typography
                        .titleLarge
            )

            Spacer(
                modifier =
                    Modifier.height(
                        2.dp
                    )
            )

            Text(
                text =
                    subtitle,

                style =
                    MaterialTheme
                        .typography
                        .bodySmall,

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

            content()
        }
    }
}

@Composable
private fun DifficultyChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    FilterChip(
        selected =
            selected,

        onClick =
            onClick,

        label = {

            Text(
                text =
                    text
            )
        },

        modifier =
            modifier
    )
}

@Composable
private fun FormActions(
    isCreate: Boolean,
    isSaving: Boolean,
    onCancel: () -> Unit,
    onSave: () -> Unit
) {

    Column(
        verticalArrangement =
            Arrangement.spacedBy(
                8.dp
            )
    ) {

        Button(
            onClick =
                onSave,

            enabled =
                !isSaving,

            modifier =
                Modifier.fillMaxWidth()
        ) {

            if (isSaving) {

                CircularProgressIndicator(
                    modifier =
                        Modifier.size(
                            18.dp
                        ),

                    strokeWidth =
                        2.dp
                )

            } else {

                Icon(
                    imageVector =
                        if (isCreate) {
                            Icons.Filled.Add
                        } else {
                            Icons.Filled.Save
                        },

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
                    text =
                        if (isCreate) {
                            "Kreiraj ekspediciju"
                        } else {
                            "Sačuvaj izmene"
                        }
                )
            }
        }

        OutlinedButton(
            onClick =
                onCancel,

            enabled =
                !isSaving,

            modifier =
                Modifier.fillMaxWidth()
        ) {

            Text(
                "Otkaži"
            )
        }
    }
}

@Composable
private fun FormErrorMessage(
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

@OptIn(
    ExperimentalMaterial3Api::class
)
@Composable
private fun ExpeditionDatePicker(
    currentDate: String,
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {

    val initialMillis =
        try {

            LocalDate
                .parse(
                    currentDate
                )
                .atStartOfDay(
                    ZoneOffset.UTC
                )
                .toInstant()
                .toEpochMilli()

        } catch (_: Exception) {

            LocalDate
                .now()
                .atStartOfDay(
                    ZoneOffset.UTC
                )
                .toInstant()
                .toEpochMilli()
        }

    val todayMillis =
        LocalDate
            .now()
            .atStartOfDay(
                ZoneOffset.UTC
            )
            .toInstant()
            .toEpochMilli()

    val datePickerState =
        rememberDatePickerState(
            initialSelectedDateMillis =
                initialMillis,

            selectableDates =
                object : SelectableDates {

                    override fun isSelectableDate(
                        utcTimeMillis: Long
                    ): Boolean {

                        return utcTimeMillis >=
                                todayMillis
                    }
                }
        )

    DatePickerDialog(
        onDismissRequest =
            onDismiss,

        confirmButton = {

            TextButton(
                onClick = {

                    datePickerState
                        .selectedDateMillis
                        ?.let { millis ->

                            val date =
                                Instant
                                    .ofEpochMilli(
                                        millis
                                    )
                                    .atZone(
                                        ZoneOffset.UTC
                                    )
                                    .toLocalDate()
                                    .toString()

                            onDateSelected(
                                date
                            )
                        }
                }
            ) {

                Text(
                    "Potvrdi"
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
    ) {

        DatePicker(
            state =
                datePickerState
        )
    }
}