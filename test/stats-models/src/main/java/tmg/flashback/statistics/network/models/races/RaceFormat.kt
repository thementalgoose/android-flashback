package tmg.flashback.statistics.network.models.races

fun RaceFormat.Companion.model(
    qualifying: String? = "knockout",
    sprint: String? = null,
    race: String? = "race"
): RaceFormat = RaceFormat(
    qualifying = qualifying,
    sprint = sprint,
    race = race
)