package org.proven.diarideviatges

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.proven.diarideviatges.model.RouteDifficulty
import org.proven.diarideviatges.model.RouteEntry
import org.proven.diarideviatges.viewmodel.TravelDiaryViewModel

class TravelDiaryViewModelTest {

    private lateinit var viewModel: TravelDiaryViewModel

    @Before
    fun setUp() {
        viewModel = TravelDiaryViewModel()
    }

    @Test
    fun `form is invalid when required fields are empty`() {
        assertFalse(viewModel.uiState.value.isFormValid)

        viewModel.onRouteNameChange("Ruta Pirineus")
        assertFalse(viewModel.uiState.value.isFormValid)

        viewModel.onDestinationChange("Girona")
        assertFalse(viewModel.uiState.value.isFormValid)

        viewModel.onBudgetChange("150.0")
        assertFalse(viewModel.uiState.value.isFormValid)

        viewModel.onDateChange("15/03/2026")
        assertTrue(viewModel.uiState.value.isFormValid)
    }

    @Test
    fun `budget validation rejects invalid and negative inputs`() {
        viewModel.onRouteNameChange("Ruta de prova")
        viewModel.onDestinationChange("Barcelona")
        viewModel.onDateChange("10/05/2026")

        viewModel.onBudgetChange("abc")
        assertFalse(viewModel.uiState.value.isFormValid)
        assertNotNull(viewModel.uiState.value.budgetError)

        viewModel.onBudgetChange("-50")
        assertFalse(viewModel.uiState.value.isFormValid)
        assertNotNull(viewModel.uiState.value.budgetError)

        viewModel.onBudgetChange("200.50")
        assertTrue(viewModel.uiState.value.isFormValid)
        assertNull(viewModel.uiState.value.budgetError)
    }

    @Test
    fun `markdown chronicle formats route entry correctly`() {
        val route = RouteEntry(
            id = "test-123",
            routeName = "Camí de Ronda",
            destination = "Cadaqués",
            difficulty = RouteDifficulty.MODERADA,
            estimatedBudget = 85.50,
            date = "20/06/2026",
            isSolo = true,
            notes = "Vista al mar espectacular."
        )

        val markdown = viewModel.generateMarkdownChronicle(route)

        assertTrue(markdown.contains("Camí de Ronda"))
        assertTrue(markdown.contains("Cadaqués"))
        assertTrue(markdown.contains("20/06/2026"))
        assertTrue(markdown.contains("Moderada"))
        val formattedBudget = "%.2f".format(85.50)
        assertTrue(markdown.contains(formattedBudget))
        assertTrue(markdown.contains("En solitari"))
        assertTrue(markdown.contains("Vista al mar espectacular."))
    }
}
