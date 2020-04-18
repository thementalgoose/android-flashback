package tmg.flashback.repo.enums

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