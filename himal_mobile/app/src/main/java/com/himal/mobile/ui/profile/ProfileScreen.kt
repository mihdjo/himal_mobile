package com.himal.mobile.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.ui.unit.dp
import com.himal.mobile.data.remote.dto.KorisnikResponse

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

        state.user == null -> {

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
                            ?: "Profil nije moguće učitati.",
                    color =
                        MaterialTheme.colorScheme.error
                )

                Spacer(
                    modifier =
                        Modifier.height(16.dp)
                )

                Button(
                    onClick =
                        viewModel::loadProfile
                ) {
                    Text("Pokušaj ponovo")
                }
            }
        }

        else -> {

            val user = state.user ?: return

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(
                        rememberScrollState()
                    )
                    .padding(16.dp)
            ) {

                Text(
                    text = "Moj profil",
                    style =
                        MaterialTheme.typography.headlineMedium
                )

                Spacer(
                    modifier =
                        Modifier.height(20.dp)
                )

                if (state.isEditing) {

                    EditProfileContent(
                        state = state,
                        viewModel = viewModel
                    )

                } else {

                    ProfileContent(
                        user = user,
                        onEdit =
                            viewModel::beginEdit,
                        onLogout =
                            onLogout
                    )
                }

                state.errorMessage?.let {

                    Spacer(
                        modifier =
                            Modifier.height(12.dp)
                    )

                    Text(
                        text = it,
                        color =
                            MaterialTheme.colorScheme.error
                    )
                }

                state.successMessage?.let {

                    Spacer(
                        modifier =
                            Modifier.height(12.dp)
                    )

                    Text(
                        text = it
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileContent(
    user: KorisnikResponse,
    onEdit: () -> Unit,
    onLogout: () -> Unit
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
                text =
                    "${user.ime} ${user.prezime}",
                style =
                    MaterialTheme.typography.headlineSmall
            )

            Text(
                text = "@${user.username}",
                style =
                    MaterialTheme.typography.bodyMedium
            )

            Spacer(
                modifier =
                    Modifier.height(20.dp)
            )

            ProfileField(
                label = "Email",
                value = user.email
            )

            ProfileField(
                label = "Datum rođenja",
                value =
                    user.datumRodjenja
            )

            ProfileField(
                label = "Član od",
                value =
                    formatCreatedDate(
                        user.datumKreiranja
                    )
            )
        }
    }

    Spacer(
        modifier =
            Modifier.height(16.dp)
    )

    Button(
        onClick = onEdit,
        modifier =
            Modifier.fillMaxWidth()
    ) {
        Text("Izmeni profil")
    }

    Spacer(
        modifier =
            Modifier.height(12.dp)
    )

    OutlinedButton(
        onClick = onLogout,
        modifier =
            Modifier.fillMaxWidth()
    ) {
        Text("Odjavi se")
    }
}

@Composable
private fun EditProfileContent(
    state: ProfileUiState,
    viewModel: ProfileViewModel
) {

    Text(
        text = "@${state.user?.username.orEmpty()}",
        style =
            MaterialTheme.typography.titleMedium
    )

    Spacer(
        modifier =
            Modifier.height(16.dp)
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
        modifier =
            Modifier.height(12.dp)
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
        modifier =
            Modifier.height(12.dp)
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
        modifier =
            Modifier.height(12.dp)
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

    Spacer(
        modifier =
            Modifier.height(20.dp)
    )

    Row(
        modifier =
            Modifier.fillMaxWidth(),
        horizontalArrangement =
            Arrangement.spacedBy(12.dp)
    ) {

        OutlinedButton(
            onClick =
                viewModel::cancelEdit,
            enabled =
                !state.isSaving,
            modifier =
                Modifier.weight(1f)
        ) {
            Text("Otkaži")
        }

        Button(
            onClick =
                viewModel::saveProfile,
            enabled =
                !state.isSaving,
            modifier =
                Modifier.weight(1f)
        ) {

            if (state.isSaving) {

                CircularProgressIndicator()

            } else {

                Text("Sačuvaj")
            }
        }
    }
}

@Composable
private fun ProfileField(
    label: String,
    value: String
) {

    Text(
        text = label,
        style =
            MaterialTheme.typography.labelMedium
    )

    Text(
        text = value,
        style =
            MaterialTheme.typography.bodyLarge
    )

    Spacer(
        modifier =
            Modifier.height(12.dp)
    )
}

private fun formatCreatedDate(
    value: String
): String {

    return value
        .substringBefore("T")
}