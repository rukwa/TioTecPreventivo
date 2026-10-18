package com.duoc.tiotecpreventivo.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.duoc.tiotecpreventivo.viewmodel.EvaluationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EvaluationScreen(
    viewModel: EvaluationViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Evaluación Preventiva TíoTec") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("< Volver") }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text("Registro de Dispositivo Ficticio", style = MaterialTheme.typography.titleMedium)

            Spacer(modifier = Modifier.height(8.dp))

            // Campo Validado con Retroalimentación e Ícono de Error
            OutlinedTextField(
                value = state.deviceName,
                onValueChange = { viewModel.onDeviceNameChanged(it) },
                label = { Text("Nombre del Dispositivo") },
                isError = state.deviceNameError != null,
                trailingIcon = {
                    if (state.deviceNameError != null) {
                        Icon(Icons.Default.Error, contentDescription = "Error", tint = MaterialTheme.colorScheme.error)
                    }
                },
                supportingText = {
                    state.deviceNameError?.let {
                        Text(text = it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Switches para Checkbox de Medidas de Seguridad
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = state.hasBackup, onCheckedChange = { viewModel.onBackupToggled(it) })
                Text("¿Posee respaldos de información periódicos?")
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = state.has2FA, onCheckedChange = { viewModel.on2FAToggled(it) })
                Text("¿Tiene habilitada la autenticación en 2 pasos?")
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = state.isUpdated, onCheckedChange = { viewModel.onUpdatedToggled(it) })
                Text("¿El sistema operativo está actualizado?")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { viewModel.saveEvaluation() },
                enabled = state.isFormValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Evaluación en Room")
            }

            Spacer(modifier = Modifier.height(16.dp))
            Divider()
            Spacer(modifier = Modifier.height(8.dp))

            Text("Historial de Evaluaciones Persistidas:", style = MaterialTheme.typography.titleSmall)

            // Animación al renderizar la lista
            AnimatedVisibility(
                visible = state.evaluationsList.isNotEmpty(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                LazyColumn {
                    items(state.evaluationsList) { item ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            ListItem(
                                headlineContent = { Text(item.deviceName) },
                                supportingContent = { Text("Puntaje: ${item.score}/100 - ${item.status}") },
                                leadingContent = { Icon(Icons.Default.Security, contentDescription = null) }
                            )
                        }
                    }
                }
            }
        }
    }
}