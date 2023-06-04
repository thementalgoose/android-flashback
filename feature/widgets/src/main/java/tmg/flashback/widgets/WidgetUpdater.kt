package tmg.flashback.widgets

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import tmg.flashback.widgets.upnext.UpNextWidgetReceiver

internal fun Context.updateAllWidgets() {
    updateWidgets(UpNextWidgetReceiver::class.java)
}

internal fun <T : AppWidgetProvider> Context.updateWidgets(zClass: Class<T>) {

    val manager = AppWidgetManager.getInstance(this)
    val ids = manager.getAppWidgetIds(ComponentName(this, zClass))

    val intent = Intent(this, zClass)
    intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)

    sendBroadcast(intent)
}