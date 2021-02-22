package tmg.flashback.core.managers

/**
 * Wrapper around the Firebase Remote Config management functionality
 * Abstracted for testing
 */
interface ConfigurationManager {

    fun setDefaults()

    suspend fun update(andActivate: Boolean = false): Boolean
    suspend fun activate(): Boolean
}