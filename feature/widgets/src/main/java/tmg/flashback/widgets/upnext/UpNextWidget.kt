package tmg.flashback.widgets.upnext

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.ColorFilter
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalSize
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.ColumnScope
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.layout.wrapContentHeight
import androidx.glance.text.FontWeight
import androidx.glance.text.TextAlign
import androidx.glance.unit.ColorProvider
import kotlinx.coroutines.runBlocking
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.formula1.enums.TrackLayout
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.formula1.model.Schedule
import tmg.flashback.ui.utils.DrawableUtils.getFlagResourceAlpha3
import tmg.flashback.widgets.R
import tmg.flashback.widgets.di.WidgetsEntryPoints
import tmg.flashback.widgets.presentation.TextBody
import tmg.flashback.widgets.presentation.TextFeature
import tmg.flashback.widgets.presentation.TextTitle
import tmg.flashback.widgets.presentation.WidgetConfigurationData
import tmg.flashback.widgets.presentation.getWidgetColourData
import tmg.utilities.extensions.ordinalAbbreviation
import tmg.utilities.extensions.startOfWeek

class UpNextWidget: GlanceAppWidget() {

    companion object {

        private val configIcon = DpSize(48.dp, 48.dp)
        private val configRaceOnlyCompressed = DpSize(90.dp, 48.dp)
        private val configRaceOnly = DpSize(180.dp, 48.dp)
        private val configRaceScheduleFullList = DpSize(140.dp, 180.dp)
        private val configRaceScheduleFullListLargeRace = DpSize(200.dp, 180.dp)
        private val configRaceScheduleFullListLargeRaceTrackIcon = DpSize(280.dp, 180.dp)
    }

    override val sizeMode = SizeMode.Responsive(setOf(
        configIcon,
        configRaceOnlyCompressed,
        configRaceOnly,
        configRaceScheduleFullList,
        configRaceScheduleFullListLargeRace,
        configRaceScheduleFullListLargeRaceTrackIcon
    ))


    override suspend fun provideGlance(context: Context, id: GlanceId) = provideContent {
        val overviewRace = getNextOverviewRace(context)
        Log.i("Widget", "Updating glance id $id")
        val widgetData = getWidgetColourData(context, showBackground = true, isDarkMode = true)

        if (overviewRace != null) {
            when (LocalSize.current) {
                configIcon -> Icon(
                    context,
                    widgetData,
                    overviewRace,
                    modifier = GlanceModifier.clickable(actionRunCallback<UpNextWidgetOpenAll>()),
                )
                configRaceOnlyCompressed -> RaceOnly(
                    context,
                    widgetData,
                    overviewRace,
                    timeSize = 18.sp,
                    modifier = GlanceModifier.clickable(actionRunCallback<UpNextWidgetOpenAll>()),
                )
                configRaceOnly -> RaceOnly(
                    context,
                    widgetData,
                    overviewRace,
                    timeSize = 28.sp,
                    modifier = GlanceModifier.clickable(actionRunCallback<UpNextWidgetOpenAll>()),
                )
                configRaceScheduleFullList -> RaceScheduleFullList(
                    context,
                    widgetData,
                    overviewRace,
                    modifier = GlanceModifier.clickable(actionRunCallback<UpNextWidgetOpenAll>()),
                )
                configRaceScheduleFullListLargeRace -> RaceScheduleFullListLargeRace(
                    context,
                    widgetData,
                    overviewRace,
                    modifier = GlanceModifier.clickable(actionRunCallback<UpNextWidgetOpenAll>()),
                    showTrackIcon = false
                )
                configRaceScheduleFullListLargeRaceTrackIcon -> RaceScheduleFullListLargeRace(
                    context,
                    widgetData,
                    overviewRace,
                    modifier = GlanceModifier.clickable(actionRunCallback<UpNextWidgetOpenAll>()),
                    showTrackIcon = true
                )
                else -> throw IllegalArgumentException("Invalid size not matching the provided ones")
            }
        } else {
            NoRace(
                context,
                widgetData,
                modifier = GlanceModifier.clickable(actionRunCallback<UpNextWidgetRefreshWidget>()),
            )
        }
    }

