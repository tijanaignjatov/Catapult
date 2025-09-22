package com.example.catapult.domain.usecase

data class QuizQuestion(
    val type: QuestionType,
    val photoUrl: String,
    val correctAnswer: String,
    val options: List<String>
)

enum class QuestionType {
    BREED,
    TEMPERAMENT
}
