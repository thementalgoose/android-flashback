package tmg.flashback.widgets.upnext.presentation

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.LocalContext
import androidx.glance.LocalGlanceId
import androidx.glance.LocalSize
import androidx.glance.action.actionParametersOf
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.currentState
import androidx.glance.state.GlanceStateDefinition
import kotlinx.coroutines.runBlocking
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.style.widgets.BuildConfig
import tmg.flashback.style.widgets.WidgetTheme
import tmg.flashback.style.widgets.WidgetThemePreview
import tmg.flashback.style.widgets.preview.Preview1x1
import tmg.flashback.style.widgets.text.TextBody
import tmg.flashback.style.widgets.utils.appWidgetId
import tmg.flashback.widgets.upnext.di.WidgetsEntryPoints
import tmg.flashback.widgets.upnext.presentation.layout.NoRace
import tmg.flashback.widgets.upnext.presentation.layout.RaceLarge
import tmg.flashback.widgets.upnext.presentation.layout.RaceIcon
import tmg.flashback.widgets.upnext.presentation.layout.RaceName
import tmg.flashback.widgets.upnext.presentation.layout.RaceSmall
import tmg.flashback.widgets.upnext.presentation.layout.ScheduleList
import tmg.flashback.widgets.upnext.presentation.layout.ScheduleListRace
import java.io.File

class UpNextWidget : GlanceAppWidget() {

    companion object {
        private val configurationIcon = DpSize(36.dp, 36.dp)
        private val configurationIconRaceName = DpSize(109.dp, 36.dp)
        private val configurationIconDetails = DpSize(56.dp, 56.dp)
        private val configurationRaceName = DpSize(140.dp, 56.dp)
        private val configurationRaceSmall = DpSize(160.dp, 56.dp)
        private val configurationRaceSmall2 = DpSize(140.dp, 80.dp)
        private val configurationRaceLarge = DpSize(200.dp, 80.dp)
        private val configurationScheduleList = DpSize(160.dp, 140.dp)
        private val configurationScheduleListWithTrack = DpSize(300.dp, 140.dp)
        private val configurationScheduleListRace = DpSize(200.dp, 180.dp)
        private val configurationScheduleListRaceWithTrack = DpSize(300.dp, 180.dp)
    }

    override val sizeMode = SizeMode.Responsive(
        setOf(
            configurationIcon,
            configurationIconRaceName,
            configurationIconDetails,
            configurationRaceName,
            configurationRaceSmall,
            configurationRaceLarge,
            configurationScheduleList,
            configurationScheduleListWithTrack,
            configurationScheduleListRace,
            configurationScheduleListRaceWithTrack,
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
                    upNextWidgetRepository = WidgetsEntryPoints.get(context).widgetsRepository(),
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
            WidgetTheme {
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
                configurationIcon -> RaceIcon(
                    context = context,
                    overviewRace = upNextConfiguration.scheduleData,
                    showText = false,
                    modifier = modifier,
                )
                configurationRaceName,
                configurationIconRaceName-> RaceName(
                    context = context,
                    overviewRace = upNextConfiguration.scheduleData,
                    modifier = modifier,
                )
                configurationIconDetails -> RaceIcon(
                    context = context,
                    overviewRace = upNextConfiguration.scheduleData,
                    showText = true,
                    modifier = modifier,
                )
                configurationRaceSmall,
                configurationRaceSmall2 -> RaceSmall(
                    context = context,
                    overviewRace = upNextConfiguration.scheduleData,
                    showRefresh = false,
                    modifier = modifier,
                )
                configurationRaceLarge -> RaceLarge(
                    context = context,
                    overviewRace = upNextConfiguration.scheduleData,
                    showRefresh = true,
                    modifier = modifier,
                )
                configurationScheduleList -> ScheduleList(
                    context = context,
                    overviewRace = upNextConfiguration.scheduleData,
                    modifier = modifier,
                    showTrackIcon = false
                )
                configurationScheduleListWithTrack -> ScheduleList(
                    context = context,
                    overviewRace = upNextConfiguration.scheduleData,
                    modifier = modifier,
                    showTrackIcon = true
                )
                configurationScheduleListRace -> ScheduleListRace(
                    context = context,
                    overviewRace = upNextConfiguration.scheduleData,
                    modifier = modifier,
                    showTrackIcon = false
                )
                configurationScheduleListRaceWithTrack -> ScheduleListRace(
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
//            if (BuildConfig.DEBUG) {
//                TextBody(
//                    text = "$config",
//                    color = GlanceTheme.colors.onBackground.getColor(context)
//                )
//            }
        } else {
            Log.i("UpNextWidget", "No race found, showing fallback")
            NoRace(
                context = context,
                modifier = GlanceModifier.clickable(actionRunCallback<UpNextWidgetRefreshWidget>()),
            )
        }

        Log.i("UpNextWidget", "provideFlance finished")
    }
}