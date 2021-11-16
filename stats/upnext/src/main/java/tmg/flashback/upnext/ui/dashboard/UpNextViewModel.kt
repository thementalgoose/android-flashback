package tmg.flashback.upnext.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.formula1.model.Schedule
import tmg.flashback.upnext.R
import tmg.flashback.upnext.controllers.UpNextController
import tmg.flashback.upnext.model.NotificationChannel
import tmg.flashback.upnext.repository.model.UpNextSchedule
import tmg.flashback.upnext.repository.model.UpNextScheduleTimestamp
import tmg.flashback.upnext.ui.timezone.TimezoneItem
import tmg.flashback.upnext.utils.NotificationUtils
import tmg.utilities.extensions.ordinalAbbreviation
import tmg.utilities.utils.LocalDateUtils.Companion.daysBetween

//region Inputs

interface UpNextViewModelInputs {
    fun refresh()
}

//endregion

//region Outputs

interface UpNextViewModelOutputs {
    val data: LiveData<OverviewRace>
    val content: LiveData<List<UpNextBreakdownModel>>
    val remainingDays: LiveData<Int>
}

//endregion


class UpNextViewModel(
    private val upNextController: UpNextController,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel(), UpNextViewModelInputs, UpNextViewModelOutputs {

    override val data: MutableLiveData<OverviewRace> = MutableLiveData()
    override val content: MutableLiveData<List<UpNextBreakdownModel>> = MutableLiveData()
    override val remainingDays: MutableLiveData<Int> = MutableLiveData()

    var inputs: UpNextViewModelInputs = this
    var outputs: UpNextViewModelOutputs = this

    init {
        refresh()
    }

    //region Inputs

    override fun refresh() {
        viewModelScope.launch(ioDispatcher) {
            when (val schedule = upNextController.getNextEvent()) {
                null -> {
                    data.postValue(OverviewRace(
                        date = LocalDate.now(),
                        time  = null,
                        season = 0,
                        round = 0,
                        raceName = "Nothing here yet",
                        circuitId = "",
                        circuitName = "",
                        country = "",
                        countryISO = "",
                        hasQualifying = false,
                        hasResults = false,
                        schedule = emptyList(),
                    ))
                    content.postValue(emptyList())
                    remainingDays.postValue(0)
                }
                else -> {
                    data.postValue(schedule)
                    content.postValue(schedule.schedule
                        .groupBy { it.timestamp.originalDate }
                        .map { (date, values) ->
                            val day = date.dayOfMonth
                            return@map Pair(date, mutableListOf<UpNextBreakdownModel>().apply {
                                add(UpNextBreakdownModel.Divider)
                                add(UpNextBreakdownModel.Day(date.format(DateTimeFormatter.ofPattern("EEEE '" + day.ordinalAbbreviation + "' MMMM")), date))
                                addAll(values
                                    .sortedBy { it.timestamp.string() }
                                    .map {
                                        val showBellIndicator = when (NotificationUtils.getCategoryBasedOnLabel(it.label)) {
                                            NotificationChannel.RACE -> upNextController.notificationRace
                                            NotificationChannel.QUALIFYING -> upNextController.notificationQualifying
                                            NotificationChannel.FREE_PRACTICE -> upNextController.notificationFreePractice
                                            NotificationChannel.SEASON_INFO -> upNextController.notificationSeasonInfo
                                        }
                                        UpNextBreakdownModel.Item(it.label, it.timestamp, showBellIndicator)
                                    })
                            })
                        }
                        .sortedBy { it.first }
                        .map { it.second }
                        .flatten()
                        .toList()
                    )
                    remainingDays.postValue(getRemainingDays(schedule.schedule))
                }
            }
        }
    }

    //endregion

    private fun getRemainingDays(list: List<Schedule>): Int {
        val earliestDate = list.minByOrNull { it.timestamp.originalDate }?.timestamp?.originalDate ?: return 0
        return daysBetween(LocalDate.now(), earliestDate).coerceAtLeast(0)
    }
}
