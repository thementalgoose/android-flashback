package tmg.flashback.formula1.extensions

import tmg.flashback.formula1.model.SeasonConstructorStandingSeason

fun List<SeasonConstructorStandingSeason>.getConstructorInProgressInfo(): Pair<String, Int>? {
    val result = this.firstOrNull { it.inProgress && it.inProgressName != null && it.inProgressRound != null } ?: return null
    return Pair(result.inProgressName!!, result.inProgressRound!!)
}