    private fun getNextOverviewRace(context: Context): OverviewRace? {
        val scheduleRepository = WidgetsEntryPoints.get(context).scheduleRepository()
        return runBlocking {
            scheduleRepository.getUpcomingEvents().minByOrNull { it.date }
        }
    }
}

@Composable
private fun GlanceModifier.surface(color: Color): GlanceModifier = this
    .fillMaxSize()
    .background(color)
    .padding(0.dp)

private fun OverviewRace.raceSchedule(): Schedule? {
    return this.schedule.firstOrNull { it.label.lowercase() == "race" }
}

private fun Schedule.labels(): Pair<String, String> {
    val deviceTime = this.timestamp.deviceLocalDateTime

    val sameWeek = LocalDate.now().startOfWeek() == deviceTime.toLocalDate().startOfWeek()
    return if (sameWeek) {
        deviceTime.format(DateTimeFormatter.ofPattern("EEE")) to deviceTime.format(DateTimeFormatter.ofPattern("HH:mm"))
    } else {
        "${deviceTime.dayOfMonth.ordinalAbbreviation} ${deviceTime.format(DateTimeFormatter.ofPattern("MMM"))}" to deviceTime.format(DateTimeFormatter.ofPattern("HH:mm"))
    }
}



@Composable
internal fun Icon(
    context: Context,
    widgetData: WidgetConfigurationData,
    overviewRace: OverviewRace,
    modifier: GlanceModifier = GlanceModifier,
) {
    val schedule = overviewRace.raceSchedule()
    Column(
        modifier = modifier.surface(widgetData.backgroundColor),
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CountryIcon(
            context = context,
            overviewRace = overviewRace
        )
        if (schedule != null) {
            val (day, time) = schedule.labels()
            TextBody(
                modifier = GlanceModifier.padding(
                    top = 2.dp,
                    start = 8.dp,
                    end = 8.dp
                ),
                textAlign = TextAlign.Center,
                text = day,
                weight = FontWeight.Bold,
                color = widgetData.contentColour
            )
            TextBody(
                modifier = GlanceModifier.padding(
                    top = 2.dp,
                    start = 8.dp,
                    end = 8.dp
                ),
                textAlign = TextAlign.Center,
                text = time,
                color = widgetData.contentColour
            )
        }
    }
}

@Composable
internal fun RaceOnly(
    context: Context,
    widgetData: WidgetConfigurationData,
    overviewRace: OverviewRace,
    timeSize: TextUnit = 28.sp,
    modifier: GlanceModifier = GlanceModifier,
) {
    Column(
        modifier = modifier.surface(widgetData.backgroundColor),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = GlanceModifier.padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = GlanceModifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CountryIcon(
                    context = context,
                    overviewRace = overviewRace
                )
                TextBody(
                    modifier = GlanceModifier.padding(
                        top = 4.dp,
                        start = 8.dp,
                        end = 8.dp,
                        bottom = 2.dp
                    ),
                    text = overviewRace.raceName,
                    color = widgetData.contentColour
                )
            }

            FeatureDate(
                featureTextSize = timeSize,
                overviewRace = overviewRace,
                widgetData = widgetData
            )
        }
    }
}

@Composable
internal fun RaceScheduleFullListLargeRace(
    context: Context,
    widgetData: WidgetConfigurationData,
    overviewRace: OverviewRace,
    showTrackIcon: Boolean,
    modifier: GlanceModifier = GlanceModifier,
) {
    Column(modifier = modifier
        .padding(vertical = 16.dp, horizontal = 16.dp)
        .surface(widgetData.backgroundColor)
    ) {
        Row(GlanceModifier.fillMaxWidth()) {
            CountryIcon(
                context = context,
                overviewRace = overviewRace
            )
            TextBody(
                modifier = GlanceModifier.padding(
                    start = 8.dp,
                    end = 8.dp
                ),
                text = overviewRace.raceName,
                color = widgetData.contentColour
            )
        }

        FeatureDate(
            overviewRace = overviewRace,
            widgetData = widgetData
        )
        Row(
            modifier = GlanceModifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom
        ) {
            Column(modifier = GlanceModifier.defaultWeight()) {
                overviewRace.schedule
                    .filter { it != overviewRace.raceSchedule() }
                    .forEach {
                        Schedule(
                            model = it,
                            widgetData = widgetData
                        )
                    }
            }
            if (showTrackIcon) {
                TrackIcon(overviewRace = overviewRace)
            }
        }
    }
}

