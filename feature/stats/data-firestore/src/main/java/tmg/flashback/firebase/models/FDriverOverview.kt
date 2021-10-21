package tmg.flashback.firebase.models

data class FDriverOverview(
        val driver: FDriverOverviewDriver = FDriverOverviewDriver(),
        val standings: Map<String, FDriverOverviewStanding>? = null
) {
        companion object
}

data class FDriverOverviewDriver(
        val dob: String = "",
        val driverCode: String? = null,
        val driverNumber: String? = null,
        val firstName: String = "",
        val id: String = "",
        val nationality: String = "",
        val nationalityISO: String? = null,
        val photoUrl: String? = null,
        val surname: String = "",
        val wikiUrl: String = ""
) {
        companion object
}

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
        val p: Double? = null,
        val podiums: Int? = null,
        val races: Int? = null,
        // Season
        val s: Int = -1,
        val wins: Int? = null
) {
        companion object
}

data class FDriverOverviewStandingConstructor(
        val color: String = "#888888",
        val id: String = "",
        val name: String = ""
) {
        companion object
}

data class FDriverOverviewStandingHistory(
        val cId: String = "", // Circuit Id
        val cName: String = "", // Circuit Name
        val cCountry: String = "", // Circuit Country
        val cISO: String? = "", // Nationality iso
        val con: String? = null, // Constructor id
        val date: String = "",
        val f: Int? = null, // Finish
        val fStatus: String? = null,
        val p: Double? = null, // Points
        val q: Int? = null, // Qualifyied
        val r: Int = -1, // Round
        val rName: String? = null
) {
        companion object
}