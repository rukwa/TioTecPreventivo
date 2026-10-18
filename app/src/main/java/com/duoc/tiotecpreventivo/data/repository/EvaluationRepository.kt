package com.duoc.tiotecpreventivo.data.repository

import com.duoc.tiotecpreventivo.data.local.EvaluationDao
import com.duoc.tiotecpreventivo.data.local.EvaluationEntity
import kotlinx.coroutines.flow.Flow

class EvaluationRepository(private val dao: EvaluationDao) {
    val allEvaluations: Flow<List<EvaluationEntity>> = dao.getAllEvaluations()

    suspend fun addEvaluation(evaluation: EvaluationEntity) {
        dao.insertEvaluation(evaluation)
    }

    suspend fun deleteEvaluation(id: Int) {
        dao.deleteEvaluation(id)
    }
}