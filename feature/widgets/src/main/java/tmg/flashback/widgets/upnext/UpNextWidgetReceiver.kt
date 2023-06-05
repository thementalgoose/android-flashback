package tmg.flashback.widgets.upnext

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_IDS
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.glance.LocalContext
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.updateAll
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.formula1.model.Schedule
import tmg.flashback.style.AppThemePreview
import tmg.flashback.widgets.presentation.WidgetConfigurationData

// https://developer.android.com/reference/kotlin/androidx/glance/appwidget/GlanceAppWidgetReceiver
@AndroidEntryPoint
class UpNextWidgetReceiver: GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = UpNextWidget()

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        Log.i("UpNextWidget", "onReceive ${intent.extras?.getIntArray(EXTRA_APPWIDGET_IDS)?.joinToString { it.toString() }}")
        runBlocking(Dispatchers.IO) {
            glanceAppWidget.updateAll(context)
        }
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        Log.i("UpNextWidget", "onUpdate ${appWidgetIds.joinToString { it.toString() }}")
    }
}

@Preview
@Composable
private fun PreviewIcon() {
    AppThemePreview {
        // The size of the widget
        Icon(
            context = LocalContext.current,
            widgetData = fakeWidgetConfigurationData,
            overviewRace = fakeOverviewRace
        )
    }
}




private val fakeWidgetConfigurationData = WidgetConfigurationData(
    contentColour = Color.DarkGray,
    backgroundColor = Color.White
)
private val fakeOverviewRace = OverviewRace(
    date = LocalDate.of(2020, 1, 3),
    time = LocalTime.of(15, 0, 0),
    season = 2020,
    round = 1,
    raceName = "Emilia Romagna Grand Prix",
    circuitId = "imola",
    circuitName = "Imola Circuit",
    laps = "66",
    country = "Italy",
    countryISO = "IT",
    hasQualifying = false,
    hasSprint = false,
    hasResults = false,
    schedule = listOf(
        Schedule("FP1", LocalDate.of(2020, 1, 1), LocalTime.of(12, 0), null),
        Schedule("FP2", LocalDate.of(2020, 1, 1), LocalTime.of(15, 0), null),
        Schedule("FP3", LocalDate.of(2020, 1, 2), LocalTime.of(11, 0), null),
        Schedule("Qualifying", LocalDate.of(2020, 1, 2), LocalTime.of(14, 0), null),
        Schedule("Race", LocalDate.of(2020, 1, 3), LocalTime.of(15, 0), null)
    )
)