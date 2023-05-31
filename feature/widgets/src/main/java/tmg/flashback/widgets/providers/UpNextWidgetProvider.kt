package tmg.flashback.widgets.providers

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.VectorDrawable
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
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
import tmg.flashback.domain.repo.ScheduleRepository
import tmg.flashback.formula1.model.Schedule
import tmg.flashback.ui.utils.DrawableUtils.getFlagResourceAlpha3
import tmg.flashback.widgets.BuildConfig
import tmg.flashback.widgets.R
import tmg.flashback.widgets.WidgetNavigationComponent
import tmg.flashback.widgets.repository.WidgetRepository
import tmg.utilities.extensions.isInNightMode
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
    @Inject
    protected lateinit var widgetsRepository: WidgetRepository

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
        val race: OverviewRace? = runBlocking {
            scheduleRepository.getUpcomingEvents().minByOrNull { it.date }
        }
        Log.i("Widgets", "Next event found to be $race")

        if (context == null) { return }
        if (race == null) {
            appWidgetIds?.forEach { widgetId ->
                val remoteView = RemoteViews(buildConfigManager.applicationId, R.layout.widget_up_next)
                remoteView.error(appWidgetManager, widgetId, context)
            }
            return
        }

        // Update all the widget ids
        appWidgetIds?.forEach { widgetId ->
            val remoteView = RemoteViews(buildConfigManager.applicationId, R.layout.widget_up_next)
            try {

                remoteView.setData(context, race)

                val showBackground = widgetsRepository.getShowBackground(widgetId)
                val colourData = context.getWidgetColourData(showBackground, context.isInNightMode())
                remoteView.setInt(R.id.box, "setBackgroundColor", colourData.backgroundColor)
                remoteView.setTextColor(R.id.name, colourData.contentColour)
                remoteView.setTextColor(R.id.days, colourData.contentColour)
                remoteView.setTextColor(R.id.daystogo, colourData.contentColour)

                when (val icon = race.getTintedCircuitIcon(context, colourData.contentColour)) {
                    null -> remoteView.setImageViewResource(R.id.circuit, R.drawable.widget_circuit_unknown)
                    else -> remoteView.setImageViewBitmap(R.id.circuit, icon)
                }

                val eventsToday = race.eventsToday()
                val eventsInFuture = race.eventsInFuture()

                if (eventsToday.isNotEmpty()) {
                    remoteView.setTextViewText(R.id.days, context.getString(R.string.dashboard_up_next_date_today))
                    remoteView.setTextViewText(R.id.daystogo, eventsToday.label())
                    remoteView.setViewVisibility(R.id.daystogo, View.VISIBLE)
                } else if (eventsInFuture.isNotEmpty()) {
                    val next = race
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
                    remoteView.setTextViewText(R.id.daystogo, next.label())
                    remoteView.setViewVisibility(R.id.daystogo, View.VISIBLE)
                }

                remoteView.setOnClickPendingIntent(R.id.flag, getRefreshWidgetPendingIntent(context, widgetId, appWidgetIds))
                remoteView.setOnClickPendingIntent(R.id.circuit, getRefreshWidgetPendingIntent(context, widgetId, appWidgetIds))

                remoteView.setOnClickPendingIntent(R.id.container, getOpenAppPendingIntent(context))
                appWidgetManager?.updateAppWidget(widgetId, remoteView)
            } catch (e: RuntimeException) {
                crashController.logException(e, "Widget Up Next provider couldn't be set up")
                remoteView.error(appWidgetManager, widgetId, context)
            }
        }
    }

    /**
     * Set the next state for the widget
     */
    private fun RemoteViews.setData(context: Context, race: OverviewRace) {
        this.setTextViewText(R.id.name, race.raceName)
//        this.setTextViewText(R.id.subtitle, race.circuitName)

        this.setViewVisibility(R.id.flag, View.VISIBLE)
        this.setImageViewResource(R.id.flag, context.getFlagResourceAlpha3(race.countryISO))

        this.setTextViewText(R.id.days, race.date.format(DateTimeFormatter.ofPattern("d MMMM yyyy")))
        this.setTextViewText(R.id.daystogo, "")
    }

    /**
     * Get the tinted circuit icon for the overview race
     */
    private fun OverviewRace.getTintedCircuitIcon(context: Context, @ColorInt toColour: Int): Bitmap? {
        var tintedIcon: Bitmap? = null
        try {
            val icon: Int? = this.circuitId.toEnum<TrackLayout> { it.circuitId }?.getDefaultIcon()
            if (icon != null) {
                tintedIcon = tintDrawable(context, icon, toColour)
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
            crashController.logException(e, "Widget Up Next provider couldn't tint bitmap")
        }
        return tintedIcon
    }

    private fun OverviewRace.eventsToday(): List<Schedule> = this
        .schedule
        .filter { it.timestamp.utcLocalDateTime.toLocalDate() == LocalDate.now() }
        .sortedBy { it.timestamp.string() }

    private fun OverviewRace.eventsInFuture() = this
        .schedule
        .filter { it.timestamp.utcLocalDateTime.toLocalDate() > LocalDate.now() }
        .sortedBy { it.timestamp.string() }

    private fun List<Schedule>.label() = this.joinToString(separator = ", ") {
        val result: String = it.timestamp.deviceLocalDateTime.toLocalTime().format(
            DateTimeFormatter.ofPattern("HH:mm"))
        return@joinToString "${it.label} ($result)"
    }

    /**
     * Set the error state for the widget
     */
    private fun RemoteViews.error(appWidgetManager: AppWidgetManager?, widgetId: Int, context: Context) {
        this.setTextViewText(R.id.name, context.getString(R.string.widget_up_next_nothing_title))
        this.setImageViewResource(R.id.circuit, R.drawable.widget_circuit_unknown)
        this.setViewVisibility(R.id.flag, View.GONE)
        this.setTextViewText(R.id.days, "")
        this.setTextViewText(R.id.daystogo, "")

        this.setOnClickPendingIntent(R.id.container, getOpenAppPendingIntent(context))

        appWidgetManager?.updateAppWidget(widgetId, this)
    }





    private fun tintDrawable(context: Context, @DrawableRes drawableId: Int, @ColorInt toColour: Int): Bitmap {
        val source = (ResourcesCompat.getDrawable(context.resources, drawableId, null) as VectorDrawable).toBitmap()
        return changeBitmapColour(source, toColour)
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

    private fun Context.getWidgetColourData(showBackground: Boolean, isDarkMode: Boolean): ColData {
        return ColData(
            contentColour = when {
                showBackground && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && isDarkMode -> ContextCompat.getColor(this, android.R.color.system_neutral1_50)
                showBackground && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> ContextCompat.getColor(this, android.R.color.system_neutral1_800)
                showBackground -> ContextCompat.getColor(this, R.color.widget_content)
                else -> ContextCompat.getColor(this, R.color.widget_content)
            },
            backgroundColor = when {
                showBackground && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && isDarkMode -> ContextCompat.getColor(this, android.R.color.system_accent2_800)
                showBackground && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> ContextCompat.getColor(this, android.R.color.system_accent2_50)
                showBackground -> ContextCompat.getColor(this, R.color.widget_upnext_background)
                else -> Color.TRANSPARENT
            }
        )
    }

    data class ColData(
        val contentColour: Int,
        val backgroundColor: Int
    )
}