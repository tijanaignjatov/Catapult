package com.example.catapult.data.local.database.entities


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_results")
data class QuizResultEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val category: Int,
    val nickname: String,
    val result: Float,
    val createdAt: Long,
    val published: Boolean
)
