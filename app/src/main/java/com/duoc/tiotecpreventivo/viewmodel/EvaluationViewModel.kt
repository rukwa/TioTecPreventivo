package com.duoc.tiotecpreventivo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.tiotecpreventivo.data.local.EvaluationEntity
import com.duoc.tiotecpreventivo.data.repository.EvaluationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EvaluationViewModel(private val repository: EvaluationRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(EvaluationUiState())
    val uiState: StateFlow<EvaluationUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.allEvaluations.collect { list ->
                _uiState.update { it.copy(evaluationsList = list) }
            }
        }
    }

    fun onDeviceNameChanged(name: String) {
        val error = if (name.isBlank()) "El nombre del dispositivo es obligatorio"
        else if (name.length < 3) "Debe tener al menos 3 caracteres"
        else null

        _uiState.update {
            it.copy(
                deviceName = name,
                deviceNameError = error,
                isFormValid = error == null && name.isNotBlank()
            )
        }
    }

    fun onCategorySelected(category: String) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun onBackupToggled(checked: Boolean) {
        _uiState.update { it.copy(hasBackup = checked) }
    }

    fun on2FAToggled(checked: Boolean) {
        _uiState.update { it.copy(has2FA = checked) }
    }

    fun onUpdatedToggled(checked: Boolean) {
        _uiState.update { it.copy(isUpdated = checked) }
    }

    fun saveEvaluation() {
        val state = _uiState.value
        if (!state.isFormValid) return

        var score = 0
        if (state.hasBackup) score += 35
        if (state.has2FA) score += 35
        if (state.isUpdated) score += 30

        val newEntity = EvaluationEntity(
            deviceName = state.deviceName,
            category = state.selectedCategory,
            score = score,
            status = if (score >= 70) "Seguro" else "Riesgo Detectado",
            date = "2026-10-24"
        )

        viewModelScope.launch {
            repository.addEvaluation(newEntity)
            // Reset de formulario
            _uiState.update {
                it.copy(deviceName = "", deviceNameError = null, isFormValid = false)
            }
        }
    }
}