package com.example.catapult.data.repository


import com.example.catapult.data.local.database.dao.QuizResultDao
import com.example.catapult.data.local.database.entities.QuizResultEntity
import javax.inject.Inject

class QuizRepository @Inject constructor(
    private val quizResultDao: QuizResultDao
) {

    suspend fun insertQuizResult(result: QuizResultEntity) {
        quizResultDao.insertResult(result)
    }

    suspend fun getAllResults(): List<QuizResultEntity> {
        return quizResultDao.getAllResults()
    }

    suspend fun clearResults() {
        quizResultDao.clearResults()
    }

    suspend fun getUnpublishedResults(): List<QuizResultEntity> {
        return quizResultDao.getUnpublishedResults()
    }
}
