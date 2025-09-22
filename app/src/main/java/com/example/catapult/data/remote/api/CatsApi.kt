package com.example.catapult.data.remote.api

import com.example.catapult.data.remote.dto.BreedDto
import com.example.catapult.data.remote.dto.PhotoDto
import retrofit2.http.GET
import retrofit2.http.Query

interface CatsApi {

    @GET("v1/breeds")
    suspend fun getAllBreeds(): List<BreedDto>

    @GET("v1/images/search")
    suspend fun getPhotosByBreed(
        @Query("breed_ids") breedId: String,
        @Query("limit") limit: Int = 10,
        @Query("format") format: String = "json"
    ): List<PhotoDto>
}
