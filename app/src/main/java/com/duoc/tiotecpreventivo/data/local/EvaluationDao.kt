package com.duoc.tiotecpreventivo.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface EvaluationDao {
    @Query("SELECT * FROM evaluations ORDER BY id DESC")
    fun getAllEvaluations(): Flow<List<EvaluationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvaluation(evaluation: EvaluationEntity)

    @Query("DELETE FROM evaluations WHERE id = :id")
    suspend fun deleteEvaluation(id: Int)
}