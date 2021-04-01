package tmg.flashback.statistics.widgets.upnext

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.VectorDrawable
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.statistics.enums.TrackLayout
import tmg.flashback.statistics.controllers.UpNextController
import tmg.flashback.core.controllers.CrashController
import tmg.flashback.core.managers.BuildConfigManager
import tmg.flashback.core.managers.NavigationManager
import tmg.flashback.core.model.UpNextSchedule
import tmg.flashback.data.utils.daysBetween
import tmg.flashback.statistics.R
import tmg.flashback.statistics.ui.util.getFlagResourceAlpha3
import tmg.utilities.extensions.fromHtml
import tmg.utilities.extensions.toEnum
import java.lang.Exception

@KoinApiExtension
class UpNextWidgetProvider : AppWidgetProvider(), KoinComponent {

    private val crashController: CrashController by inject()
    private val upNextController: UpNextController by inject()
    private val buildConfigManager: BuildConfigManager by inject()
    private val navigationManager: NavigationManager by inject()

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
    }

    override fun onUpdate(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetIds: IntArray?) {
        Log.i("Flashback", "Updating up next widgets... ${appWidgetIds.toString()}")

        val nextEvent: UpNextSchedule? = upNextController.getNextEvent()

        if (context == null) {
            return
        }
        if (nextEvent == null) {
            appWidgetIds?.forEach { widgetId ->
                val remoteView = RemoteViews(buildConfigManager.applicationId, R.layout.widget_up_next)
                remoteView.error(appWidgetManager, widgetId, context)
            }
            return
        }

        var tintedIcon: Bitmap? = null
        try {
            val icon: Int? = nextEvent.circuitId?.toEnum<TrackLayout> { it.circuitId }?.icon
            if (icon != null) {
                tintedIcon = tintDrawable(context, icon)
            }
        }
        catch (e: Exception) {
            Log.i("Flashback", "Failed to tint icon ${e.message}")
            e.printStackTrace()
            crashController.logError(e, "Widget Up Next provider couldn't tint bitmap")
        }

        appWidgetIds?.forEach { widgetId ->

            val remoteView = RemoteViews(buildConfigManager.applicationId, R.layout.widget_up_next)

            try {
                remoteView.setTextViewText(R.id.name, nextEvent.title)
                remoteView.setTextViewText(R.id.subtitle, nextEvent.subtitle ?: "")

                if (tintedIcon != null) {
                    remoteView.setImageViewBitmap(R.id.circuit, tintedIcon)
                }
                else {
                    remoteView.setImageViewResource(R.id.circuit, R.drawable.widget_circuit_unknown)
                }

                if (nextEvent.flag != null) {
                    remoteView.setViewVisibility(R.id.flag, View.VISIBLE)
                    remoteView.setImageViewResource(R.id.flag, context.getFlagResourceAlpha3(nextEvent.flag ?: ""))
                }
                else {
                    remoteView.setViewVisibility(R.id.flag, View.GONE)

                }

//                when (val days = daysBetween(LocalDate.now(), nextEvent.timestamp.originalDate)) {
//                    0 -> {
//                        nextEvent.timestamp.ifDate {
//                            remoteView.setTextViewText(R.id.days, context.getString(R.string.dashboard_up_next_date_today))
//                            remoteView.setTextViewText(R.id.daystogo, "")
//                            remoteView.setViewVisibility(R.id.daystogo, View.INVISIBLE)
//                        }
//                        nextEvent.timestamp.ifDateAndTime { utc, local ->
//                            remoteView.setTextViewText(R.id.days, context.getString(R.string.dashboard_up_next_date_starts_at_today, local.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))))
//                            if (local.toLocalTime() == utc.toLocalTime()) {
//                                remoteView.setTextViewText(R.id.daystogo, "")
//                                remoteView.setViewVisibility(R.id.daystogo, View.INVISIBLE)
//                            } else {
//                                remoteView.setTextViewText(R.id.daystogo, context.getString(R.string.dashboard_up_next_date_localtime).fromHtml())
//                                remoteView.setViewVisibility(R.id.daystogo, View.VISIBLE)
//                            }
//                        }
//                    }
//                    else -> {
//                        remoteView.setTextViewText(R.id.days, days.toString())
//                        remoteView.setViewVisibility(R.id.daystogo, View.VISIBLE)
//                        remoteView.setTextViewText(R.id.daystogo, context.resources.getQuantityText(R.plurals.dashboard_up_next_suffix_days, days))
//                    }
//                }

                remoteView.setOnClickPendingIntent(R.id.container, getRefreshWidgetPendingIntent(context, widgetId, appWidgetIds))
                appWidgetManager?.updateAppWidget(widgetId, remoteView)
            } catch (e: RuntimeException) {
                crashController.logError(e, "Widget Up Next provider couldn't be set up")
                remoteView.error(appWidgetManager, widgetId, context)
            }
        }
    }

    /**
     * Set the error state for the widget
     */
    private fun RemoteViews.error(appWidgetManager: AppWidgetManager?, widgetId: Int, context: Context) {
        this.setTextViewText(R.id.name, context.getString(R.string.widget_up_next_nothing_title))
        this.setTextViewText(R.id.subtitle, context.getString(R.string.widget_up_next_nothing_subtitle))
        this.setImageViewResource(R.id.circuit, R.drawable.widget_circuit_unknown)
        this.setViewVisibility(R.id.flag, View.GONE)
        this.setTextViewText(R.id.days, "")
        this.setTextViewText(R.id.daystogo, "")

        this.setOnClickPendingIntent(R.id.container, getOpenAppPendingIntent(context))

        appWidgetManager?.updateAppWidget(widgetId, this)
    }

    private fun tintDrawable(context: Context, @DrawableRes drawableId: Int): Bitmap {
        val source = (ResourcesCompat.getDrawable(context.resources, drawableId, null) as VectorDrawable).toBitmap()
        return changeBitmapColour(source, Color.WHITE)
    }

    private fun changeBitmapColour(source: Bitmap, @ColorInt colour: Int): Bitmap {
        val resultBitmap = Bitmap.createBitmap(source, 0, 0, source.width - 1, source.height - 1)
        val paint = Paint()
        val colourFilter: ColorFilter = PorterDuffColorFilter(colour, PorterDuff.Mode.SRC_IN)
        paint.colorFilter = colourFilter

        val canvas = Canvas(resultBitmap)
        canvas.drawBitmap(resultBitmap, 0f, 0f, paint)

        return resultBitmap
    }

    private fun getOpenAppPendingIntent(context: Context): PendingIntent {
        val intent = navigationManager.getAppStartupIntent(context)
        return PendingIntent.getActivity(context, 0, intent, 0)
    }

    private fun getRefreshWidgetPendingIntent(context: Context, widgetId: Int, appWidgetIds: IntArray?): PendingIntent {
        val intent = Intent(context, UpNextWidgetProvider::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds ?: IntArray(0))
        return PendingIntent.getBroadcast(context, widgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }
}