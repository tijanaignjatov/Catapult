package com.example.catapult.data.local.database


import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.catapult.data.local.database.dao.BreedDao
import com.example.catapult.data.local.database.dao.PhotoDao
import com.example.catapult.data.local.database.dao.QuizResultDao
import com.example.catapult.data.local.database.entities.BreedEntity
import com.example.catapult.data.local.database.entities.PhotoEntity
import com.example.catapult.data.local.database.entities.QuizResultEntity

@Database(
    entities = [BreedEntity::class, PhotoEntity::class, QuizResultEntity::class],
    version = 2
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun breedDao(): BreedDao
    abstract fun photoDao(): PhotoDao
    abstract fun quizResultDao(): QuizResultDao
}

