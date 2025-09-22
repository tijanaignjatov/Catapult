package com.example.catapult.utils

import com.example.catapult.data.local.database.entities.BreedEntity
import com.example.catapult.data.local.database.entities.WeightEntity
import com.example.catapult.data.remote.dto.BreedDto


fun BreedDto.toEntity(): BreedEntity {
    return BreedEntity(
        id = id,
        name = name,
        altNames = altNames,
        description = description,
        temperament = temperament,
        origin = origin,
        lifeSpan = lifeSpan,
        weight = WeightEntity(
            imperial = weight.imperial,
            metric = weight.metric
        ),
        adaptability = adaptability,
        affection_level = affectionLevel,
        child_friendly = childFriendly,
        dog_friendly = dogFriendly,
        energy_level = energyLevel,
        grooming = grooming,
        health_issues = healthIssues,
        intelligence = intelligence,
        shedding_level = sheddingLevel,
        social_needs = socialNeeds,
        stranger_friendly = strangerFriendly,
        vocalisation = vocalisation,
        wikipedia_url = wikipediaUrl,
        rare = rare
    )
}

