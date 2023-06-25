package tmg.flashback.flashbackapi.api.models.drivers

import androidx.annotation.Keep
import kotlinx.serialization.Serializable
import tmg.flashback.flashbackapi.api.models.constructors.Constructor
import tmg.flashback.flashbackapi.api.models.races.RaceData

@Keep
@Serializable
data class DriverHistory(
    val driver: Driver,
    val standings: Map<String, DriverHistoryStanding>
) {
    companion object
}

@Keep
@Serializable
data class DriverHistoryStanding(
    val season: Int,
    val championshipPosition: Int? = null,
    val points: Double,
    val inProgress: Boolean,
    val races: Map<String, DriverHistoryStandingRace>
) {
    companion object
}

@Keep
@Serializable
data class DriverHistoryStandingRace(
    val constructor: Constructor,
    val race: RaceData,
    val sprintQuali: Boolean?,
    val qualified: Int?,
    val gridPos: Int?,
    val finished: Int,
    val status: String,
    val points: Double
) {
    companion object
}