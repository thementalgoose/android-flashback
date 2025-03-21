package tmg.flashback.widgets.upnext.repository

import tmg.flashback.prefs.manager.PreferenceManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpNextWidgetRepository @Inject constructor(
    private val preferenceManager: PreferenceManager
) {
    var showBackground: Boolean
        get() = preferenceManager.getBoolean(keyWidgetShowBackground, true)
        set(value) = preferenceManager.save(keyWidgetShowBackground, value)

    var deeplinkToEvent: Boolean
        get() = preferenceManager.getBoolean(keyWidgetDeeplinkToEvent, false)
        set(value) = preferenceManager.save(keyWidgetDeeplinkToEvent, value)

    var showWeather: Boolean
        get() = preferenceManager.getBoolean(keyWidgetShowWeather, false)
        set(value) = preferenceManager.save(keyWidgetShowWeather, value)

    companion object {
        private const val keyWidgetShowBackground: String = "WIDGET_SHOW_BACKGROUND"
        private const val keyWidgetDeeplinkToEvent: String = "WIDGET_DEEPLINK_EVENT"
        private const val keyWidgetShowWeather: String = "WIDGET_SHOW_WEATHER"
    }
}