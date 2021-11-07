package tmg.flashback.statistics.room.models.overview

fun Schedule.Companion.model(
    season: Int = 2020,
    round: Int = 1,
    label: String = "label",
    date: String = "2020-01-01",
    time: String = "12:34"
): Schedule = Schedule(
    season = season,
    round = round,
    label = label,
    date = date,
    time = time
)