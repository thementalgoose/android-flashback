package tmg.f1stats.repo.enums

enum class RaceStatus(
    val statusCode: String
) {
    FINISHED("Finished"),
    RETIRED("Retired");

    companion object {
        fun fromStatus(status: String): RaceStatus {
            return values().firstOrNull { it.statusCode == status } ?: RETIRED
        }
    }
}