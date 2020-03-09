package tmg.f1stats.repo

data class Optional<T>(val value: T?) {
    val isNull: Boolean
        get() = value != null
}