package tmg.flashback.statistics.network.models.races

import kotlinx.serialization.Serializable

@Serializable
data class Race(
    val data: RaceData,
    val race: Map<String, RaceResult>,
    val qualifying: Map<String, QualifyingResult>
) {
    companion object
}