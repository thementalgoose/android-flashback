package tmg.flashback.widgets.providers

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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.configuration.usecases.ApplyConfigUseCase
import tmg.flashback.configuration.usecases.FetchConfigUseCase
import tmg.flashback.crash_reporting.manager.CrashManager
import tmg.flashback.device.managers.BuildConfigManager
import tmg.flashback.formula1.enums.TrackLayout
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.formula1.utils.getFlagResourceAlpha3
import tmg.flashback.statistics.repo.ScheduleRepository
import tmg.flashback.widgets.BuildConfig
import tmg.flashback.widgets.R
import tmg.flashback.widgets.WidgetNavigationComponent
import tmg.utilities.extensions.toEnum
import tmg.utilities.utils.LocalDateUtils
import javax.inject.Inject

@AndroidEntryPoint
class UpNextWidgetProvider @Inject constructor() : AppWidgetProvider() {

    @Inject
    protected lateinit var crashController: CrashManager
    @Inject
    protected lateinit var buildConfigManager: BuildConfigManager
    @Inject
    protected lateinit var applyConfigUseCase: ApplyConfigUseCase
    @Inject
    protected lateinit var fetchConfigUseCase: FetchConfigUseCase
    @Inject
    protected lateinit var scheduleRepository: ScheduleRepository
    @Inject
    protected lateinit var widgetNavigationComponent: WidgetNavigationComponent

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
    }

    override fun onUpdate(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetIds: IntArray?) {
        Log.i("Widgets", "Updating up next widgets ${if (BuildConfig.DEBUG) appWidgetIds.contentToString() else ""}")

        // Fire and forget remote config sync
        CoroutineScope(Dispatchers.IO).launch {
            applyConfigUseCase.apply()
            fetchConfigUseCase.fetch()
        }

        // Pre app checks
        val nextEvent: OverviewRace? = runBlocking {
            scheduleRepository.getUpcomingEvents()
                .minByOrNull { it.date }
        }
        if (BuildConfig.DEBUG) {
            Log.i("Widgets", "Next event found to be $nextEvent")
        }
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
            val icon: Int? = nextEvent.circuitId.toEnum<TrackLayout> { it.circuitId }?.icon
            if (icon != null) {
                tintedIcon = tintDrawable(context, icon)
            }
        }
        catch (e: Exception) {
            Log.i("Widgets", "Failed to tint icon ${e.message}")
            e.printStackTrace()
            crashController.logException(e, "Widget Up Next provider couldn't tint bitmap")
        }

        // Update all the widget ids
        appWidgetIds?.forEach { widgetId ->

            val remoteView = RemoteViews(buildConfigManager.applicationId, R.layout.widget_up_next)

            try {
                remoteView.setTextViewText(R.id.name, nextEvent.raceName)
                remoteView.setTextViewText(R.id.subtitle, nextEvent.circuitName)

                if (tintedIcon != null) {
                    remoteView.setImageViewBitmap(R.id.circuit, tintedIcon)
                }
                else {
                    remoteView.setImageViewResource(R.id.circuit, R.drawable.widget_circuit_unknown)
                }

                remoteView.setViewVisibility(R.id.flag, View.VISIBLE)
                remoteView.setImageViewResource(R.id.flag, context.getFlagResourceAlpha3(nextEvent.countryISO))

                remoteView.setTextViewText(R.id.days, nextEvent.date.format(DateTimeFormatter.ofPattern("d MMMM yyyy")))
                remoteView.setTextViewText(R.id.daystogo, "")

                val eventsToday = nextEvent.schedule
                    .filter { it.timestamp.utcLocalDateTime.toLocalDate() == LocalDate.now() }
                    .sortedBy { it.timestamp.string() }

                val eventsInFuture = nextEvent.schedule
                    .filter { it.timestamp.utcLocalDateTime.toLocalDate() > LocalDate.now() }
                    .sortedBy { it.timestamp.string() }

                if (eventsToday.isNotEmpty()) {

                    remoteView.setTextViewText(R.id.days, context.getString(R.string.dashboard_up_next_date_today))
                    remoteView.setTextViewText(R.id.daystogo, eventsToday.joinToString(separator = ", ") {
                        val result: String = it.timestamp.deviceLocalDateTime.toLocalTime().format(
                            DateTimeFormatter.ofPattern("HH:mm"))
                        return@joinToString "${it.label} ($result)"
                    })
                    remoteView.setViewVisibility(R.id.daystogo, View.VISIBLE)
                }
                if (eventsToday.isEmpty() && eventsInFuture.isNotEmpty()) {
                    val next = nextEvent
                        .schedule
                        .sortedBy { it.timestamp.string() }
                        .filter {
                            it.timestamp.utcLocalDateTime.toLocalDate() == eventsInFuture.first().timestamp.utcLocalDateTime.toLocalDate()
                        }
                    val days = LocalDateUtils.daysBetween(
                        LocalDate.now(),
                        eventsInFuture.first().timestamp.utcLocalDateTime.toLocalDate()
                    )

                    remoteView.setTextViewText(R.id.days, context.resources.getQuantityString(R.plurals.dashboard_up_next_suffix_days, days, days))
                    remoteView.setTextViewText(R.id.daystogo, next.joinToString(separator = ", ") {
                        val result: String = it.timestamp.deviceLocalDateTime.toLocalTime().format(
                            DateTimeFormatter.ofPattern("HH:mm"))
                        return@joinToString "${it.label} ($result)"
                    })
                    remoteView.setViewVisibility(R.id.daystogo, View.VISIBLE)
                }

                remoteView.setOnClickPendingIntent(R.id.flag, getRefreshWidgetPendingIntent(context, widgetId, appWidgetIds))
                remoteView.setOnClickPendingIntent(R.id.circuit, getRefreshWidgetPendingIntent(context, widgetId, appWidgetIds))
                remoteView.setOnClickPendingIntent(R.id.refresh, getRefreshWidgetPendingIntent(context, widgetId, appWidgetIds))

                remoteView.setOnClickPendingIntent(R.id.container, getOpenAppPendingIntent(context))
                appWidgetManager?.updateAppWidget(widgetId, remoteView)
            } catch (e: RuntimeException) {
                crashController.logException(e, "Widget Up Next provider couldn't be set up")
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
        val intent = widgetNavigationComponent.launchApp(context)
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    }

    private fun getRefreshWidgetPendingIntent(context: Context, widgetId: Int, appWidgetIds: IntArray?): PendingIntent {
        val intent = Intent(context, UpNextWidgetProvider::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds ?: IntArray(0))
        return PendingIntent.getBroadcast(context, widgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    }
}