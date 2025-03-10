package tmg.flashback.widgets.presentation.upnext

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.LocalGlanceId
import androidx.glance.LocalSize
import androidx.glance.action.actionParametersOf
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.currentState
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
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.text.FontWeight
import androidx.glance.text.TextAlign
import kotlinx.coroutines.runBlocking
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.formula1.enums.TrackLayout
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.formula1.model.Schedule
import tmg.flashback.formula1.model.Timestamp
import tmg.flashback.strings.R.string
import tmg.flashback.ui.utils.DrawableUtils.getFlagResourceAlpha3
import tmg.flashback.widgets.R
import tmg.flashback.widgets.di.WidgetsEntryPoints
import tmg.flashback.widgets.presentation.components.TextBody
import tmg.flashback.widgets.presentation.components.TextFeature
import tmg.flashback.widgets.presentation.components.TextTitle
import tmg.flashback.widgets.utils.BitmapUtils.getBitmapFromVectorDrawable
import tmg.flashback.widgets.utils.appWidgetId
import tmg.utilities.extensions.startOfWeek
import java.io.File

class UpNextWidget : GlanceAppWidget() {

    companion object {
        private val configIcon = DpSize(48.dp, 48.dp)
        private val configRaceOnlyCompressed = DpSize(90.dp, 48.dp)
        private val configRaceOnly = DpSize(180.dp, 48.dp)
        private val configRaceScheduleFullList = DpSize(140.dp, 150.dp)
        private val configRaceScheduleFullListTrackIcon = DpSize(260.dp, 150.dp)
        private val configRaceScheduleFullListLargeRace = DpSize(200.dp, 180.dp)
        private val configRaceScheduleFullListLargeRaceTrackIcon = DpSize(260.dp, 180.dp)
    }

    override val sizeMode = SizeMode.Responsive(
        setOf(
            configIcon,
            configRaceOnlyCompressed,
            configRaceOnly,
            configRaceScheduleFullList,
            configRaceScheduleFullListTrackIcon,
            configRaceScheduleFullListLargeRace,
            configRaceScheduleFullListLargeRaceTrackIcon
        )
    )

    override val stateDefinition: GlanceStateDefinition<UpNextConfiguration>
        get() = object : GlanceStateDefinition<UpNextConfiguration> {
            override suspend fun getDataStore(
                context: Context,
                fileKey: String
            ): DataStore<UpNextConfiguration> {
                return UpNextConfigurationDataStore(
                    context = context,
                    widgetRepository = WidgetsEntryPoints.get(context).widgetsRepository(),
                    scheduleRepository = WidgetsEntryPoints.get(context).scheduleRepository(),
                )
            }

            override fun getLocation(context: Context, fileKey: String): File {
                throw NotImplementedError("Not implemented")
            }
        }


    override suspend fun provideGlance(context: Context, id: GlanceId) {
        Log.d("UpNextWidget", "provideGlance $id")
        provideContent {
            GlanceTheme(colors = GlanceTheme.colors) {
                Log.d("UpNextWidget", "provideGlance -> provideContent $id")
                val upNextConfiguration: UpNextConfiguration = currentState()
                Content(upNextConfiguration)
            }
        }
    }

    @Composable
    private fun Content(
        upNextConfiguration: UpNextConfiguration
    ) {
        val context = LocalContext.current
        if (upNextConfiguration.scheduleData != null) {
            val config = LocalSize.current
            val modifier = when (upNextConfiguration.deeplinkToEvent) {
                true -> GlanceModifier.clickable(
                    actionRunCallback<UpNextWidgetOpenEvent>(
                        actionParametersOf(
                            UpNextWidgetOpenEvent.PARAM_DATA to upNextConfiguration.scheduleData,
                        )
                    )
                )

                false -> GlanceModifier.clickable(actionRunCallback<UpNextWidgetOpenAll>())
            }

            Log.d("UpNextWidget", "Rendering widget ${LocalGlanceId.current.appWidgetId} with $upNextConfiguration")

            when (config) {
                configIcon -> Icon(
                    context = context,
                    overviewRace = upNextConfiguration.scheduleData,
                    modifier = modifier,
                )

                configRaceOnlyCompressed -> RaceOnly(
                    context = context,
                    overviewRace = upNextConfiguration.scheduleData,
                    timeSize = 22.sp,
                    showRefresh = false,
                    modifier = modifier,
                )

                configRaceOnly -> RaceOnly(
                    context = context,
                    overviewRace = upNextConfiguration.scheduleData,
                    timeSize = 30.sp,
                    showRefresh = true,
                    modifier = modifier,
                )

                configRaceScheduleFullList -> RaceScheduleFullList(
                    context = context,
                    overviewRace = upNextConfiguration.scheduleData,
                    modifier = modifier,
                    showTrackIcon = false
                )

                configRaceScheduleFullListTrackIcon -> RaceScheduleFullList(
                    context = context,
                    overviewRace = upNextConfiguration.scheduleData,
                    modifier = modifier,
                    showTrackIcon = true
                )

                configRaceScheduleFullListLargeRace -> RaceScheduleFullListLargeRace(
                    context = context,
                    overviewRace = upNextConfiguration.scheduleData,
                    modifier = modifier,
                    showTrackIcon = false
                )

                configRaceScheduleFullListLargeRaceTrackIcon -> RaceScheduleFullListLargeRace(
                    context = context,
                    overviewRace = upNextConfiguration.scheduleData,
                    modifier = modifier,
                    showTrackIcon = true
                )

                else -> {
                    Log.e("UpNextWidget", "Invalid size, throwing IAW")
                    throw IllegalArgumentException("Invalid size not matching the provided ones")
                }
            }
        } else {
            Log.i("UpNextWidget", "No race found, showing fallback")
            NoRace(
                context = context,
                modifier = GlanceModifier.clickable(actionRunCallback<UpNextWidgetRefreshWidget>()),
            )
        }

        Log.i("UpNextWidget", "provideFlance finished")
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
        deviceTime.format(DateTimeFormatter.ofPattern("EEE")) to deviceTime.format(
            DateTimeFormatter.ofPattern(
                "HH:mm"
            )
        )
    } else {
        "${deviceTime.dayOfMonth} ${deviceTime.format(DateTimeFormatter.ofPattern("MMM"))}" to deviceTime.format(
            DateTimeFormatter.ofPattern("HH:mm")
        )
    }
}

