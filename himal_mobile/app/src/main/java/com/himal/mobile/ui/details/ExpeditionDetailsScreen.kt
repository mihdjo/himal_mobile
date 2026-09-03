package com.himal.mobile.ui.details

import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.himal.mobile.data.remote.dto.EkspedicijaOpremaResponse
import com.himal.mobile.data.remote.dto.EkspedicijaResponse

@Composable
fun ExpeditionDetailsScreen(
    expeditionId: Long,
    viewModel: ExpeditionDetailsViewModel,
    onBack: () -> Unit,
    onSessionExpired: () -> Unit
) {

    val state by
    viewModel.uiState.collectAsState()

    LaunchedEffect(expeditionId) {
        viewModel.loadExpedition(
            expeditionId
        )
    }

    LaunchedEffect(
        state.sessionExpired
    ) {

        if (state.sessionExpired) {
            onSessionExpired()
        }
    }

    when {

        state.isLoading -> {

            LoadingDetails()
        }

        state.errorMessage != null -> {

            DetailsError(
                message =
                    state.errorMessage
                        ?: "Nepoznata greška.",

                onBack =
                    onBack
            )
        }

        state.expedition != null -> {

            val expedition =
                state.expedition!!

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
                        16.dp
                    )
            ) {

                /*
                 * BACK
                 */
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

                /*
                 * HEADER
                 */
                item {

                    ExpeditionHeader(
                        expedition =
                            expedition
                    )
                }

                /*
                 * METRICS
                 */
                item {

                    ExpeditionMetrics(
                        expedition =
                            expedition
                    )
                }

                /*
                 * DESCRIPTION
                 */
                item {

                    DetailsSection(
                        title =
                            "O ekspediciji"
                    ) {

                        Text(
                            text =
                                expedition.opis,

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

                /*
                 * ACTIONS
                 */
                item {

                    ExpeditionActions(
                        isSaved =
                            state.isSaved,

                        isSavedLoading =
                            state.isSavedLoading,

                        isInPlan =
                            state.isInPlan,

                        isPlanLoading =
                            state.isPlanLoading,

                        onToggleSaved =
                            viewModel::toggleSaved,

                        onTogglePlan =
                            viewModel::togglePlan
                    )
                }

                /*
                 * ACTION ERROR
                 */
                state.actionErrorMessage
                    ?.let { error ->

                        item {

                            ErrorMessage(
                                message =
                                    error
                            )
                        }
                    }

                /*
                 * EXTERNAL ROUTE
                 */
                expedition.externalUrl
                    ?.takeIf {
                        it.isNotBlank()
                    }
                    ?.let { url ->

                        item {

                            ExternalRouteButton(
                                url =
                                    url
                            )
                        }
                    }

                /*
                 * EQUIPMENT HEADER
                 */
                item {

                    Column {

                        Text(
                            text =
                                "Potrebna oprema",

                            style =
                                MaterialTheme
                                    .typography
                                    .titleLarge
                        )

                        Spacer(
                            modifier =
                                Modifier.height(
                                    3.dp
                                )
                        )

                        Text(
                            text =
                                "Pripremi se pre polaska.",

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

                /*
                 * EQUIPMENT ERROR
                 */
                if (
                    state.equipmentErrorMessage
                    != null
                ) {

                    item {

                        ErrorMessage(
                            message =
                                state.equipmentErrorMessage
                                    ?: "Greška pri učitavanju opreme."
                        )
                    }

                } else if (
                    state.equipment.isEmpty()
                ) {

                    item {

                        Surface(
                            modifier =
                                Modifier.fillMaxWidth(),

                            shape =
                                RoundedCornerShape(
                                    16.dp
                                ),

                            color =
                                MaterialTheme
                                    .colorScheme
                                    .surfaceVariant
                        ) {

                            Text(
                                text =
                                    "Za ovu ekspediciju nije definisana oprema.",

                                style =
                                    MaterialTheme
                                        .typography
                                        .bodyMedium,

                                color =
                                    MaterialTheme
                                        .colorScheme
                                        .onSurfaceVariant,

                                modifier =
                                    Modifier.padding(
                                        18.dp
                                    )
                            )
                        }
                    }

                } else {

                    items(
                        items =
                            state.equipment,

                        key = {
                            it.idOpreme
                        }
                    ) { equipment ->

                        EquipmentCard(
                            equipment =
                                equipment
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadingDetails() {

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
private fun DetailsError(
    message: String,
    onBack: () -> Unit
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

        OutlinedButton(
            onClick =
                onBack
        ) {

            Icon(
                imageVector =
                    Icons.Filled.ArrowBack,

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
                "Nazad"
            )
        }
    }
}

@Composable
private fun ExpeditionHeader(
    expedition: EkspedicijaResponse
) {

    Column {

        Text(
            text =
                expedition.naziv,

            style =
                MaterialTheme
                    .typography
                    .headlineMedium,

            color =
                MaterialTheme
                    .colorScheme
                    .onBackground
        )

        Spacer(
            modifier =
                Modifier.height(
                    8.dp
                )
        )

        Row(
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Icon(
                imageVector =
                    Icons.Filled.LocationOn,

                contentDescription =
                    null,

                tint =
                    MaterialTheme
                        .colorScheme
                        .primary,

                modifier =
                    Modifier.size(
                        18.dp
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
                    expedition.lokacija,

                style =
                    MaterialTheme
                        .typography
                        .bodyLarge,

                color =
                    MaterialTheme
                        .colorScheme
                        .primary
            )
        }

        Spacer(
            modifier =
                Modifier.height(
                    14.dp
                )
        )

        Row(
            horizontalArrangement =
                Arrangement.spacedBy(
                    8.dp
                )
        ) {

            DifficultyBadge(
                difficulty =
                    expedition.tezina
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
                        expedition
                            .tipEkspedicije,

                    style =
                        MaterialTheme
                            .typography
                            .labelMedium,

                    color =
                        MaterialTheme
                            .colorScheme
                            .onSecondaryContainer,

                    modifier =
                        Modifier.padding(
                            horizontal =
                                11.dp,

                            vertical =
                                6.dp
                        )
                )
            }
        }
    }
}

@Composable
private fun ExpeditionMetrics(
    expedition: EkspedicijaResponse
) {

    Column(
        verticalArrangement =
            Arrangement.spacedBy(
                8.dp
            )
    ) {

        Row(
            modifier =
                Modifier.fillMaxWidth(),

            horizontalArrangement =
                Arrangement.spacedBy(
                    8.dp
                )
        ) {

            DetailMetric(
                title =
                    "Dužina",

                value =
                    "${expedition.duzinaKm} km",

                modifier =
                    Modifier.weight(
                        1f
                    )
            )

            DetailMetric(
                title =
                    "Trajanje",

                value =
                    "${expedition.trajanjeMin} min",

                icon = {
                    Icon(
                        imageVector =
                            Icons.Filled.AccessTime,

                        contentDescription =
                            null,

                        modifier =
                            Modifier.size(
                                16.dp
                            )
                    )
                },

                modifier =
                    Modifier.weight(
                        1f
                    )
            )
        }

        Row(
            modifier =
                Modifier.fillMaxWidth(),

            horizontalArrangement =
                Arrangement.spacedBy(
                    8.dp
                )
        ) {

            DetailMetric(
                title =
                    "Polazak",

                value =
                    expedition.datumPolaska,

                icon = {

                    Icon(
                        imageVector =
                            Icons.Filled.CalendarToday,

                        contentDescription =
                            null,

                        modifier =
                            Modifier.size(
                                16.dp
                            )
                    )
                },

                modifier =
                    Modifier.weight(
                        1f
                    )
            )

            DetailMetric(
                title =
                    "Autor",

                value =
                    expedition.autorUsername,

                icon = {

                    Icon(
                        imageVector =
                            Icons.Filled.Person,

                        contentDescription =
                            null,

                        modifier =
                            Modifier.size(
                                16.dp
                            )
                    )
                },

                modifier =
                    Modifier.weight(
                        1f
                    )
            )
        }
    }
}

@Composable
private fun DetailMetric(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    icon: (@Composable () -> Unit)? =
        null
) {

    Surface(
        modifier =
            modifier,

        shape =
            RoundedCornerShape(
                14.dp
            ),

        color =
            MaterialTheme
                .colorScheme
                .surfaceVariant
    ) {

        Column(
            modifier =
                Modifier.padding(
                    13.dp
                )
        ) {

            Row(
                verticalAlignment =
                    Alignment.CenterVertically,

                horizontalArrangement =
                    Arrangement.spacedBy(
                        5.dp
                    )
            ) {

                icon?.invoke()

                Text(
                    text =
                        title,

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

            Spacer(
                modifier =
                    Modifier.height(
                        3.dp
                    )
            )

            Text(
                text =
                    value,

                style =
                    MaterialTheme
                        .typography
                        .labelLarge,

                color =
                    MaterialTheme
                        .colorScheme
                        .onSurface
            )
        }
    }
}

@Composable
private fun DetailsSection(
    title: String,
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
                        10.dp
                    )
            )

            content()
        }
    }
}

@Composable
private fun ExpeditionActions(
    isSaved: Boolean,
    isSavedLoading: Boolean,
    isInPlan: Boolean,
    isPlanLoading: Boolean,
    onToggleSaved: () -> Unit,
    onTogglePlan: () -> Unit
) {

    Column {

        Text(
            text =
                "Sačuvaj i isplaniraj",

            style =
                MaterialTheme
                    .typography
                    .titleMedium
        )

        Spacer(
            modifier =
                Modifier.height(
                    10.dp
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

            if (isSaved) {

                FilledTonalButton(
                    onClick =
                        onToggleSaved,

                    enabled =
                        !isSavedLoading,

                    modifier =
                        Modifier.weight(
                            1f
                        )
                ) {

                    if (isSavedLoading) {

                        SmallProgress()

                    } else {

                        Icon(
                            imageVector =
                                Icons.Filled.Favorite,

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
                            "Sačuvano"
                        )
                    }
                }

            } else {

                OutlinedButton(
                    onClick =
                        onToggleSaved,

                    enabled =
                        !isSavedLoading,

                    modifier =
                        Modifier.weight(
                            1f
                        )
                ) {

                    if (isSavedLoading) {

                        SmallProgress()

                    } else {

                        Icon(
                            imageVector =
                                Icons.Filled.FavoriteBorder,

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
                            "Sačuvaj"
                        )
                    }
                }
            }

            if (isInPlan) {

                FilledTonalButton(
                    onClick =
                        onTogglePlan,

                    enabled =
                        !isPlanLoading,

                    modifier =
                        Modifier.weight(
                            1f
                        )
                ) {

                    if (isPlanLoading) {

                        SmallProgress()

                    } else {

                        Icon(
                            imageVector =
                                Icons.Filled.CheckCircle,

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
                            "U planu"
                        )
                    }
                }

            } else {

                Button(
                    onClick =
                        onTogglePlan,

                    enabled =
                        !isPlanLoading,

                    modifier =
                        Modifier.weight(
                            1f
                        )
                ) {

                    if (isPlanLoading) {

                        SmallProgress()

                    } else {

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
                            "Moj plan"
                        )
                    }
                }
            }
        }
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
private fun ExternalRouteButton(
    url: String
) {

    val context =
        LocalContext.current

    OutlinedButton(
        onClick = {

            try {

                val normalizedUrl =
                    if (
                        url.startsWith(
                            "http://"
                        ) ||
                        url.startsWith(
                            "https://"
                        )
                    ) {

                        url

                    } else {

                        "https://$url"
                    }

                val intent =
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(
                            normalizedUrl
                        )
                    )

                context.startActivity(
                    intent
                )

            } catch (_: Exception) {

                Toast
                    .makeText(
                        context,
                        "Nije moguće otvoriti rutu.",
                        Toast.LENGTH_SHORT
                    )
                    .show()
            }
        },

        modifier =
            Modifier.fillMaxWidth()
    ) {

        Text(
            "Otvori spoljnu rutu"
        )

        Spacer(
            modifier =
                Modifier.size(
                    8.dp
                )
        )

        Icon(
            imageVector =
                Icons.Filled.OpenInNew,

            contentDescription =
                null,

            modifier =
                Modifier.size(
                    18.dp
                )
        )
    }
}

@Composable
private fun EquipmentCard(
    equipment:
    EkspedicijaOpremaResponse
) {

    ElevatedCard(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(
                16.dp
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
                    16.dp
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
                            .titleMedium,

                    modifier =
                        Modifier.weight(
                            1f
                        )
                )

                EquipmentRequirementBadge(
                    required =
                        equipment.obavezna
                )
            }

            Spacer(
                modifier =
                    Modifier.height(
                        10.dp
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
                        .surfaceVariant
            ) {

                Text(
                    text =
                        "Količina: ${equipment.kolicina}",

                    style =
                        MaterialTheme
                            .typography
                            .labelMedium,

                    modifier =
                        Modifier.padding(
                            horizontal =
                                10.dp,

                            vertical =
                                5.dp
                        )
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
                                10.dp
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
                                10.dp
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
                                    10.dp
                                )
                        )
                    }
                }
        }
    }
}

@Composable
private fun EquipmentRequirementBadge(
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
                    .surfaceVariant
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
                        .onSurfaceVariant
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

@Composable
private fun DifficultyBadge(
    difficulty: String
) {

    val containerColor =
        when (difficulty) {

            "EASY" ->
                MaterialTheme
                    .colorScheme
                    .primaryContainer

            "MEDIUM" ->
                MaterialTheme
                    .colorScheme
                    .secondaryContainer

            "HARD" ->
                MaterialTheme
                    .colorScheme
                    .errorContainer

            else ->
                MaterialTheme
                    .colorScheme
                    .surfaceVariant
        }

    val contentColor =
        when (difficulty) {

            "EASY" ->
                MaterialTheme
                    .colorScheme
                    .onPrimaryContainer

            "MEDIUM" ->
                MaterialTheme
                    .colorScheme
                    .onSecondaryContainer

            "HARD" ->
                MaterialTheme
                    .colorScheme
                    .onErrorContainer

            else ->
                MaterialTheme
                    .colorScheme
                    .onSurfaceVariant
        }

    Surface(
        shape =
            RoundedCornerShape(
                50.dp
            ),

        color =
            containerColor
    ) {

        Text(
            text =
                when (difficulty) {

                    "EASY" ->
                        "Laka"

                    "MEDIUM" ->
                        "Srednja"

                    "HARD" ->
                        "Teška"

                    else ->
                        difficulty
                },

            style =
                MaterialTheme
                    .typography
                    .labelMedium,

            fontWeight =
                FontWeight.Medium,

            color =
                contentColor,

            modifier =
                Modifier.padding(
                    horizontal =
                        11.dp,

                    vertical =
                        6.dp
                )
        )
    }
}