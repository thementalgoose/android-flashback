package tmg.flashback.widgets.upnext

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import tmg.flashback.widgets.di.WidgetsEntryPoints

internal class UpNextWidgetOpenAll: ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
//        WidgetsEntryPoints.get(context).updateWidgetUseCase().update()
        val intent = WidgetsEntryPoints.get(context).widgetsNavigationComponent().launchApp(context)
        context.startActivity(intent)
    }
}

internal class UpNextWidgetRefreshWidget: ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        WidgetsEntryPoints.get(context).updateWidgetUseCase().update()
    }
}