package org.proven.diarideviatges.ui.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.proven.diarideviatges.model.RouteDifficulty
import org.proven.diarideviatges.model.TravelDiaryUiState
import org.proven.diarideviatges.viewmodel.TravelDiaryViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(
    viewModel: TravelDiaryViewModel,
    uiState: TravelDiaryUiState,
    onNavigateToPostal: () -> Unit,
    onNavigateToHistory: () -> Unit
) {
    val context = LocalContext.current
    var showDatePicker by remember { mutableStateOf(false) }

    // Mostrar missatges d'usuari (Toasts) si n'hi ha
    LaunchedEffect(uiState.userMessage) {
        uiState.userMessage?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            viewModel.clearUserMessage()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Map,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(" Nova Ruta de Viatge")
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToHistory) {
                        BadgedBox(
                            badge = {
                                if (uiState.savedRoutes.isNotEmpty()) {
                                    Badge { Text("${uiState.savedRoutes.size}") }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.History,
                                contentDescription = "Veure Històric"
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Formulari de Registre de Ruta",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            // Nom de la Ruta
            OutlinedTextField(
                value = uiState.routeNameInput,
                onValueChange = { viewModel.onRouteNameChange(it) },
                label = { Text("Nom de la ruta *") },
                placeholder = { Text("Ex: Ruta dels 7 Llacs") },
                isError = uiState.routeNameError != null,
                supportingText = {
                    uiState.routeNameError?.let {
                        Text(text = it, color = MaterialTheme.colorScheme.error)
                    }
                },
                trailingIcon = {
                    if (uiState.routeNameError != null) {
                        Icon(Icons.Default.Error, contentDescription = "Error", tint = MaterialTheme.colorScheme.error)
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // Destí
            OutlinedTextField(
                value = uiState.destinationInput,
                onValueChange = { viewModel.onDestinationChange(it) },
                label = { Text("Destí *") },
                placeholder = { Text("Ex: Pirineus, Girona, Kyoto...") },
                isError = uiState.destinationError != null,
                supportingText = {
                    uiState.destinationError?.let {
                        Text(text = it, color = MaterialTheme.colorScheme.error)
                    }
                },
                trailingIcon = {
                    if (uiState.destinationError != null) {
                        Icon(Icons.Default.Error, contentDescription = "Error", tint = MaterialTheme.colorScheme.error)
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // Nivell de Dificultat (Ús de FilterChip de Material 3 per a opció única)
            Text(
                text = "Nivell de dificultat:",
                style = MaterialTheme.typography.bodyMedium
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                RouteDifficulty.entries.forEach { difficulty ->
                    val selected = uiState.difficultyInput == difficulty
                    FilterChip(
                        selected = selected,
                        onClick = { viewModel.onDifficultyChange(difficulty) },
                        label = { Text(difficulty.label) },
                        leadingIcon = if (selected) {
                            {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                                )
                            }
                        } else null
                    )
                }
            }

            // Pressupost Estimat
            OutlinedTextField(
                value = uiState.estimatedBudgetInput,
                onValueChange = { viewModel.onBudgetChange(it) },
                label = { Text("Pressupost estimat (€) *") },
                placeholder = { Text("Ex: 150.00") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                isError = uiState.budgetError != null,
                supportingText = {
                    uiState.budgetError?.let {
                        Text(text = it, color = MaterialTheme.colorScheme.error)
                    }
                },
                trailingIcon = {
                    if (uiState.budgetError != null) {
                        Icon(Icons.Default.Error, contentDescription = "Error", tint = MaterialTheme.colorScheme.error)
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // Data de la Ruta (DatePicker Dialog)
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = uiState.dateInput,
                    onValueChange = { },
                    readOnly = true,
                    label = { Text("Data de la ruta *") },
                    placeholder = { Text("Selecciona la data") },
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(Icons.Default.CalendarToday, contentDescription = "Seleccionar Data")
                        }
                    },
                    isError = uiState.dateError != null,
                    supportingText = {
                        uiState.dateError?.let {
                            Text(text = it, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showDatePicker = true }
                )
            }

            // Viatge en Solitari (Switch)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Viatge en solitari",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Ruta feta en solitari",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = uiState.isSoloInput,
                    onCheckedChange = { viewModel.onSoloChange(it) }
                )
            }

            // Notes de l'Experiència (Multilínia)
            OutlinedTextField(
                value = uiState.notesInput,
                onValueChange = { viewModel.onNotesChange(it) },
                label = { Text("Notes de l'experiència") },
                placeholder = { Text("Descriu el relat de la jornada, consells o punts d'interès...") },
                minLines = 3,
                maxLines = 6,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Botó Acció Principal: Generar Postal / Crònica
            Button(
                onClick = {
                    val saved = viewModel.saveCurrentRoute(context)
                    if (saved != null) {
                        onNavigateToPostal()
                    }
                },
                enabled = uiState.isFormValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Generar Postal / Crònica")
            }

            // Accés a l'Històric
            OutlinedButton(
                onClick = onNavigateToHistory,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text("Consultar Històric de Rutes (${uiState.savedRoutes.size})")
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    // Diàleg per seleccionar la data utilitzant el selector natiu DatePicker de Material 3
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                            calendar.timeInMillis = millis
                            val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                            format.timeZone = TimeZone.getTimeZone("UTC")
                            viewModel.onDateChange(format.format(calendar.time))
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("D'acord")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel·lar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
