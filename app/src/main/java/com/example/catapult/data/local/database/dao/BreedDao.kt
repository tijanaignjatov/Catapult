package com.example.catapult.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.catapult.data.local.database.entities.BreedEntity

@Dao
interface BreedDao {

    @Query("SELECT * FROM breeds")
    suspend fun getAllBreeds(): List<BreedEntity>

    @Query("DELETE FROM breeds")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(breeds: List<BreedEntity>)

    @Query("SELECT * FROM breeds")
    fun observeAllBreeds(): kotlinx.coroutines.flow.Flow<List<BreedEntity>>
}
