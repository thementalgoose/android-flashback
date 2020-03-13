package tmg.f1stats.repo_firebase.models

data class FSeasonOverview(
    val circuit: FSeasonOverviewCircuit = FSeasonOverviewCircuit(),
    val constructors: Map<String, FSeasonOverviewConstructor> = emptyMap(),
    val date: String = "",
    val drivers: Map<String, FSeasonOverviewDriver> = emptyMap(),
    val qualifyingResults: FSeasonOverviewQualifying = FSeasonOverviewQualifying(),
    val raceKey: String = "",
    val raceName: String = "",
    val raceResults: Map<String, FSeasonOverviewRaces>? = null,
    val round: Int = -1,
    val season: Int = -1,
    val time: String = "",
    val wikiUrl: String = ""
)  {
    val winner: FSeasonOverviewRaces?
        get() = raceResults?.entries?.minBy { it.value.finishPos ?: Int.MAX_VALUE }!!.value
}

data class FSeasonOverviewCircuit(
    val circuitId: String = "",
    val circuitName: String = "",
    val country: String = "",
    val locality: String = "",
    val locationLat: String = "",
    val locationLng: String = "",
    val wikiUrl: String = ""
)

data class FSeasonOverviewConstructor(
    val constructorId: String = "",
    val name: String = "",
    val nationality: String = "",
    val wikiUrl: String = "",
    val color: String = ""
)

data class FSeasonOverviewDriver(
    val dateOfBirth: String = "",
    val driverCode: String = "",
    val driverId: String = "",
    val driverNumber: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val nationality: String = "",
    val seasonConstructor: String = "",
    val wikiUrl: String = ""
)

data class FSeasonOverviewQualifying(
    val q1: Map<String, FSeasonOverviewQualifyingResult>? = null,
    val q2: Map<String, FSeasonOverviewQualifyingResult>? = null,
    val q3: Map<String, FSeasonOverviewQualifyingResult>? = null
)

data class FSeasonOverviewQualifyingResult(
    val driverId: String = "",
    val position: Int = -1,
    val time: String = ""
)

data class FSeasonOverviewRaces(
    val driverId: String = "",
    val fastestLap: FSeasonOverviewRacesFastestLap = FSeasonOverviewRacesFastestLap(),
    val finishPos: Int? = -1,
    val finishPosText: String = "",
    val gridPos: Int = -1,
    val status: String = "",
    val time: String? = null
)

data class FSeasonOverviewRacesFastestLap(
    val averageSpeed: String = "",
    val lapNumber: String = "",
    val rank: String = "",
    val time: String = ""
)