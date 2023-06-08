package tmg.flashback.widgets.upnext

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import androidx.glance.appwidget.updateIf
import tmg.flashback.widgets.di.WidgetsEntryPoints
import tmg.flashback.widgets.utils.appWidgetId
import java.util.UUID

internal class UpNextWidgetOpenAll: ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        Log.i("UpNextWidget", "Opening app for $glanceId (${glanceId.appWidgetId})")
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
        Log.i("UpNextWidget", "Refresh widget action for $glanceId (${glanceId.appWidgetId})")
        val intent = Intent(context, UpNextWidgetReceiver::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, intArrayOf(glanceId.appWidgetId))
        context.sendBroadcast(intent)

        updateAppWidgetState(context, glanceId) {
            it[stringPreferencesKey("uuid")] = UUID.randomUUID().toString()
        }
        UpNextWidget().update(context, glanceId)
    }
}