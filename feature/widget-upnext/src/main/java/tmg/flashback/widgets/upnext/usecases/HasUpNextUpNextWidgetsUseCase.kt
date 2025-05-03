package tmg.flashback.widgets.upnext.usecases

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import tmg.flashback.widgets.upnext.presentation.UpNextWidgetReceiver
import javax.inject.Inject

internal class HasUpNextUpNextWidgetsUseCase @Inject constructor(
    @ApplicationContext
    private val applicationContext: Context
) {

    private val appWidgetManager: AppWidgetManager
        get() = AppWidgetManager.getInstance(applicationContext)

    fun hasWidgets(): Boolean {
        val currentIds = appWidgetManager.getAppWidgetIds(ComponentName(applicationContext, UpNextWidgetReceiver::class.java))
        return currentIds.isNotEmpty()
    }
}