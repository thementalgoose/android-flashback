package tmg.flashback.statistics.network.models.races

import kotlinx.serialization.Serializable

@Serializable
data class Race(
    val data: RaceData,
    val race: Map<String, RaceResult>,
    val qualifying: Map<String, QualifyingResult>
)

@Serializable
data class RaceResult(
    val driverId: String,
    val driverNumber: String,
    val constructorId: String,
    val points: Double,
    val qualified: Int? = null,
    val gridPos: Int? = null,
    val finished: Int,
    val status: String,
    val time: String? = null,
    val fastestLap: FastestLap?
)

@Serializable
data class FastestLap(
    val position: Int,
    val time: String
)

@Serializable
data class QualifyingResult(
    val driverId: String,
    val driverNumber: String?,
    val constructorId: String,
    val points: Double? = null,
    val qualified: Int?,
    val q1: String? = null,
    val q2: String? = null,
    val q3: String? = null,
    val qSprint: SprintQualifyingResult? = null
)

@Serializable
data class SprintQualifyingResult(
    val points: Double,
    val qualified: Int,
    val gridPos: Int?,
    val finished: Int,
    val status: String,
    val time: String
)