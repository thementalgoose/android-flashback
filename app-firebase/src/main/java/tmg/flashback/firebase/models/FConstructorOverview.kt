package tmg.flashback.firebase.models

data class FConstructorOverview(
        val data: FConstructorOverviewData = FConstructorOverviewData(),
        val drivers: Map<String, FConstructorOverviewDrivers>? = null,
        val standings: Map<String, FConstructorOverviewStandings>? = null
)

data class FConstructorOverviewData(
        val colour: String = "",
        val id: String = "",
        val name: String = "",
        val nationality: String = "",
        val nationalityISO: String = "",
        val wikiUrl: String = ""
)

data class FConstructorOverviewDrivers(
        val dob: String? = null,
        val driverCode: String? = null,
        val driverNumber: String? = null,
        val firstName: String = "",
        val id: String = "",
        val nationality: String = "",
        val nationalityISO: String = "",
        val photoUrl: String? = null,
        val surname: String = "",
        val wikiUrl: String = ""
)

data class FConstructorOverviewStandings(
        val championshipStanding: Int? = null,
        val drivers: Map<String, FConstructorOverviewStandingsDriver>? = null,
        val inProgress: Boolean? = null,
        val p: Int? = null, // Points
        val races: Int? = null,
        val s: Int = 0 // Season
)

data class FConstructorOverviewStandingsDriver(
        val bF: Int? = null, // Best Finish
        val bQ: Int? = null, // Best Qualifying
        val p: Int? = null, // Points
        val p1: Int? = null, // P1 finishes
        val p2: Int? = null, // P2 finishes
        val p3: Int? = null, // P3 finishes
        val pF: Int? = null, // Points finishes
        val q1: Int? = null, // Qualified P1
        val q2: Int? = null, // Qualified P2
        val q3: Int? = null, // Qualified P3
        val q10: Int? = null, // Qualified top 10      OPTIONAL
        val races: Int? = null,
        val pos: Int? = null // Championship position
)

