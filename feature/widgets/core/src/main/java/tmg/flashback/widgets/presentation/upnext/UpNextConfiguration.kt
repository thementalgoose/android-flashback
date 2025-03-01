package tmg.flashback.widgets.presentation.upnext

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import tmg.flashback.data.repo.ScheduleRepository
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.widgets.presentation.components.WidgetColourData
import tmg.flashback.widgets.presentation.components.getWidgetColourData
import tmg.flashback.widgets.repository.WidgetRepository
import tmg.utilities.extensions.isInNightMode

data class UpNextConfiguration(
    val scheduleData: OverviewRace?,
    val widgetColourData: WidgetColourData,
    val deeplinkToEvent: Boolean
) {
    constructor(
        scheduleData: OverviewRace?,
        showBackground: Boolean,
        deeplinkToEvent: Boolean,
        context: Context,
        isDarkMode: Boolean = context.isInNightMode(),
    ): this(
        scheduleData = scheduleData,
        widgetColourData = getWidgetColourData(context, showBackground, isDarkMode),
        deeplinkToEvent = deeplinkToEvent,
    )
}

class UpNextConfigurationDataStore(
    private val context: Context,
    private val widgetRepository: WidgetRepository,
    private val scheduleRepository: ScheduleRepository,
): DataStore<UpNextConfiguration> {

    private suspend fun getConfig(): UpNextConfiguration {
        val nextRace = scheduleRepository.getUpcomingEvents().minByOrNull { it.date }
        return UpNextConfiguration(
            scheduleData = nextRace,
            showBackground = widgetRepository.showBackground,
            deeplinkToEvent = widgetRepository.deeplinkToEvent,
            context = context
        )
    }

    override val data: Flow<UpNextConfiguration>
        get() = flow {
            emit(getConfig())
        }

    override suspend fun updateData(
        transform: suspend (t: UpNextConfiguration) -> UpNextConfiguration
    ): UpNextConfiguration {
        return transform(getConfig())
    }
}