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
    }

    fun setShowBackground(appWidgetId: Int, showBackground: Boolean) {
        preferenceManager.save(widgetUpNextShowBackground(appWidgetId), showBackground)
    }

    fun getShowBackground(appWidgetId: Int): Boolean {
        return preferenceManager.getBoolean(widgetUpNextShowBackground(appWidgetId), false)
    }
}