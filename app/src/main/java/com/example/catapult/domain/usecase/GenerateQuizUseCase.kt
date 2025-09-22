package com.example.catapult.domain.usecase

import com.example.catapult.data.local.database.dao.BreedDao
import com.example.catapult.data.local.database.dao.PhotoDao
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.random.Random

class GenerateQuizUseCase @Inject constructor(
    private val breedDao: BreedDao,
    private val photoDao: PhotoDao
) {

    suspend fun generateQuestions(count: Int = 20): List<QuizQuestion> = withContext(Dispatchers.IO) {
        val breeds = breedDao.getAllBreeds().shuffled()
        val usedPhotos = mutableSetOf<String>()

        return@withContext breeds.mapNotNull { breed ->
            val photos = photoDao.getPhotosForBreed(breed.id)

            val randomPhoto = photos
                .filterNot { usedPhotos.contains(it.url) }
                .randomOrNull()?.url ?: ""

            if (randomPhoto.isBlank()) return@mapNotNull null

            usedPhotos.add(randomPhoto)

            val questionType = if (Random.nextBoolean()) QuestionType.BREED else QuestionType.TEMPERAMENT

            when (questionType) {
                QuestionType.BREED -> {
                    val incorrectOptions = breeds.filter { it.id != breed.id }.shuffled().take(3).map { it.name }
                    val options = (incorrectOptions + breed.name).shuffled()

                    QuizQuestion(
                        type = QuestionType.BREED,
                        photoUrl = randomPhoto,
                        correctAnswer = breed.name,
                        options = options
                    )
                }

                QuestionType.TEMPERAMENT -> {
                    val breedTemperaments = breed.temperament.split(", ").toMutableList()
                    val allTemperaments = breeds.flatMap { it.temperament.split(", ") }.toSet()
                    val invalidTemperaments = allTemperaments - breedTemperaments

                    val correctInvalid = invalidTemperaments.randomOrNull() ?: return@mapNotNull null
                    val validOptions = breedTemperaments.shuffled().take(3)
                    val options = (validOptions + correctInvalid).shuffled()

                    QuizQuestion(
                        type = QuestionType.TEMPERAMENT,
                        photoUrl = randomPhoto,
                        correctAnswer = correctInvalid,
                        options = options
                    )
                }
            }
        }.take(count)
    }

}
