package tmg.f1stats.repo.models

data class Season(
    val season: Int,
    val drivers: List<Driver>,
    val constructors: List<Constructor>,
    val rounds: List<Round>
) {
    val circuits: List<Circuit>
        get() = rounds.map { it.circuit }

    val firstRound: Round?
        get() = rounds.minBy { it.round }

    val lastRound: Round?
        get() = rounds.maxBy { it.round }

    fun getRound(round: Int): Round? {
        return rounds.firstOrNull { it.round == round }
    }
}