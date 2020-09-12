package tmg.flashback.firebase.models

data class FDriverOverview(
        val driver: FDriverOverviewDriver = FDriverOverviewDriver(),
        val standings: Map<String, FDriverOverviewStanding>? = null
)

data class FDriverOverviewDriver(
        val dob: String = "",
        val driverCode: String? = null,
        val driverNumber: String? = null,
        val firstName: String = "",
        val id: String = "",
        val nationality: String = "",
        val nationalityISO: String? = null,
        val photoUrl: String = "",
        val surname: String = "",
        val wikiUrl: String = ""
)

data class FDriverOverviewStanding(
        val bestFinish: Int? = null,
        val bestFinishQuantity: Int? = null,
        val bestQualifying: Int? = null,
        val bestQualifyingQuantity: Int? = null,
        val championshipStanding: Int? = null,
        val constructor: List<FDriverOverviewStandingConstructor>? = null,
        val history: Map<String, FDriverOverviewStandingHistory>? = null,
        val inProgress: Boolean? = null,
        // Points
        val p: Int? = null,
        val podiums: Int? = null,
        val races: Int? = null,
        // Season
        val s: Int = -1,
        val wins: Int? = null
)

data class FDriverOverviewStandingConstructor(
        val color: String = "#888888",
        val id: String = "",
        val name: String = ""
)

data class FDriverOverviewStandingHistory(
        val cId: String = "",
        val cName: String = "",
        val cCountry: String = "",
        // Nationality iso
        val cISO: String? = "",
        val date: String = "",
        // Finish
        val f: Int? = null,
        val fStatus: String? = null,
        // Points
        val p: Int? = null,
        // Qualifyied
        val q: Int? = null,
        // Round
        val r: Int = -1,
        val rName: String? = null
)