@Composable
internal fun RaceScheduleFullList(
    context: Context,
    widgetData: WidgetConfigurationData,
    overviewRace: OverviewRace,
    modifier: GlanceModifier = GlanceModifier,
) {
    Column(modifier = modifier
        .padding(vertical = 16.dp, horizontal = 16.dp)
        .surface(widgetData.backgroundColor)
    ) {
        Row(GlanceModifier.fillMaxWidth()) {
            CountryIcon(
                context = context,
                overviewRace = overviewRace
            )
            TextBody(
                modifier = GlanceModifier.padding(
                    start = 8.dp,
                    end = 8.dp
                ),
                text = overviewRace.raceName,
                color = widgetData.contentColour
            )
        }

        Row(modifier = GlanceModifier.defaultWeight()) { }
        Row(
            modifier = GlanceModifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom
        ) {
            Column(modifier = GlanceModifier.defaultWeight()) {
                overviewRace.schedule
                    .forEach {
                        Schedule(
                            model = it,
                            widgetData = widgetData
                        )
                    }
            }
        }
    }
}


@Composable
private fun NoRace(
    context: Context,
    widgetData: WidgetConfigurationData,
    modifier: GlanceModifier = GlanceModifier
) {
    Column(
        modifier = modifier.surface(widgetData.backgroundColor),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = GlanceModifier.padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(GlanceModifier.fillMaxWidth()) {
                TextTitle(
                    modifier = GlanceModifier.padding(
                        top = 4.dp,
                        bottom = 4.dp
                    ),
                    text = context.getString(R.string.widget_up_next_nothing_title),
                    color = widgetData.contentColour
                )
            }

            TextBody(
                text = context.getString(R.string.widget_up_next_nothing_subtitle)
            )
        }
    }
}

@Composable
private fun ColumnScope.FeatureDate(
    featureTextSize: TextUnit = 32.sp,
    overviewRace: OverviewRace,
    widgetData: WidgetConfigurationData
) {
    val raceSchedule = overviewRace.raceSchedule()
    if (raceSchedule != null) {
        val (day, time) = raceSchedule.labels()
        Column(
            modifier = GlanceModifier.defaultWeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextFeature(
                fontSize = featureTextSize,
                text = "$day ($time)",
                color = widgetData.contentColour,
            )
        }
    } else {
        Row(modifier = GlanceModifier.defaultWeight()) { }
    }
}

@Composable
private fun CountryIcon(
    context: Context,
    overviewRace: OverviewRace,
    modifier: GlanceModifier = GlanceModifier.size(24.dp, 24.dp)
) {
    val resource = context.getFlagResourceAlpha3(overviewRace.countryISO)
    Image(
        provider = ImageProvider(resId = resource),
        contentDescription = overviewRace.country,
        modifier = modifier
    )
}

@Composable
private fun TrackIcon(
    overviewRace: OverviewRace,
    modifier: GlanceModifier = GlanceModifier.width(100.dp).wrapContentHeight()
) {
    val trackLayout = TrackLayout.getTrack(overviewRace.circuitId)?.getIcon(overviewRace.season, overviewRace.raceName) ?: R.drawable.widget_circuit_unknown
    Image(
        provider = ImageProvider(resId = trackLayout),
        contentDescription = overviewRace.circuitName,
        colorFilter = ColorFilter.tint(ColorProvider(Color.White)),
        modifier = modifier
    )
}

@Composable
private fun Schedule(
    model: Schedule,
    widgetData: WidgetConfigurationData,
    modifier: GlanceModifier = GlanceModifier
) {
    val deviceLocaleTime = model.timestamp.deviceLocalDateTime
    val alpha = if (deviceLocaleTime.isBefore(LocalDateTime.now())) 0.5f else 1f
    val (day, time) = model.labels()
    Row(modifier = modifier.padding(top = 6.dp)) {
        TextBody(
            modifier = GlanceModifier,
            color = widgetData.contentColour.copy(alpha = alpha),
            text = model.label,
            weight = FontWeight.Bold
        )
        Spacer(GlanceModifier.width(12.dp))
        TextBody(
            color = widgetData.contentColour.copy(alpha = alpha),
            text = "$day (${time})"
        )
    }
}
