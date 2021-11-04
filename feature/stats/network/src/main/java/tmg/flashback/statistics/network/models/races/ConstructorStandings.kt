package tmg.flashback.statistics.network.models.races

import kotlinx.serialization.Serializable

@Serializable
data class ConstructorStandings(
    val constructorId: String,
    val points: Double,
    val inProgress: Boolean? = null,
    val races: Int,
    val position: Int? = null,
    val drivers: Map<String, Double>
) {
    companion object
}