package tmg.flashback.firebase.models

data class FSeason(
        val drivers: Map<String, FSeasonOverviewDriver>? = null,
        val constructors: Map<String, FSeasonOverviewConstructor>? = null,
        val race: Map<String, FRound>? = null,
        val standings: FSeasonStatistics? = null
)

data class FSeasonStatistics(
        val constructors: Map<String, FSeasonStatisticsPoints>? = null,
        val drivers: Map<String, FSeasonStatisticsPoints>? = null
)

data class FSeasonStatisticsPoints(
        val p: Double? = 0.0,
        val pos: Int? = null
)

data class FSeasonOverviewDriver(
        val id: String = "",
        val firstName: String = "",
        val lastName: String = "",
        val code: String? = null,
        val number: Int? = null,
        val wikiUrl: String = "",
        val photoUrl: String? = null,
        val dob: String = "",
        val nationality: String = "",
        val nationalityISO: String = "",
        val constructorId: String = ""
)

data class FSeasonOverviewConstructor(
        val id: String = "",
        val name: String = "",
        val wikiUrl: String = "",
        val nationality: String = "",
        val nationalityISO: String = "",
        val colour: String = ""
)

data class FSeasonOverviewPoints(
        val position: Int = -1,
        val points: Double = -1.0
)

data class FSeasonOverviewPointsConfig(
        val fastestLap: Int? = null,
        val doubleForLastRace: Boolean? = null
)

data class FRound(
        val season: Int = -1,
        val round: Int = -1,
        val name: String = "",
        val date: String = "",
        val time: String? = "",
        val wiki: String? = null,
        val driverCon: Map<String, String>? = null,
        val circuit: FSeasonOverviewRaceCircuit = FSeasonOverviewRaceCircuit(),
        val qualifying: Map<String, FSeasonOverviewRaceQualifying>? = null,
        val sprintQualifying: Map<String, FSeasonOverviewRaceSprintQualifying>? = null,
        val race: Map<String, FSeasonOverviewRaceRace>? = null
)

data class FSeasonOverviewRaceCircuit(
        val id: String = "",
        val name: String = "",
        val wikiUrl: String = "",
        val locality: String = "",
        val country: String = "",
        val countryISO: String = "",
        val location: FSeasonOverviewRaceCircuitLocation = FSeasonOverviewRaceCircuitLocation()
)

data class FSeasonOverviewRaceCircuitLocation(
        val lat: String? = "",
        val lng: String? = ""
)

data class FSeasonOverviewRaceQualifying(
        val pos: Int? = null,
        val q1: String? = null,
        val q2: String? = null,
        val q3: String? = null
)

data class FSeasonOverviewRaceSprintQualifying(
        val result: Int? = null,
        val grid: Int? = null,
        val resultText: String = "",
        val qualified: Int? = null,
        val status: String? = null,
        val points: Double? = null,
        val time: String? = null
)

data class FSeasonOverviewRaceRace(
        val result: Int? = null,
        val grid: Int? = null,
        val resultText: String = "",
        val qualified: Int? = null,
        val status: String? = null,
        val points: Double? = null,
        val time: String? = null,
        val fastestLap: FSeasonOverviewRaceRaceFastestLap? = null
)

data class FSeasonOverviewRaceRaceFastestLap(
        val pos: Int = -1,
        val lap: Int = -1,
        val time: String = ""
)