package tmg.flashback.repo.config

import tmg.flashback.repo.models.remoteconfig.UpNextSchedule

interface RemoteConfigRepository {

    /**
     * What year we should default too when opening the app
     */
    val defaultYear: Int

    /**
     * Up next schedule to be shown in the app
     * - Contains name, a date, an optional time and potentially a flag
     */
    val upNext: List<UpNextSchedule>

    /**
     * Banner to be shown at the top of the home screen
     */
    val banner: String?

    /**
     * Enable the RSS feed functionality
     */
    val rss: Boolean

    /**
     * Data provided by tag
     */
    val dataProvidedBy: String?

    /**
     * Enable the search functionality
     */
    val search: Boolean
}