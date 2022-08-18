package tmg.flashback.managers.widgets

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import tmg.flashback.widgets.providers.UpNextWidgetProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppWidgetManager @Inject constructor(
    @ApplicationContext
    private val applicationContext: Context
): WidgetManager {

    private val appWidgetManager: AppWidgetManager get() = AppWidgetManager.getInstance(applicationContext)

    @Suppress("EXPERIMENTAL_API_USAGE")
    override val hasWidgets: Boolean
        get() {
            val currentIds = appWidgetManager.getAppWidgetIds(ComponentName(applicationContext, UpNextWidgetProvider::class.java))
            return currentIds.isNotEmpty()
        }
}