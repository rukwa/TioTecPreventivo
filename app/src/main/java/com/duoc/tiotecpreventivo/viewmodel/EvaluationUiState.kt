package com.duoc.tiotecpreventivo.viewmodel

import com.duoc.tiotecpreventivo.data.local.EvaluationEntity

data class EvaluationUiState(
    val deviceName: String = "",
    val deviceNameError: String? = null,
    val selectedCategory: String = "Respaldos",
    val hasBackup: Boolean = false,
    val has2FA: Boolean = false,
    val isUpdated: Boolean = false,
    val isFormValid: Boolean = false,
    val evaluationsList: List<EvaluationEntity> = emptyList()
)