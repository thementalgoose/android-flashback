package tmg.flashback.device.managers

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class WidgetManager @Inject constructor(
    @ApplicationContext
    private val context: Context
) {
    fun updateWidgets(zClass: Class<*>) {
        val manager = AppWidgetManager.getInstance(context)
        val ids = manager.getAppWidgetIds(ComponentName(context, zClass))

        val intent = Intent(context, zClass)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)

        context.sendBroadcast(intent)
    }
}