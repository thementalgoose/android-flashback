package tmg.flashback.stats.ui.weekend.sprint

import tmg.flashback.formula1.model.RaceSprintResult

sealed class SprintModel(
    val id: String
) {

    object NotAvailableYet: SprintModel("not_available_yet")

    object NotAvailable: SprintModel("not_available")

    object Loading: SprintModel("loading")

    data class Result(
        val result: RaceSprintResult
    ): SprintModel(
        id = result.driver.driver.id
    )
}