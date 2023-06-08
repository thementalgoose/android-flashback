package tmg.flashback.widgets.repository

import tmg.flashback.prefs.manager.PreferenceManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WidgetRepository @Inject constructor(
    private val preferenceManager: PreferenceManager
) {

    companion object {
        private fun widgetUpNextShowBackground(appWidgetId: Int) = "widget_upnext_show_background_$appWidgetId"
        private fun widgetUpNextShowWeather(appWidgetId: Int) = "widget_upnext_show_weather_$appWidgetId"
    }

    fun setShowBackground(appWidgetId: Int, showBackground: Boolean) {
        preferenceManager.save(widgetUpNextShowBackground(appWidgetId), showBackground)
    }

    fun getShowBackground(appWidgetId: Int): Boolean {
        return preferenceManager.getBoolean(widgetUpNextShowBackground(appWidgetId), false)
    }

    fun setShowWeather(appWidgetId: Int, showWeather: Boolean) {
        preferenceManager.save(widgetUpNextShowWeather(appWidgetId), showWeather)
    }

    fun getShowWeather(appWidgetId: Int): Boolean {
        return preferenceManager.getBoolean(widgetUpNextShowWeather(appWidgetId))
    }
}