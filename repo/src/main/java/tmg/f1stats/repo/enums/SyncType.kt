package tmg.f1stats.repo.enums

enum class SyncType(
    val type: String
) {
    TYPE("type"),
    SEASON_OVERVIEW("seasonOverview");

    override fun toString(): String {
        return type
    }
}