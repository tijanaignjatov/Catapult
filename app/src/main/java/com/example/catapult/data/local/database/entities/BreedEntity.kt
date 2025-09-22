package com.example.catapult.data.local.database.entities


import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "breeds")
data class BreedEntity(
    @PrimaryKey val id: String,
    val name: String,
    @ColumnInfo(name = "alt_names")
    val altNames: String?,            // dodato
    val description: String,
    val temperament: String,
    val origin: String,
    @ColumnInfo(name = "life_span")
    val lifeSpan: String,
    @Embedded val weight: WeightEntity,  // dodato, nested entity
    val adaptability: Int,
    val affection_level: Int,
    val child_friendly: Int,
    val dog_friendly: Int,             // dodato
    val energy_level: Int,
    val grooming: Int,
    val health_issues: Int,
    val intelligence: Int,
    val shedding_level: Int,           // dodato
    val social_needs: Int,
    val stranger_friendly: Int,
    val vocalisation: Int,             // dodato
    val wikipedia_url: String?,
    val rare: Int
)
