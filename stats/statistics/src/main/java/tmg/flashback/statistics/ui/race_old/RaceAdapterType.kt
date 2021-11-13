package tmg.flashback.statistics.ui.race_old

enum class RaceAdapterType {
    RACE,
    QUALIFYING_SPRINT,
    QUALIFYING_POS_1,
    QUALIFYING_POS_2,
    QUALIFYING_POS,
    CONSTRUCTOR_STANDINGS
}

fun RaceAdapterType.isQualifying(): Boolean {
    return this == RaceAdapterType.QUALIFYING_POS || this == RaceAdapterType.QUALIFYING_POS_1 || this == RaceAdapterType.QUALIFYING_POS_2
}