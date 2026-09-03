package com.himal.mobile.ui.auth

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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Hiking
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    onBackToLogin: () -> Unit,
    onRegistrationSuccess: (String) -> Unit
) {

    val state by
    viewModel.uiState.collectAsState()

    var passwordVisible by
    remember {
        mutableStateOf(false)
    }

    var showDatePicker by
    remember {
        mutableStateOf(false)
    }

    LaunchedEffect(
        state.registrationSuccessful
    ) {

        if (state.registrationSuccessful) {

            val username =
                state.registeredUsername

            if (username != null) {

                onRegistrationSuccess(
                    username
                )
            }
        }
    }

    if (showDatePicker) {

        RegistrationBirthDatePicker(
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

    LazyColumn(
        modifier =
            Modifier.fillMaxSize(),

        contentPadding =
            PaddingValues(
                start = 24.dp,
                end = 24.dp,
                top = 32.dp,
                bottom = 32.dp
            ),

        verticalArrangement =
            Arrangement.spacedBy(
                18.dp
            ),

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        item {

            RegisterHeader()
        }

        item {

            RegisterFormCard(
                state =
                    state,

                viewModel =
                    viewModel,

                passwordVisible =
                    passwordVisible,

                onPasswordVisibilityChange = {

                    passwordVisible =
                        !passwordVisible
                },

                onDateClick = {

                    showDatePicker =
                        true
                }
            )
        }

        state.errorMessage
            ?.let { message ->

                item {

                    RegisterErrorMessage(
                        message =
                            message
                    )
                }
            }

        item {

            RegisterActions(
                isLoading =
                    state.isLoading,

                onRegister =
                    viewModel::register,

                onBackToLogin =
                    onBackToLogin
            )
        }
    }
}

@Composable
private fun RegisterHeader() {

    Column(
        modifier =
            Modifier.fillMaxWidth(),

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Surface(
            modifier =
                Modifier.size(
                    80.dp
                ),

            shape =
                CircleShape,

            color =
                MaterialTheme
                    .colorScheme
                    .primaryContainer
        ) {

            Column(
                modifier =
                    Modifier.fillMaxSize(),

                verticalArrangement =
                    Arrangement.Center,

                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {

                Icon(
                    imageVector =
                        Icons.Filled.Hiking,

                    contentDescription =
                        null,

                    tint =
                        MaterialTheme
                            .colorScheme
                            .primary,

                    modifier =
                        Modifier.size(
                            38.dp
                        )
                )
            }
        }

        Spacer(
            modifier =
                Modifier.height(
                    16.dp
                )
        )

        Text(
            text =
                "HIMAL",

            style =
                MaterialTheme
                    .typography
                    .headlineLarge,

            fontWeight =
                FontWeight.Bold,

            color =
                MaterialTheme
                    .colorScheme
                    .primary
        )

        Spacer(
            modifier =
                Modifier.height(
                    4.dp
                )
        )

        Text(
            text =
                "Kreiraj nalog",

            style =
                MaterialTheme
                    .typography
                    .headlineSmall
        )

        Spacer(
            modifier =
                Modifier.height(
                    4.dp
                )
        )

        Text(
            text =
                "Pridruži se zajednici i započni sledeću avanturu.",

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
private fun RegisterFormCard(
    state: RegisterUiState,
    viewModel: RegisterViewModel,
    passwordVisible: Boolean,
    onPasswordVisibilityChange: () -> Unit,
    onDateClick: () -> Unit
) {

    ElevatedCard(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(
                20.dp
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
                    "Lični podaci",

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
                    "Osnovni podaci tvog HIMAL profila.",

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
                        16.dp
                    )
            )

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
                        state.ime,

                    onValueChange =
                        viewModel::onImeChange,

                    label = {

                        Text(
                            "Ime"
                        )
                    },

                    leadingIcon = {

                        Icon(
                            imageVector =
                                Icons.Filled.Person,

                            contentDescription =
                                null
                        )
                    },

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
                        state.prezime,

                    onValueChange =
                        viewModel::onPrezimeChange,

                    label = {

                        Text(
                            "Prezime"
                        )
                    },

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

            Spacer(
                modifier =
                    Modifier.height(
                        12.dp
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
                        18.dp
                    )
            )

            Text(
                text =
                    "Podaci za prijavu",

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
                    "Username i lozinka koristiće se za pristup nalogu.",

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

            OutlinedTextField(
                value =
                    state.username,

                onValueChange =
                    viewModel::onUsernameChange,

                label = {

                    Text(
                        "Username"
                    )
                },

                leadingIcon = {

                    Icon(
                        imageVector =
                            Icons.Filled.Person,

                        contentDescription =
                            null
                    )
                },

                supportingText = {

                    Text(
                        "${state.username.length}/50 • najmanje 3 karaktera"
                    )
                },

                keyboardOptions =
                    KeyboardOptions(
                        keyboardType =
                            KeyboardType.Text
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
                        10.dp
                    )
            )

            OutlinedTextField(
                value =
                    state.password,

                onValueChange =
                    viewModel::onPasswordChange,

                label = {

                    Text(
                        "Lozinka"
                    )
                },

                leadingIcon = {

                    Icon(
                        imageVector =
                            Icons.Filled.Lock,

                        contentDescription =
                            null
                    )
                },

                trailingIcon = {

                    IconButton(
                        onClick =
                            onPasswordVisibilityChange
                    ) {

                        Icon(
                            imageVector =
                                if (passwordVisible) {
                                    Icons.Filled.VisibilityOff
                                } else {
                                    Icons.Filled.Visibility
                                },

                            contentDescription =
                                if (passwordVisible) {
                                    "Sakrij lozinku"
                                } else {
                                    "Prikaži lozinku"
                                }
                        )
                    }
                },

                visualTransformation =
                    if (passwordVisible) {

                        VisualTransformation.None

                    } else {

                        PasswordVisualTransformation()
                    },

                supportingText = {

                    Text(
                        "Najmanje 6 karaktera"
                    )
                },

                keyboardOptions =
                    KeyboardOptions(
                        keyboardType =
                            KeyboardType.Password
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
                        18.dp
                    )
            )

            Text(
                text =
                    "Datum rođenja",

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
                    "Izaberi datum pomoću kalendara.",

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
                        10.dp
                    )
            )

            OutlinedButton(
                onClick =
                    onDateClick,

                enabled =
                    !state.isLoading,

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
        }
    }
}

@Composable
private fun RegisterActions(
    isLoading: Boolean,
    onRegister: () -> Unit,
    onBackToLogin: () -> Unit
) {

    Column(
        modifier =
            Modifier.fillMaxWidth(),

        verticalArrangement =
            Arrangement.spacedBy(
                8.dp
            )
    ) {

        Button(
            onClick =
                onRegister,

            enabled =
                !isLoading,

            modifier =
                Modifier.fillMaxWidth()
        ) {

            if (isLoading) {

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
                        Icons.Filled.PersonAdd,

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
                    "Kreiraj nalog"
                )
            }
        }

        OutlinedButton(
            onClick =
                onBackToLogin,

            enabled =
                !isLoading,

            modifier =
                Modifier.fillMaxWidth()
        ) {

            Text(
                "Već imaš nalog? Prijavi se"
            )
        }
    }
}

@Composable
private fun RegisterErrorMessage(
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RegistrationBirthDatePicker(
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