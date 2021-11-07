package tmg.flashback.statistics.network.models.races

fun InProgressInfo.Companion.model(
    name: String = "name",
    round: Int = 1
): InProgressInfo = InProgressInfo(
    name = name,
    round = round
)