package tmg.flashback.statistics.network.models.overview

fun Event.Companion.model(
    label: String = "label",
    type: String = "testing",
    date: String = "2020-10-12"
): Event = Event(
    label = label,
    type = type,
    date = date
)