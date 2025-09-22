package com.example.catapult.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.catapult.data.local.database.entities.PhotoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhotos(photos: List<PhotoEntity>)

    @Query("SELECT * FROM photos WHERE breedId = :breedId")
    suspend fun getPhotosForBreed(breedId: String): List<PhotoEntity>

    @Query("DELETE FROM photos")
    suspend fun clearPhotos()

    @Query("SELECT * FROM photos WHERE breedId = :breedId")
    fun observePhotosForBreed(breedId: String): Flow<List<PhotoEntity>>
}
