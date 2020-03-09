package tmg.f1stats.repo.enums

enum class SyncProgress(
    val type: String
) {
    NEW("new"),
    IN_PROGRESS("inProgress"),
    ERROR("error"),
    FINISHED("finished");

    override fun toString(): String {
        return type
    }
}