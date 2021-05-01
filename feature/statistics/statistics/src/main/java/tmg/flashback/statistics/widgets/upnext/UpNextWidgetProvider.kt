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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import tmg.configuration.controllers.ConfigController
import tmg.configuration.repository.models.UpNextSchedule
import tmg.flashback.statistics.enums.TrackLayout
import tmg.flashback.statistics.controllers.UpNextController
import tmg.crash_reporting.controllers.CrashController
import tmg.flashback.device.managers.BuildConfigManager
import tmg.flashback.core.managers.NavigationManager
import tmg.flashback.data.repositories.AppRepository
import tmg.flashback.data.utils.daysBetween
import tmg.flashback.statistics.R
import tmg.flashback.statistics.ui.util.getFlagResourceAlpha3
import tmg.utilities.extensions.toEnum
import java.lang.Exception

@KoinApiExtension
class UpNextWidgetProvider : AppWidgetProvider(), KoinComponent {

    private val crashController: CrashController by inject()
    private val upNextController: UpNextController by inject()
    private val buildConfigManager: BuildConfigManager by inject()
    private val navigationManager: NavigationManager by inject()

    private val configController: ConfigController by inject()
    private val appRepository: AppRepository by inject()

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
    }

    override fun onUpdate(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetIds: IntArray?) {
        Log.i("Flashback", "Updating up next widgets ${appWidgetIds.contentToString()}")

        // Fire and forget remote config sync
        GlobalScope.launch {
            configController.applyPending()
            configController.fetch()
        }

        // Pre app checks
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

        // Given we have valid app widget ids and an up next item
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

        // Update all the widget ids
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

                val eventsToday = nextEvent.values
                    .filter { it.timestamp.originalDate == LocalDate.now() }
                    .sortedBy { it.timestamp.string() }

                val eventsInFuture = nextEvent.values
                    .filter { it.timestamp.originalDate > LocalDate.now() }
                    .sortedBy { it.timestamp.string() }

                if (eventsToday.isNotEmpty()) {

                    remoteView.setTextViewText(R.id.days, context.getString(R.string.dashboard_up_next_date_today))
                    remoteView.setTextViewText(R.id.daystogo, eventsToday.joinToString(separator = ", ") {
                        var result: String? = null
                        it.timestamp.ifDateAndTime { utc, local ->
                            result = local.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))
                        }
                        if (result == null) {
                            return@joinToString it.label
                        }
                        else {
                            return@joinToString "${it.label} ($result)"
                        }
                    })
                    remoteView.setViewVisibility(R.id.daystogo, View.VISIBLE)
                }
                if (eventsToday.isEmpty() && eventsInFuture.isNotEmpty()) {
                    val next = nextEvent
                        .values
                        .sortedBy { it.timestamp.string() }
                        .filter {
                            it.timestamp.originalDate == eventsInFuture.first().timestamp.originalDate
                        }
                    val days = daysBetween(LocalDate.now(), eventsInFuture.first().timestamp.originalDate)

                    remoteView.setTextViewText(R.id.days, context.resources.getQuantityString(R.plurals.dashboard_up_next_suffix_days, days, days))
                    remoteView.setTextViewText(R.id.daystogo, next.joinToString(separator = ", ") {
                        var result: String? = null
                        it.timestamp.ifDateAndTime { utc, local ->
                            result = local.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))
                        }
                        if (result == null) {
                            return@joinToString it.label
                        }
                        else {
                            return@joinToString "${it.label} ($result)"
                        }
                    })
                    remoteView.setViewVisibility(R.id.daystogo, View.VISIBLE)
                }

                remoteView.setOnClickPendingIntent(R.id.flag, getRefreshWidgetPendingIntent(context, widgetId, appWidgetIds))
                remoteView.setOnClickPendingIntent(R.id.circuit, getRefreshWidgetPendingIntent(context, widgetId, appWidgetIds))
                remoteView.setOnClickPendingIntent(R.id.refresh, getRefreshWidgetPendingIntent(context, widgetId, appWidgetIds))

                if (appRepository.widgetOpenApp) {
                    remoteView.setOnClickPendingIntent(R.id.container, getOpenAppPendingIntent(context))
                }
                else {
                    remoteView.setOnClickPendingIntent(R.id.container, getRefreshWidgetPendingIntent(context, widgetId, appWidgetIds))
                }
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