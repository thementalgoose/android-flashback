package tmg.flashback.widgets.upnext.presentation

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.state.updateAppWidgetState
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.style.widgets.utils.appWidgetId
import tmg.flashback.widgets.upnext.di.WidgetsEntryPoints
import java.util.UUID

internal class UpNextWidgetOpenAll: ActionCallback {

    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        Log.i("UpNextWidget", "Opening app for $glanceId (${glanceId.appWidgetId})")
        val intent = WidgetsEntryPoints.get(context).widgetsNavigationComponent().getLaunchAppIntent(context)
        context.startActivity(intent)
    }
}
internal class UpNextWidgetOpenEvent: ActionCallback {

    companion object {
        val PARAM_DATA = ActionParameters.Key<OverviewRace>("data")
    }
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val data = parameters[PARAM_DATA] ?: return launchApp(context, glanceId)
        Log.i("UpNextWidget", "Opening app for $glanceId (${glanceId.appWidgetId}) showing ${data.season} / ${data.round}")
        val intent = WidgetsEntryPoints.get(context).widgetsNavigationComponent().getLaunchAppWithSeasonRoundIntent(
            context = context,
            season = data.season,
            round = data.round,
            raceName = data.raceName,
            circuitId = data.circuitId,
            circuitName = data.circuitName,
            country = data.country,
            countryISO = data.countryISO,
            date = data.date
        )
        context.startActivity(intent)
    }

    private fun launchApp(context: Context, glanceId: GlanceId) {
        Log.i("UpNextWidget", "Opening app for $glanceId (${glanceId.appWidgetId})")
        val intent = WidgetsEntryPoints.get(context).widgetsNavigationComponent().getLaunchAppIntent(context)
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