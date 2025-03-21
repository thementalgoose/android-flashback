package tmg.flashback.widgets.upnext.usecases

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import tmg.flashback.widgets.upnext.contract.usecases.HasUpNextWidgetsUseCase
import tmg.flashback.widgets.upnext.presentation.UpNextWidgetReceiver
import javax.inject.Inject

internal class HasUpNextUpNextWidgetsUseCaseImpl @Inject constructor(
    @ApplicationContext
    private val applicationContext: Context
): HasUpNextWidgetsUseCase {

    private val appWidgetManager: AppWidgetManager
        get() = AppWidgetManager.getInstance(applicationContext)

    override fun hasWidgets(): Boolean {
        val currentIds = appWidgetManager.getAppWidgetIds(ComponentName(applicationContext, UpNextWidgetReceiver::class.java))
        return currentIds.isNotEmpty()
    }
}