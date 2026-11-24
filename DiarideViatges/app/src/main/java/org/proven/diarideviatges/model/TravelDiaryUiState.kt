package org.proven.diarideviatges.model

/**
 * Estat de la interfície d'usuari per a l'aplicació Diari de Viatges.
 * Mantenim la immutabilitat utilitzant data class i actualitzacions amb .copy().
 */
data class TravelDiaryUiState(
    // Camps del formulari
    val routeNameInput: String = "",
    val destinationInput: String = "",
    val difficultyInput: RouteDifficulty = RouteDifficulty.FACIL,
    val estimatedBudgetInput: String = "",
    val dateInput: String = "",
    val isSoloInput: Boolean = false,
    val notesInput: String = "",

    // Errors de validació visuals (null si no hi ha error)
    val routeNameError: String? = null,
    val destinationError: String? = null,
    val budgetError: String? = null,
    val dateError: String? = null,

    // Indicador si el formulari és completament vàlid per enviar
    val isFormValid: Boolean = false,

    // Històric de rutes desades i ruta seleccionada per a la crònica/postal
    val savedRoutes: List<RouteEntry> = emptyList(),
    val selectedRoute: RouteEntry? = null,

    // Missatge d'usuari (Snackbars / Toasts)
    val userMessage: String? = null
)