private fun OverviewRace.labels(): Pair<String, String> {
    val deviceTime = Timestamp(this.date, this.time ?: LocalTime.of(12, 0)).deviceLocalDateTime

    val sameWeek = LocalDate.now().startOfWeek() == deviceTime.toLocalDate().startOfWeek()
    val timeString =
        this.time?.let { deviceTime.format(DateTimeFormatter.ofPattern("HH:mm")) } ?: ""
    return if (sameWeek) {
        deviceTime.format(DateTimeFormatter.ofPattern("EEE")) to timeString
    } else {
        "${deviceTime.dayOfMonth} ${deviceTime.format(DateTimeFormatter.ofPattern("MMM"))}" to timeString
    }
}


@Composable
internal fun Icon(
    context: Context,
    overviewRace: OverviewRace,
    modifier: GlanceModifier = GlanceModifier,
) {
    val schedule = overviewRace.raceSchedule()
    Column(
        modifier = modifier.surface(GlanceTheme.colors.background.getColor(context)),
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
                color = GlanceTheme.colors.onBackground.getColor(context)
            )
            TextBody(
                modifier = GlanceModifier.padding(
                    top = 2.dp,
                    start = 8.dp,
                    end = 8.dp
                ),
                textAlign = TextAlign.Center,
                text = time,
                color = GlanceTheme.colors.onBackground.getColor(context)
            )
        }
    }
}

@Composable
internal fun RaceOnly(
    context: Context,
    overviewRace: OverviewRace,
    timeSize: TextUnit = 28.sp,
    showRefresh: Boolean = false,
    modifier: GlanceModifier = GlanceModifier,
) {
    Column(
        modifier = modifier.surface(GlanceTheme.colors.background.getColor(context)),
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
                    modifier = GlanceModifier
                        .defaultWeight()
                        .padding(
                            top = 4.dp,
                            start = 8.dp,
                            end = 8.dp,
                            bottom = 2.dp
                        ),
                    text = overviewRace.raceName,
                    color = GlanceTheme.colors.onBackground.getColor(context)
                )
                if (showRefresh) {
                    Image(
                        modifier = GlanceModifier.clickable(actionRunCallback<UpNextWidgetRefreshWidget>()),
                        provider = ImageProvider(R.drawable.ic_widget_refresh),
                        contentDescription = context.getString(string.ab_refresh),
//                        colorFilter = ColorFilter.tint(ColorProvider(widgetData.contentColour))
                    )
                }
            }

            FeatureDate(
                featureTextSize = timeSize,
                overviewRace = overviewRace,
                context = context
            )
        }
    }
}

@Composable
internal fun RaceScheduleFullListLargeRace(
    context: Context,
    overviewRace: OverviewRace,
    showTrackIcon: Boolean,
    modifier: GlanceModifier = GlanceModifier,
) {
    Column(
        modifier = modifier
            .padding(vertical = 16.dp, horizontal = 16.dp)
            .surface(GlanceTheme.colors.background.getColor(context))
    ) {
        Row(GlanceModifier.fillMaxWidth()) {
            CountryIcon(
                context = context,
                overviewRace = overviewRace
            )
            TextBody(
                modifier = GlanceModifier
                    .defaultWeight()
                    .padding(
                        start = 8.dp,
                        end = 8.dp
                    ),
                text = overviewRace.raceName,
                color = GlanceTheme.colors.onBackground.getColor(context)
            )
            Image(
                modifier = GlanceModifier.clickable(actionRunCallback<UpNextWidgetRefreshWidget>()),
                provider = ImageProvider(R.drawable.ic_widget_refresh),
                contentDescription = context.getString(string.ab_refresh),
            )
        }

        FeatureDate(
            overviewRace = overviewRace,
            context = context
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
                            compressed = false,
                            context = context
                        )
                    }
            }
            if (showTrackIcon) {
                TrackIcon(
                    context = context,
                    overviewRace = overviewRace,
                    trackColour = GlanceTheme.colors.onBackground.getColor(context)
                )
            }
        }
    }
}

