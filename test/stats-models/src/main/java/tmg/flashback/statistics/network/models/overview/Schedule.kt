package tmg.flashback.statistics.network.models.overview

fun Schedule.Companion.model(
    label: String = "label",
    date: String = "2020-01-01",
    time: String = "12:34"
): Schedule = Schedule(
    label = label,
    date = date,
    time = time
)