package tmg.flashback.core.controllers

/**
 * All major features in the app
 */
class FeatureController(
        private val configurationController: ConfigurationController
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