@Composable
internal fun RaceScheduleFullList(
    context: Context,
    overviewRace: OverviewRace,
    modifier: GlanceModifier = GlanceModifier,
    showTrackIcon: Boolean
) {
    println("Race schedule full list - $showTrackIcon")
    Column(
        modifier = modifier
            .padding(vertical = 16.dp, horizontal = 16.dp)
            .surface(GlanceTheme.colors.background.getColor(context))
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
                color = GlanceTheme.colors.onBackground.getColor(context)
            )
        }

        if (overviewRace.schedule.isEmpty()) {
            Row(
                modifier = GlanceModifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom
            ) {
                Column(modifier = GlanceModifier.defaultWeight()) {
                    val (day, time) = overviewRace.labels()
                    Row(modifier = modifier.padding(top = 12.dp)) {
                        TextBody(
                            modifier = GlanceModifier,
                            color = GlanceTheme.colors.onBackground.getColor(context),
                            text = "Race",
                            weight = FontWeight.Bold
                        )
                        Spacer(GlanceModifier.width(12.dp))
                        TextBody(
                            color = GlanceTheme.colors.onBackground.getColor(context),
                            text = "$day (${time})"
                        )
                    }
                }
            }
        } else {
            Row(modifier = GlanceModifier.defaultWeight()) { }
            Row(
                modifier = GlanceModifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.Bottom
            ) {
                Column(modifier = GlanceModifier.defaultWeight()) {
                    overviewRace.schedule
                        .forEach {
                            Schedule(
                                model = it,
                                compressed = true,
                                context = context
                            )
                        }
                }
                if (showTrackIcon) {
                    TrackIcon(
                        context = context,
                        overviewRace = overviewRace,
                        trackColour = GlanceTheme.colors.onBackground.getColor(context)
                    )
                }
            }
        }
    }
}


@Composable
private fun NoRace(
    context: Context,
    modifier: GlanceModifier = GlanceModifier
) {
    Column(
        modifier = modifier.surface(GlanceTheme.colors.background.getColor(context)),
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
                    text = context.getString(string.widget_up_next_nothing_title),
                    color = GlanceTheme.colors.onBackground.getColor(context)
                )
            }

            TextBody(
                text = context.getString(string.widget_up_next_nothing_subtitle)
            )
        }
    }
}

@Composable
private fun ColumnScope.FeatureDate(
    featureTextSize: TextUnit = 32.sp,
    context: Context,
    overviewRace: OverviewRace
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
                color = GlanceTheme.colors.onBackground.getColor(context),
            )
        }
    } else {
        val (day, time) = overviewRace.labels()
        Column(
            modifier = GlanceModifier.defaultWeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextFeature(
                fontSize = featureTextSize,
                text = "$day ($time)",
                color = GlanceTheme.colors.onBackground.getColor(context),
            )
        }
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
    context: Context,
    overviewRace: OverviewRace,
    trackColour: Color,
    modifier: GlanceModifier = GlanceModifier.width(100.dp).wrapContentHeight()
) {
    val trackLayout = TrackLayout.getTrack(overviewRace.circuitId)
        ?.getIcon(overviewRace.season, overviewRace.raceName) ?: R.drawable.widget_circuit_unknown
    val bitmap = getBitmapFromVectorDrawable(context, trackLayout, trackColour.toArgb())
    if (bitmap != null) {
        Image(
            provider = ImageProvider(bitmap),
            contentDescription = overviewRace.circuitName,
            //        colorFilter = ColorFilter.tint(ColorProvider(trackColour)),
            modifier = modifier
        )
    }
}

@Composable
private fun Schedule(
    model: Schedule,
    context: Context,
    compressed: Boolean,
    modifier: GlanceModifier = GlanceModifier
) {
    val deviceLocaleTime = model.timestamp.deviceLocalDateTime
    val alpha = if (deviceLocaleTime.isBefore(LocalDateTime.now())) 0.5f else 1f
    val (day, time) = model.labels()
    Row(modifier = modifier.padding(top = 3.dp)) {
        TextBody(
            modifier = GlanceModifier,
            color = GlanceTheme.colors.onBackground.getColor(context).copy(alpha = alpha),
            text = if (compressed) model.label.shortenLabel() else model.label,
            weight = FontWeight.Bold
        )
        Spacer(GlanceModifier.width(12.dp))
        TextBody(
            color = GlanceTheme.colors.onBackground.getColor(context).copy(alpha = alpha),
            text = "$day (${time})"
        )
    }
}

private fun String.shortenLabel() = when (this.lowercase()) {
    "sprint qualifying" -> "Sprint Q.."
    "sprint shootout" -> "Sprint Q.."
    "qualifying" -> "Quali.."
    "qualify" -> "Quali.."
    else -> this
}