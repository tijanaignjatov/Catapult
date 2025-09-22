package com.example.catapult.data.local.database.dao


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.catapult.data.local.database.entities.QuizResultEntity

@Dao
interface QuizResultDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResult(result: QuizResultEntity)

    @Query("SELECT * FROM quiz_results ORDER BY createdAt DESC")
    suspend fun getAllResults(): List<QuizResultEntity>

    @Query("DELETE FROM quiz_results")
    suspend fun clearResults()

    @Query("SELECT * FROM quiz_results WHERE published = 0")
    suspend fun getUnpublishedResults(): List<QuizResultEntity>
}
