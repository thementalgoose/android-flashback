package tmg.flashback.statistics.network.models.races

data class Race(
    val data: RaceData,
    val race: Map<String, RaceResult>,
    val qualifying: Map<String, QualifyingResult>
)

data class RaceResult(
    val driverId: String,
    val driverNumber: String,
    val constructorId: String,
    val points: Double,
    val qualified: Int?,
    val gridPos: Int?,
    val finished: Int?,
    val status: String,
    val time: String,
    val fastestLap: FastestLap?
)

data class FastestLap(
    val position: Int,
    val time: String
)

data class QualifyingResult(
    val driverId: String,
    val driverNumber: String?,
    val constructorId: String,
    val points: Double?,
    val qualified: Int?,
    val q1: String?,
    val q2: String?,
    val q3: String?,
    val qSprint: SprintQualifyingResult?
)

data class SprintQualifyingResult(
    val points: Double,
    val qualified: Int,
    val gridPos: Int?,
    val finished: Int,
    val status: String,
    val time: String
)