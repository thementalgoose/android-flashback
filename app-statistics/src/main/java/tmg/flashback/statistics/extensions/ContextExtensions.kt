package tmg.flashback.statistics.extensions

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import tmg.flashback.statistics.widgets.upnext.UpNextWidgetProvider

fun Context.updateAllWidgets() {
    updateWidgets(UpNextWidgetProvider::class.java)
}

fun <T : AppWidgetProvider> Context.updateWidgets(zClass: Class<T>) {

    val manager = AppWidgetManager.getInstance(this)
    val ids = manager.getAppWidgetIds(ComponentName(this, zClass))

    val intent = Intent(this, zClass)
    intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)

    sendBroadcast(intent)
}