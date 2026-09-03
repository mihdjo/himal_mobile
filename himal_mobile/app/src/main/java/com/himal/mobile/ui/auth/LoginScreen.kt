package com.himal.mobile.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onRegisterClick: () -> Unit
) {

    val state by
    viewModel.uiState.collectAsState()

    var passwordVisible by
    remember {
        mutableStateOf(false)
    }

    val focusManager =
        LocalFocusManager.current

    /*
     * Dok proveravamo postoji li već
     * sačuvana JWT sesija, ne prikazujemo
     * Login formu.
     */
    if (state.isCheckingSession) {

        SessionLoadingScreen()

        return
    }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 24.dp,
                    vertical = 32.dp
                ),

        verticalArrangement =
            Arrangement.Center,

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        LoginHeader()

        Spacer(
            modifier =
                Modifier.height(
                    30.dp
                )
        )

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
                        20.dp
                    )
            ) {

                Text(
                    text =
                        "Dobrodošao nazad",

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
                        "Prijavi se i nastavi svoju sledeću avanturu.",

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
                        state.username,

                    onValueChange =
                        viewModel::onUsernameChange,

                    modifier =
                        Modifier.fillMaxWidth(),

                    label = {

                        Text(
                            "Korisničko ime"
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

                    singleLine =
                        true,

                    enabled =
                        !state.isLoading,

                    keyboardOptions =
                        KeyboardOptions(
                            keyboardType =
                                KeyboardType.Text,

                            imeAction =
                                ImeAction.Next
                        ),

                    keyboardActions =
                        KeyboardActions(
                            onNext = {

                                focusManager
                                    .moveFocus(
                                        FocusDirection.Down
                                    )
                            }
                        ),

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
                        state.password,

                    onValueChange =
                        viewModel::onPasswordChange,

                    modifier =
                        Modifier.fillMaxWidth(),

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
                            onClick = {

                                passwordVisible =
                                    !passwordVisible
                            }
                        ) {

                            Icon(
                                imageVector =
                                    if (
                                        passwordVisible
                                    ) {
                                        Icons.Filled
                                            .VisibilityOff
                                    } else {
                                        Icons.Filled
                                            .Visibility
                                    },

                                contentDescription =
                                    if (
                                        passwordVisible
                                    ) {
                                        "Sakrij lozinku"
                                    } else {
                                        "Prikaži lozinku"
                                    }
                            )
                        }
                    },

                    visualTransformation =
                        if (
                            passwordVisible
                        ) {

                            VisualTransformation.None

                        } else {

                            PasswordVisualTransformation()
                        },

                    keyboardOptions =
                        KeyboardOptions(
                            keyboardType =
                                KeyboardType.Password,

                            imeAction =
                                ImeAction.Done
                        ),

                    keyboardActions =
                        KeyboardActions(
                            onDone = {

                                focusManager
                                    .clearFocus()

                                if (
                                    !state.isLoading
                                ) {

                                    viewModel.login()
                                }
                            }
                        ),

                    singleLine =
                        true,

                    enabled =
                        !state.isLoading,

                    shape =
                        RoundedCornerShape(
                            14.dp
                        )
                )

                state.errorMessage
                    ?.let { error ->

                        Spacer(
                            modifier =
                                Modifier.height(
                                    14.dp
                                )
                        )

                        LoginErrorMessage(
                            message =
                                error
                        )
                    }

                Spacer(
                    modifier =
                        Modifier.height(
                            20.dp
                        )
                )

                Button(
                    onClick = {

                        focusManager
                            .clearFocus()

                        viewModel.login()
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    enabled =
                        !state.isLoading,

                    contentPadding =
                        PaddingValues(
                            vertical =
                                13.dp
                        )
                ) {

                    if (
                        state.isLoading
                    ) {

                        CircularProgressIndicator(
                            modifier =
                                Modifier.size(
                                    18.dp
                                ),

                            strokeWidth =
                                2.dp,

                            color =
                                MaterialTheme
                                    .colorScheme
                                    .onPrimary
                        )

                    } else {

                        Icon(
                            imageVector =
                                Icons.Filled.Login,

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
                            "Prijavi se"
                        )
                    }
                }

                Spacer(
                    modifier =
                        Modifier.height(
                            10.dp
                        )
                )

                OutlinedButton(
                    onClick =
                        onRegisterClick,

                    enabled =
                        !state.isLoading,

                    modifier =
                        Modifier.fillMaxWidth(),

                    contentPadding =
                        PaddingValues(
                            vertical =
                                13.dp
                        )
                ) {

                    Text(
                        "Nemaš nalog? Kreiraj ga"
                    )
                }
            }
        }

        Spacer(
            modifier =
                Modifier.height(
                    22.dp
                )
        )

        Text(
            text =
                "Planiraj. Pripremi. Istraži.",

            style =
                MaterialTheme
                    .typography
                    .labelLarge,

            color =
                MaterialTheme
                    .colorScheme
                    .onSurfaceVariant
        )
    }
}

@Composable
private fun LoginHeader() {

    Column(
        modifier =
            Modifier.fillMaxWidth(),

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Surface(
            modifier =
                Modifier.size(
                    96.dp
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

                horizontalAlignment =
                    Alignment.CenterHorizontally,

                verticalArrangement =
                    Arrangement.Center
            ) {

                Icon(
                    imageVector =
                        Icons.Filled.Landscape,

                    contentDescription =
                        "HIMAL",

                    modifier =
                        Modifier.size(
                            54.dp
                        ),

                    tint =
                        MaterialTheme
                            .colorScheme
                            .primary
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
                    3.dp
                )
        )

        Text(
            text =
                "Alpinism pocket guide",

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
private fun LoginErrorMessage(
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

            style =
                MaterialTheme
                    .typography
                    .bodyMedium,

            color =
                MaterialTheme
                    .colorScheme
                    .onErrorContainer,

            modifier =
                Modifier.padding(
                    12.dp
                )
        )
    }
}

@Composable
private fun SessionLoadingScreen() {

    Column(
        modifier =
            Modifier.fillMaxSize(),

        verticalArrangement =
            Arrangement.Center,

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Surface(
            modifier =
                Modifier.size(
                    82.dp
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
                        Icons.Filled.Landscape,

                    contentDescription =
                        null,

                    modifier =
                        Modifier.size(
                            44.dp
                        ),

                    tint =
                        MaterialTheme
                            .colorScheme
                            .primary
                )
            }
        }

        Spacer(
            modifier =
                Modifier.height(
                    18.dp
                )
        )

        CircularProgressIndicator(
            modifier =
                Modifier.size(
                    28.dp
                )
        )
    }
}