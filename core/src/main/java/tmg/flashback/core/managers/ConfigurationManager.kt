package tmg.flashback.core.managers

interface ConfigurationManager {

    fun setDefaults()

    suspend fun update(andActivate: Boolean = false): Boolean
    suspend fun activate(): Boolean
}