package tmg.f1stats.repo_firebase.models

data class FSeason(
    val drivers: Map<String, FSeasonOverviewDriver>?,
    val constructors: Map<String, FSeasonOverviewConstructor>?,
    val points: List<FSeasonOverviewPoints>?,
    val pointsConfig: FSeasonOverviewPointsConfig?,
    val race: Map<String, FRound>?
)

data class FSeasonOverviewDriver(
    val id: String,
    val firstName: String,
    val lastName: String,
    val code: String?,
    val number: Int?,
    val wikiUrl: String,
    val dob: String,
    val nationality: String,
    val nationalityISO: String,
    val constructorId: String
)

data class FSeasonOverviewConstructor(
    val id: String,
    val name: String,
    val wikiUrl: String,
    val nationality: String,
    val nationalityISO: String,
    val colour: String
)

data class FSeasonOverviewPoints(
    val position: Int,
    val points: Int
)

data class FSeasonOverviewPointsConfig(
    val fastestLap: Int?,
    val doubleForLastRace: Boolean?
)

data class FRound(
    val season: Int,
    val round: Int,
    val name: String,
    val date: String,
    val time: String,
    val circuit: FSeasonOverviewRaceCircuit,
    val qualifying: Map<String, FSeasonOverviewRaceQualifying>?,
    val race: Map<String, FSeasonOverviewRaceRace>?
)

data class FSeasonOverviewRaceCircuit(
    val id: String,
    val name: String,
    val wikiUrl: String,
    val locality: String,
    val country: String,
    val countryISO: String,
    val location: FSeasonOverviewRaceCircuitLocation
)

data class FSeasonOverviewRaceCircuitLocation(
    val lat: Double,
    val lng: Double
)

data class FSeasonOverviewRaceQualifying(
    val pos: Int?,
    val q1: String?,
    val q2: String?,
    val q3: String?
)

data class FSeasonOverviewRaceRace(
    val result: Int?,
    val grid: Int?,
    val resultText: String,
    val status: String?,
    val points: Int?,
    val time: String?,
    val fastestLap: FSeasonOverviewRaceRaceFastestLap?
)

data class FSeasonOverviewRaceRaceFastestLap(
    val pos: Int,
    val lap: Int,
    val time: String
)