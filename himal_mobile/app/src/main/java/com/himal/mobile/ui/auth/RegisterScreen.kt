package com.himal.mobile.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    onBackToLogin: () -> Unit,
    onRegistrationSuccess: (String) -> Unit
) {

    val state by
    viewModel.uiState.collectAsState()

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState()
            )
            .padding(24.dp),
        verticalArrangement =
            Arrangement.Center,
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Text(
            text = "Kreiraj nalog",
            style =
                MaterialTheme.typography.headlineMedium
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "Pridruži se HIMAL zajednici"
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        OutlinedTextField(
            value = state.ime,
            onValueChange =
                viewModel::onImeChange,
            label = {
                Text("Ime")
            },
            modifier =
                Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        OutlinedTextField(
            value = state.prezime,
            onValueChange =
                viewModel::onPrezimeChange,
            label = {
                Text("Prezime")
            },
            modifier =
                Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        OutlinedTextField(
            value = state.email,
            onValueChange =
                viewModel::onEmailChange,
            label = {
                Text("Email")
            },
            modifier =
                Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        OutlinedTextField(
            value = state.username,
            onValueChange =
                viewModel::onUsernameChange,
            label = {
                Text("Username")
            },
            modifier =
                Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        OutlinedTextField(
            value = state.password,
            onValueChange =
                viewModel::onPasswordChange,
            label = {
                Text("Lozinka")
            },
            visualTransformation =
                PasswordVisualTransformation(),
            modifier =
                Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        OutlinedTextField(
            value = state.datumRodjenja,
            onValueChange =
                viewModel::onDatumRodjenjaChange,
            label = {
                Text("Datum rođenja")
            },
            supportingText = {
                Text("Format: YYYY-MM-DD")
            },
            modifier =
                Modifier.fillMaxWidth(),
            singleLine = true
        )

        state.errorMessage?.let {

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = it,
                color =
                    MaterialTheme.colorScheme.error
            )
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Button(
            onClick =
                viewModel::register,
            enabled =
                !state.isLoading,
            modifier =
                Modifier.fillMaxWidth()
        ) {

            if (state.isLoading) {

                CircularProgressIndicator()

            } else {

                Text("Registruj se")
            }
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        OutlinedButton(
            onClick = onBackToLogin,
            enabled = !state.isLoading,
            modifier =
                Modifier.fillMaxWidth()
        ) {

            Text(
                "Već imaš nalog? Prijavi se"
            )
        }
    }
}