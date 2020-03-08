package tmg.f1stats.repo_firebase.models

data class FSeasonOverview(
    val circuit: FSeasonOverviewCircuit,
    val constructors: Map<String, FSeasonOverviewConstructor>,
    val date: String,
    val drivers: Map<String, FSeasonOverviewDriver>,
    val qualifyingResult: FSeasonOverviewQualifying,
    val raceKey: String,
    val raceName: String,
    val raceResults: Map<String, FSeasonOverviewRaces>?,
    val round: Int,
    val season: Int,
    val time: String,
    val wikiUrl: String
)  {
    val winner: FSeasonOverviewRaces?
        get() = raceResults?.entries?.minBy { it.value.finishPos }!!.value
}

data class FSeasonOverviewCircuit(
    val circuitId: String,
    val circuitName: String,
    val country: String,
    val locality: String,
    val locationLat: String,
    val locationLng: String,
    val wikiUrl: String
)

data class FSeasonOverviewConstructor(
    val constructorId: String,
    val name: String,
    val nationality: String,
    val wikiUrl: String
)

data class FSeasonOverviewDriver(
    val dateOfBirth: String,
    val driverCode: String,
    val driverId: String,
    val driverNumber: String,
    val firstName: String,
    val lastName: String,
    val nationality: String,
    val seasonConstructor: String,
    val wikiUrl: String
)

data class FSeasonOverviewQualifying(
    val q1: Map<String, FSeasonOverviewQualifyingResult>?,
    val q2: Map<String, FSeasonOverviewQualifyingResult>?,
    val q3: Map<String, FSeasonOverviewQualifyingResult>?
)

data class FSeasonOverviewQualifyingResult(
    val driverId: String,
    val position: String,
    val time: String
)

data class FSeasonOverviewRaces(
    val driverId: String,
    val fastestLap: FSeasonOverviewRacesFastestLap,
    val finishPos: Int,
    val finishPosText: String,
    val gridPos: Int,
    val status: String,
    val time: String?
)

data class FSeasonOverviewRacesFastestLap(
    val averageSpeed: String,
    val lapNumber: String,
    val rank: String,
    val time: String
)