package tmg.flashback.controllers

import tmg.configuration.controllers.ConfigController

/**
 * All major features in the app
 */
class FeatureController(
        private val configurationController: ConfigController
) {
    /**
     * Is the RSS feature enabled
     */
    val rssEnabled: Boolean
        get() = configurationController.rss

    /**
     * Is the calendar dashboard feature enabled
     */
    val calendarDashboardEnabled: Boolean
        get() = configurationController.dashboardCalendar

}