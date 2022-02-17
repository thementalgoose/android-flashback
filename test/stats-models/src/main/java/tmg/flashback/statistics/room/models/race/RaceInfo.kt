package tmg.flashback.statistics.room.models.race

fun RaceInfo.Companion.model(
    season: Int = 2020,
    round: Int = 1,
    name: String = "name",
    date: String = "2020-10-12",
    circuitId: String = "circuitId",
    time: String? = "12:34",
    wikiUrl: String? = "wikiUrl",
    youtube: String? = "youtube",
    formatQualifying: String? = "knockout",
    formatSprint: String? = null,
    formatRace: String? = "race"
): RaceInfo = RaceInfo(
    season = season,
    round = round,
    name = name,
    date = date,
    circuitId = circuitId,
    time = time,
    wikiUrl = wikiUrl,
    youtube = youtube,
    formatQualifying = formatQualifying,
    formatSprint = formatSprint,
    formatRace = formatRace
)