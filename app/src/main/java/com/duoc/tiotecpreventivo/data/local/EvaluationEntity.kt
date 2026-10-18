package com.duoc.tiotecpreventivo.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "evaluations")
data class EvaluationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val deviceName: String,
    val category: String, // ej. Respaldos, Autenticación, Actualizaciones
    val score: Int,
    val status: String,   // ej. "Completado", "Pendiente"
    val date: String
)