package org.proven.diarideviatges.model

import java.util.UUID

/**
 * Model de dades immutable per a una ruta del diari de viatges.
 */
data class RouteEntry(
    val id: String = UUID.randomUUID().toString(),
    val routeName: String,
    val destination: String,
    val difficulty: RouteDifficulty,
    val estimatedBudget: Double,
    val date: String,
    val isSolo: Boolean,
    val notes: String
)
