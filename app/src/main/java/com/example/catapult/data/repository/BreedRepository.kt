package com.example.catapult.data.repository

import com.example.catapult.data.local.database.dao.BreedDao
import com.example.catapult.data.local.database.dao.PhotoDao
import com.example.catapult.data.local.database.entities.BreedEntity
import com.example.catapult.data.local.database.entities.PhotoEntity
import com.example.catapult.data.remote.api.CatsApi
import com.example.catapult.utils.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BreedRepository @Inject constructor(
    private val breedDao: BreedDao,
    private val photoDao: PhotoDao,
    private val catsApi: CatsApi
) {

    suspend fun synchronizeBreedsIfNeeded() = withContext(Dispatchers.IO) {
        val localBreeds = breedDao.getAllBreeds()
        if (localBreeds.isEmpty()) {
            refreshBreeds()
        }
    }

    suspend fun refreshBreeds() = withContext(Dispatchers.IO) {
        val remoteBreeds = catsApi.getAllBreeds()
        val breedEntities = remoteBreeds.map { it.toEntity() }

        breedDao.deleteAll()
        breedDao.insertAll(breedEntities)

        photoDao.clearPhotos()

        remoteBreeds.forEach { breedDto ->
            val breedId = breedDto.id ?: return@forEach
            val photos = catsApi.getPhotosByBreed(breedId)
            val photoEntities = photos.map { photoDto ->
                PhotoEntity(
                    id = photoDto.id ?: "",
                    url = photoDto.url ?: "",
                    breedId = breedId
                )
            }
            photoDao.insertPhotos(photoEntities)
        }
    }

    fun observeAllBreeds() = breedDao.observeAllBreeds()

    fun observePhotosForBreed(breedId: String) = photoDao.observePhotosForBreed(breedId)
}
