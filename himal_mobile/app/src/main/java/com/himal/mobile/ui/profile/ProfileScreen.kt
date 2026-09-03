package com.himal.mobile.ui.profile

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.himal.mobile.data.remote.dto.KorisnikResponse
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onLogout: () -> Unit
) {

    val state by
    viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    LaunchedEffect(
        state.sessionExpired
    ) {

        if (state.sessionExpired) {
            onLogout()
        }
    }

    when {

        state.isLoading -> {

            ProfileLoading()
        }

        state.user == null -> {

            ProfileLoadError(
                message =
                    state.errorMessage
                        ?: "Profil nije moguće učitati.",

                onRetry =
                    viewModel::loadProfile
            )
        }

        else -> {

            val user =
                state.user
                    ?: return

            LazyColumn(
                modifier =
                    Modifier.fillMaxSize(),

                contentPadding =
                    PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 20.dp,
                        bottom = 32.dp
                    ),

                verticalArrangement =
                    Arrangement.spacedBy(
                        16.dp
                    )
            ) {

                item {

                    ProfileHeader(
                        user =
                            user
                    )
                }

                state.successMessage
                    ?.let { message ->

                        item {

                            SuccessMessage(
                                message =
                                    message
                            )
                        }
                    }

                state.errorMessage
                    ?.let { message ->

                        item {

                            ErrorMessage(
                                message =
                                    message
                            )
                        }
                    }

                if (state.isEditing) {

                    item {

                        EditProfileContent(
                            state =
                                state,

                            viewModel =
                                viewModel
                        )
                    }

                } else {

                    item {

                        ProfileContent(
                            user =
                                user,

                            onEdit =
                                viewModel::beginEdit
                        )
                    }

                    item {

                        LogoutSection(
                            onLogout =
                                onLogout
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileHeader(
    user: KorisnikResponse
) {

    Column(
        horizontalAlignment =
            Alignment.CenterHorizontally,

        modifier =
            Modifier.fillMaxWidth()
    ) {

        Surface(
            shape =
                CircleShape,

            color =
                MaterialTheme
                    .colorScheme
                    .primaryContainer,

            modifier =
                Modifier.size(
                    86.dp
                )
        ) {

            Column(
                modifier =
                    Modifier.fillMaxSize(),

                verticalArrangement =
                    Arrangement.Center,

                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {

                Text(
                    text =
                        initials(
                            user.ime,
                            user.prezime
                        ),

                    style =
                        MaterialTheme
                            .typography
                            .headlineMedium,

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        MaterialTheme
                            .colorScheme
                            .primary
                )
            }
        }

        Spacer(
            modifier =
                Modifier.height(
                    14.dp
                )
        )

        Text(
            text =
                "${user.ime} ${user.prezime}",

            style =
                MaterialTheme
                    .typography
                    .headlineMedium
        )

        Spacer(
            modifier =
                Modifier.height(
                    3.dp
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
                    .secondaryContainer
        ) {

            Text(
                text =
                    "@${user.username}",

                style =
                    MaterialTheme
                        .typography
                        .labelLarge,

                color =
                    MaterialTheme
                        .colorScheme
                        .onSecondaryContainer,

                modifier =
                    Modifier.padding(
                        horizontal =
                            12.dp,

                        vertical =
                            6.dp
                    )
            )
        }
    }
}

@Composable
private fun ProfileContent(
    user: KorisnikResponse,
    onEdit: () -> Unit
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
                        "Podaci o profilu",

                    style =
                        MaterialTheme
                            .typography
                            .titleLarge
                )

                Icon(
                    imageVector =
                        Icons.Filled.Person,

                    contentDescription =
                        null,

                    tint =
                        MaterialTheme
                            .colorScheme
                            .primary
                )
            }

            Spacer(
                modifier =
                    Modifier.height(
                        18.dp
                    )
            )

            ProfileField(
                label =
                    "Email",

                value =
                    user.email,

                icon = {

                    Icon(
                        imageVector =
                            Icons.Filled.Email,

                        contentDescription =
                            null,

                        modifier =
                            Modifier.size(
                                18.dp
                            )
                    )
                }
            )

            Spacer(
                modifier =
                    Modifier.height(
                        10.dp
                    )
            )

            ProfileField(
                label =
                    "Datum rođenja",

                value =
                    user.datumRodjenja,

                icon = {

                    Icon(
                        imageVector =
                            Icons.Filled.CalendarMonth,

                        contentDescription =
                            null,

                        modifier =
                            Modifier.size(
                                18.dp
                            )
                    )
                }
            )

            Spacer(
                modifier =
                    Modifier.height(
                        10.dp
                    )
            )

            ProfileField(
                label =
                    "Član HIMAL-a od",

                value =
                    formatCreatedDate(
                        user.datumKreiranja
                    )
            )
        }
    }

    Spacer(
        modifier =
            Modifier.height(
                12.dp
            )
    )

    Button(
        onClick =
            onEdit,

        modifier =
            Modifier.fillMaxWidth()
    ) {

        Icon(
            imageVector =
                Icons.Filled.Edit,

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
            "Izmeni profil"
        )
    }
}

@Composable
private fun ProfileField(
    label: String,
    value: String,
    icon: (@Composable () -> Unit)? =
        null
) {

    Surface(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(
                14.dp
            ),

        color =
            MaterialTheme
                .colorScheme
                .surfaceVariant
    ) {

        Row(
            modifier =
                Modifier.padding(
                    14.dp
                ),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            if (icon != null) {

                Surface(
                    shape =
                        CircleShape,

                    color =
                        MaterialTheme
                            .colorScheme
                            .surface
                ) {

                    Column(
                        modifier =
                            Modifier.padding(
                                8.dp
                            )
                    ) {

                        icon()
                    }
                }

                Spacer(
                    modifier =
                        Modifier.size(
                            12.dp
                        )
                )
            }

            Column {

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

                Spacer(
                    modifier =
                        Modifier.height(
                            2.dp
                        )
                )

                Text(
                    text =
                        value,

                    style =
                        MaterialTheme
                            .typography
                            .bodyLarge
                )
            }
        }
    }
}

@OptIn(
    ExperimentalMaterial3Api::class
)
@Composable
private fun EditProfileContent(
    state: ProfileUiState,
    viewModel: ProfileViewModel
) {

    var showDatePicker by
    remember {
        mutableStateOf(
            false
        )
    }

    if (showDatePicker) {

        BirthDatePicker(
            currentDate =
                state.datumRodjenja,

            onDateSelected = { date ->

                viewModel
                    .onDatumRodjenjaChange(
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
                    "Izmeni profil",

                style =
                    MaterialTheme
                        .typography
                        .titleLarge
            )

            Spacer(
                modifier =
                    Modifier.height(
                        4.dp
                    )
            )

            Text(
                text =
                    "@${state.user?.username.orEmpty()}",

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
                        18.dp
                    )
            )

            OutlinedTextField(
                value =
                    state.ime,

                onValueChange =
                    viewModel::onImeChange,

                label = {

                    Text(
                        "Ime"
                    )
                },

                supportingText = {

                    Text(
                        "${state.ime.length}/50"
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
                        10.dp
                    )
            )

            OutlinedTextField(
                value =
                    state.prezime,

                onValueChange =
                    viewModel::onPrezimeChange,

                label = {

                    Text(
                        "Prezime"
                    )
                },

                supportingText = {

                    Text(
                        "${state.prezime.length}/50"
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
                        10.dp
                    )
            )

            OutlinedTextField(
                value =
                    state.email,

                onValueChange =
                    viewModel::onEmailChange,

                label = {

                    Text(
                        "Email"
                    )
                },

                leadingIcon = {

                    Icon(
                        imageVector =
                            Icons.Filled.Email,

                        contentDescription =
                            null
                    )
                },

                keyboardOptions =
                    KeyboardOptions(
                        keyboardType =
                            KeyboardType.Email
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
                            "Datum rođenja",

                        style =
                            MaterialTheme
                                .typography
                                .bodySmall
                    )

                    Text(
                        text =
                            state.datumRodjenja
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

            Spacer(
                modifier =
                    Modifier.height(
                        18.dp
                    )
            )

            Button(
                onClick =
                    viewModel::saveProfile,

                enabled =
                    !state.isSaving,

                modifier =
                    Modifier.fillMaxWidth()
            ) {

                if (state.isSaving) {

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
                            Icons.Filled.Save,

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
                        "Sačuvaj izmene"
                    )
                }
            }

            Spacer(
                modifier =
                    Modifier.height(
                        8.dp
                    )
            )

            OutlinedButton(
                onClick =
                    viewModel::cancelEdit,

                enabled =
                    !state.isSaving,

                modifier =
                    Modifier.fillMaxWidth()
            ) {

                Text(
                    "Otkaži"
                )
            }
        }
    }
}

@Composable
private fun LogoutSection(
    onLogout: () -> Unit
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
                    "Nalog",

                style =
                    MaterialTheme
                        .typography
                        .titleMedium
            )

            Spacer(
                modifier =
                    Modifier.height(
                        4.dp
                    )
            )

            Text(
                text =
                    "Odjava će ukloniti lokalnu sesiju i checklist podatke ovog korisnika sa uređaja.",

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

            OutlinedButton(
                onClick =
                    onLogout,

                modifier =
                    Modifier.fillMaxWidth(),

                colors =
                    ButtonDefaults
                        .outlinedButtonColors(
                            contentColor =
                                MaterialTheme
                                    .colorScheme
                                    .error
                        )
            ) {

                Icon(
                    imageVector =
                        Icons.Filled.Logout,

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
                    "Odjavi se"
                )
            }
        }
    }
}

@Composable
private fun SuccessMessage(
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
                .primaryContainer
    ) {

        Text(
            text =
                message,

            color =
                MaterialTheme
                    .colorScheme
                    .onPrimaryContainer,

            modifier =
                Modifier.padding(
                    14.dp
                )
        )
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

@OptIn(
    ExperimentalMaterial3Api::class
)
@Composable
private fun BirthDatePicker(
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
                .minusYears(
                    18
                )
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

                        return utcTimeMillis <
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

@Composable
private fun ProfileLoading() {

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
private fun ProfileLoadError(
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

private fun initials(
    firstName: String,
    lastName: String
): String {

    val first =
        firstName
            .trim()
            .firstOrNull()
            ?.uppercaseChar()
            ?.toString()
            .orEmpty()

    val last =
        lastName
            .trim()
            .firstOrNull()
            ?.uppercaseChar()
            ?.toString()
            .orEmpty()

    return first + last
}

private fun formatCreatedDate(
    value: String
): String {

    return value
        .substringBefore(
            "T"
        )
}