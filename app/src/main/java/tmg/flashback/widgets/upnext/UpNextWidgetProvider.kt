package tmg.flashback.widgets.upnext

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
import org.threeten.bp.LocalDate
import org.threeten.bp.temporal.ChronoUnit
import tmg.flashback.BuildConfig
import tmg.flashback.R
import tmg.flashback.constants.TrackLayout
import tmg.flashback.controllers.UpNextController
import tmg.flashback.firebase.crash.FirebaseCrashManager
import tmg.flashback.firebase.crash.FirebaseCrashManagerImpl
import tmg.flashback.managers.remoteconfig.FirebaseRemoteConfigManager
import tmg.flashback.managers.sharedprefs.SharedPreferenceManager
import tmg.flashback.repo.models.remoteconfig.UpNextSchedule
import tmg.flashback.ui.SplashActivity
import tmg.flashback.ui.utils.getFlagResourceAlpha3
import tmg.utilities.extensions.toEnum
import java.lang.Exception
import kotlin.math.absoluteValue

class UpNextWidgetProvider : AppWidgetProvider() {

    var crashManager: FirebaseCrashManager? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
    }

    override fun onUpdate(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetIds: IntArray?) {
        Log.i("Flashback", "Updating up next widgets... ${appWidgetIds.toString()}")

        val upNextController = getUpNextController(context)
        val nextEvent: UpNextSchedule? = upNextController?.getNextEvent()

        if (context == null) {
            return
        }
        if (nextEvent == null) {
            appWidgetIds?.forEach { widgetId ->
                val remoteView = RemoteViews(BuildConfig.APPLICATION_ID, R.layout.widget_up_next)
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
            crashManager?.logError(e, "Widget Up Next provider couldn't tint bitmap")
        }

        appWidgetIds?.forEach { widgetId ->

            val remoteView = RemoteViews(BuildConfig.APPLICATION_ID, R.layout.widget_up_next)

            try {
                remoteView.setTextViewText(R.id.name, nextEvent.name)
                remoteView.setTextViewText(R.id.subtitle, nextEvent.circuitName ?: "")

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

                when (val days = ChronoUnit.DAYS.between(nextEvent.date, LocalDate.now()).toInt().absoluteValue) {
                    0 -> {
                        remoteView.setTextViewText(R.id.days, context.getString(R.string.dashboard_up_next_today))
                        remoteView.setTextViewText(R.id.daystogo, "")
                        remoteView.setViewVisibility(R.id.daystogo, View.INVISIBLE)
                    }
                    else -> {
                        remoteView.setTextViewText(R.id.days, days.toString())
                        remoteView.setViewVisibility(R.id.daystogo, View.VISIBLE)
                        remoteView.setTextViewText(R.id.daystogo, context.resources.getQuantityText(R.plurals.dashboard_up_next_days_to_go, days))
                    }
                }

                remoteView.setOnClickPendingIntent(R.id.container, getRefreshWidgetPendingIntent(context, widgetId, appWidgetIds))
                appWidgetManager?.updateAppWidget(widgetId, remoteView)
            } catch (e: RuntimeException) {
                crashManager?.logError(e, "Widget Up Next provider couldn't be set up")
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
        val intent = Intent(context, SplashActivity::class.java)
        return PendingIntent.getActivity(context, 0, intent, 0)
    }

    private fun getRefreshWidgetPendingIntent(context: Context, widgetId: Int, appWidgetIds: IntArray?): PendingIntent {
        val intent = Intent(context, UpNextWidgetProvider::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds ?: IntArray(0))
        return PendingIntent.getBroadcast(context, widgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun getUpNextController(context: Context?): UpNextController? {
        if (context == null) {
            return null
        }
        crashManager = FirebaseCrashManagerImpl()
        val sharedPreferenceManager = SharedPreferenceManager(context)
        val remoteConfig = FirebaseRemoteConfigManager(crashManager, sharedPreferenceManager)
        return UpNextController(remoteConfig)
    }
}