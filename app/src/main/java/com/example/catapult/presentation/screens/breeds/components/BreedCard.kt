package com.example.catapult.presentation.screens.breeds.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.catapult.data.local.database.entities.BreedEntity
import com.google.accompanist.flowlayout.FlowRow

@Composable
fun BreedCard(
    breed: BreedEntity,
    onClick: () -> Unit
) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
        Text(
            text = breed.name,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "${breed.description}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 3
        )

        Spacer(modifier = Modifier.height(12.dp))

        FlowRow(
            mainAxisSpacing = 8.dp,
            crossAxisSpacing = 8.dp
        ) {
            breed.temperament.split(", ").forEach { temp ->
                AssistChip(
                    onClick = {},
                    label = {
                        Text(
                            text = temp,
                            style = MaterialTheme.typography.labelLarge
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        labelColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                )
            }
        }
    }
}
