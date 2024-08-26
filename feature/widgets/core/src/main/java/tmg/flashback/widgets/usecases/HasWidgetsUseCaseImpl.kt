package tmg.flashback.widgets.usecases

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import tmg.flashback.widgets.contract.usecases.HasWidgetsUseCase
import tmg.flashback.widgets.presentation.upnext.UpNextWidgetReceiver
import javax.inject.Inject

internal class HasWidgetsUseCaseImpl @Inject constructor(
    @ApplicationContext
    private val applicationContext: Context
): HasWidgetsUseCase {

    private val appWidgetManager: AppWidgetManager
        get() = AppWidgetManager.getInstance(applicationContext)

    override fun hasWidgets(): Boolean {
        val currentIds = appWidgetManager.getAppWidgetIds(ComponentName(applicationContext, UpNextWidgetReceiver::class.java))
        return currentIds.isNotEmpty()
    }
}