package com.example.catapult.data.local.database.entities


import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "photos")
data class PhotoEntity(
    @PrimaryKey val id: String,
    val url: String,
    val breedId: String
)
