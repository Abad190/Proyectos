package org.proven.diarideviatges.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.proven.diarideviatges.data.StorageManager
import org.proven.diarideviatges.model.RouteDifficulty
import org.proven.diarideviatges.model.RouteEntry
import org.proven.diarideviatges.model.TravelDiaryUiState

/**
 * ViewModel que gestiona la lògica de presentació i l'estat reactiu
 * seguint l'arquitectura MVVM.
 */
class TravelDiaryViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(TravelDiaryUiState())
    val uiState: StateFlow<TravelDiaryUiState> = _uiState.asStateFlow()

    /**
     - Carrega l'històric de rutes des de l'emmagatzematge intern privat (filesDir).
     */
    fun loadRoutes(context: Context) {
        val storageManager = StorageManager(context)
        val loadedRoutes = storageManager.loadRoutes()
        _uiState.update { currentState ->
            currentState.copy(savedRoutes = loadedRoutes)
        }
    }

    // --- Mètodes d'actualització del formulari ---

    fun onRouteNameChange(newName: String) {
        _uiState.update { currentState ->
            val error = if (newName.isBlank()) "El nom de la ruta és obligatori" else null
            val updatedState = currentState.copy(
                routeNameInput = newName,
                routeNameError = error
            )
            updatedState.copy(isFormValid = validateState(updatedState))
        }
    }

    fun onDestinationChange(newDestination: String) {
        _uiState.update { currentState ->
            val error = if (newDestination.isBlank()) "El destí és obligatori" else null
            val updatedState = currentState.copy(
                destinationInput = newDestination,
                destinationError = error
            )
            updatedState.copy(isFormValid = validateState(updatedState))
        }
    }

    fun onDifficultyChange(newDifficulty: RouteDifficulty) {
        _uiState.update { currentState ->
            currentState.copy(difficultyInput = newDifficulty)
        }
    }

    fun onBudgetChange(newBudget: String) {
        _uiState.update { currentState ->
            val budgetDouble = newBudget.toDoubleOrNull()
            val error = when {
                newBudget.isBlank() -> "El pressupost és obligatori"
                budgetDouble == null -> "Introdueix un número vàlid (ex: 150.0)"
                budgetDouble < 0 -> "El pressupost no pot ser negatiu"
                else -> null
            }
            val updatedState = currentState.copy(
                estimatedBudgetInput = newBudget,
                budgetError = error
            )
            updatedState.copy(isFormValid = validateState(updatedState))
        }
    }

    fun onDateChange(newDate: String) {
        _uiState.update { currentState ->
            val error = if (newDate.isBlank()) "La data és obligatòria" else null
            val updatedState = currentState.copy(
                dateInput = newDate,
                dateError = error
            )
            updatedState.copy(isFormValid = validateState(updatedState))
        }
    }

    fun onSoloChange(isSolo: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(isSoloInput = isSolo)
        }
    }

    fun onNotesChange(newNotes: String) {
        _uiState.update { currentState ->
            currentState.copy(notesInput = newNotes)
        }
    }

    /**
     - Valida si l'estat actual del formulari permet desar la ruta.
     */
    private fun validateState(state: TravelDiaryUiState): Boolean {
        val isNameValid = state.routeNameInput.isNotBlank()
        val isDestValid = state.destinationInput.isNotBlank()
        val isDateValid = state.dateInput.isNotBlank()
        val budgetVal = state.estimatedBudgetInput.toDoubleOrNull()
        val isBudgetValid = budgetVal != null && budgetVal >= 0
        return isNameValid && isDestValid && isDateValid && isBudgetValid
    }

    /**
     - Desa la ruta actual introduïda al formulari a l'emmagatzematge intern (filesDir).
     - Actualitza l'històric i la ruta seleccionada per a la vista Postal.
     */
    fun saveCurrentRoute(context: Context): RouteEntry? {
        val currentState = _uiState.value
        if (!validateState(currentState)) return null

        val budgetDouble = currentState.estimatedBudgetInput.toDoubleOrNull() ?: 0.0

        val newRoute = RouteEntry(
            routeName = currentState.routeNameInput.trim(),
            destination = currentState.destinationInput.trim(),
            difficulty = currentState.difficultyInput,
            estimatedBudget = budgetDouble,
            date = currentState.dateInput.trim(),
            isSolo = currentState.isSoloInput,
            notes = currentState.notesInput.trim()
        )

        val updatedRoutes = currentState.savedRoutes + newRoute
        val storageManager = StorageManager(context)
        storageManager.saveRoutes(updatedRoutes)

        _uiState.update { state ->
            state.copy(
                savedRoutes = updatedRoutes,
                selectedRoute = newRoute,
                // Reiniciem el formulari per a la propera entrada
                routeNameInput = "",
                destinationInput = "",
                difficultyInput = RouteDifficulty.FACIL,
                estimatedBudgetInput = "",
                dateInput = "",
                isSoloInput = false,
                notesInput = "",
                routeNameError = null,
                destinationError = null,
                budgetError = null,
                dateError = null,
                isFormValid = false,
                userMessage = "Ruta guardada correctament a l'històric privat!"
            )
        }

        return newRoute
    }

    /**
     - Selecciona una ruta concreta per visualitzar la seva crònica/postal.
     */
    fun selectRoute(route: RouteEntry) {
        _uiState.update { currentState ->
            currentState.copy(selectedRoute = route)
        }
    }

    /**
     - Elimina una ruta de l'històric i re-guarda el fitxer JSON a filesDir.
     */
    fun deleteRoute(context: Context, routeId: String) {
        val currentState = _uiState.value
        val updatedRoutes = currentState.savedRoutes.filterNot { it.id == routeId }
        val storageManager = StorageManager(context)
        storageManager.saveRoutes(updatedRoutes)

        _uiState.update { state ->
            val selected = if (state.selectedRoute?.id == routeId) null else state.selectedRoute
            state.copy(
                savedRoutes = updatedRoutes,
                selectedRoute = selected,
                userMessage = "Ruta eliminada de l'històric."
            )
        }
    }

    fun clearUserMessage() {
        _uiState.update { currentState ->
            currentState.copy(userMessage = null)
        }
    }

    /**
     - Genera el contingut formatat de la crònica en Markdown (.md).
     - Aquest format és ideal per exportar a fitxers externs o compartir via aplicacions com WhatsApp/Email.
     */
    fun generateMarkdownChronicle(route: RouteEntry): String {
        val soloText = if (route.isSolo) "Sí (En solitari)" else "No (En companyia)"
        return """
            # 🗺️ Crònica de Ruta: ${route.routeName}
            
            **Destí:** ${route.destination}
            **Data:** ${route.date}
            **Dificultat:** ${route.difficulty.label}
            **Pressupost Estimat:** ${"%.2f".format(route.estimatedBudget)} €
            **Viatge en Solitari:** $soloText
            
            ---
            
            ### 📝 Relat de l'Experiència i Notes
            ${if (route.notes.isNotBlank()) route.notes else "_Sense notes de la jornada._"}
            
            ---
            *Generat automàticament des de l'aplicació Diari de Viatges*
        """.trimIndent()
    }
}
