package tmg.flashback.formula1.model

data class SeasonDriverStandings(
    val driver: Driver,
    val standings: List<SeasonDriverStandingSeason>
)