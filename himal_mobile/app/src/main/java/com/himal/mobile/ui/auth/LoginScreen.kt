package com.himal.mobile.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onRegisterClick: () -> Unit
) {

    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "HIMAL",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "Alpinism pocket guide",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(
            modifier = Modifier.height(32.dp)
        )

        OutlinedTextField(
            value = state.username,
            onValueChange = viewModel::onUsernameChange,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Korisničko ime")
            },
            singleLine = true,
            enabled = !state.isLoading
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        OutlinedTextField(
            value = state.password,
            onValueChange = viewModel::onPasswordChange,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Lozinka")
            },
            singleLine = true,
            enabled = !state.isLoading,
            visualTransformation =
                PasswordVisualTransformation()
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Button(
            onClick = viewModel::login,
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading
        ) {

            if (state.isLoading) {

                CircularProgressIndicator()

            } else {

                Text("Prijavi se")
            }
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        OutlinedButton(
            onClick = onRegisterClick,
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Nemaš nalog? Registruj se")
        }

        state.errorMessage?.let { error ->

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                text = error,
                color = MaterialTheme.colorScheme.error
            )
        }

        if (state.isLoggedIn) {

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                text =
                    "Uspešna prijava: " +
                            "${state.loggedInUsername}"
            )
        }
    }
}