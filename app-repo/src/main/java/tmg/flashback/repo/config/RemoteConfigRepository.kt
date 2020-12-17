package tmg.flashback.repo.config

interface RemoteConfigRepository {

    suspend fun update(andActivate: Boolean = false): Boolean
    suspend fun activate(): Boolean

    /**
     * What year we should default too when opening the app
     */
    val defaultYear: Int

    /**
     * Banner to be shown at the top of the home screen
     */
    val banner: String?

    /**
     * Enable the RSS feed functionality
     */
    val rss: Boolean
}