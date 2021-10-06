package tmg.flashback.firebase.models

data class FSeason(
    val drivers: Map<String, FSeasonOverviewDriver>? = null,
    val constructors: Map<String, FSeasonOverviewConstructor>? = null,
    val race: Map<String, FRound>? = null,
    val standings: FSeasonStatistics? = null
) {
    companion object
}

data class FSeasonStatistics(
    val constructors: Map<String, FSeasonStatisticsPoints>? = null,
    val drivers: Map<String, FSeasonStatisticsPoints>? = null
) {
    companion object
}

data class FSeasonStatisticsPoints(
    val p: Double? = 0.0,
    val pos: Int? = null
) {
    companion object
}

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
) {
    companion object
}

data class FSeasonOverviewConstructor(
    val id: String = "",
    val name: String = "",
    val wikiUrl: String = "",
    val nationality: String = "",
    val nationalityISO: String = "",
    val colour: String = ""
) {
    companion object
}

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
) {
    companion object
}

data class FSeasonOverviewRaceCircuit(
    val id: String = "",
    val name: String = "",
    val wikiUrl: String? = "",
    val locality: String = "",
    val country: String = "",
    val countryISO: String = "",
    val location: FCircuitLocation? = FCircuitLocation()
) {
    companion object
}

data class FSeasonOverviewRaceCircuitLocation(
    val lat: String? = "",
    val lng: String? = ""
) {
    companion object
}

data class FSeasonOverviewRaceQualifying(
    val pos: Int? = null,
    val q1: String? = null,
    val q2: String? = null,
    val q3: String? = null
) {
    companion object
}

data class FSeasonOverviewRaceSprintQualifying(
    val result: Int? = null,
    val grid: Int? = null,
    val resultText: String = "",
    // TODO: Make this private when converter is gone!
    @Deprecated("To be made private when converter is gone")
    val qualified: Int? = null,
    val status: String? = null,
    val points: Double? = null,
    val time: String? = null
) {
    val getSprintQualified: Int?
        get() {
            if (qualified != -1 && qualified != 0) {
                return qualified
            }
            if (grid != null && grid != -1 && grid != 0) {
                return grid
            }
            return null
        }

    companion object
}

data class FSeasonOverviewRaceRace(
    val result: Int? = null,
    val grid: Int? = null,
    val resultText: String? = "",
    // TODO: Make this private when converter is gone!
    @Deprecated("To be made private when converter is gone")
    val qualified: Int? = null,
    val status: String? = null,
    val points: Double? = null,
    val time: String? = null,
    val fastestLap: FSeasonOverviewRaceRaceFastestLap? = null
) {
    val getQualified: Int?
        get() {
            if (qualified != -1 && qualified != 0) {
                return qualified
            }
            if (grid != null && grid != -1 && grid != 0) {
                return grid
            }
            return null
        }

    companion object
}

data class FSeasonOverviewRaceRaceFastestLap(
    val pos: Int = -1,
    val lap: Int = -1,
    val time: String = ""
) {
    companion object
}