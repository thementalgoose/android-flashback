package tmg.flashback.upnext.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.formula1.model.Timestamp
import tmg.flashback.upnext.R
import tmg.flashback.upnext.controllers.UpNextController
import tmg.flashback.upnext.repository.json.UpNextItemJson
import tmg.flashback.upnext.repository.model.UpNextSchedule
import tmg.flashback.upnext.repository.model.UpNextScheduleTimestamp
import tmg.flashback.upnext.ui.timezone.TimezoneItem
import tmg.flashback.upnext.utils.daysBetween
import tmg.utilities.extensions.ordinalAbbreviation

//region Inputs

interface UpNextViewModelInputs {
    fun refresh()
}

//endregion

//region Outputs

interface UpNextViewModelOutputs {
    val data: LiveData<UpNextSchedule>
    val content: LiveData<List<UpNextBreakdownModel>>
    val timezones: LiveData<List<TimezoneItem>>
    val remainingDays: LiveData<Int>
}

//endregion


class UpNextViewModel(
    private val upNextController: UpNextController
): ViewModel(), UpNextViewModelInputs, UpNextViewModelOutputs {

    override val data: MutableLiveData<UpNextSchedule> = MutableLiveData()
    override val content: MutableLiveData<List<UpNextBreakdownModel>> = MutableLiveData()
    override val timezones: MutableLiveData<List<TimezoneItem>> = MutableLiveData()
    override val remainingDays: MutableLiveData<Int> = MutableLiveData()

    var inputs: UpNextViewModelInputs = this
    var outputs: UpNextViewModelOutputs = this

    init {
        refresh()
    }

    //region Inputs

    override fun refresh() {
        when (val schedule = upNextController.getNextEvent()) {
            null -> {
                data.value = UpNextSchedule(
                    title = "Nothing here yet!",
                    subtitle = "Please check back later when we know more",
                    values = emptyList(),
                    flag = null,
                    circuitId = null,
                    season = 0,
                    round = 0
                )
                content.value = emptyList()
                timezones.value = emptyList()
                remainingDays.value = 0
            }
            else -> {
                data.value = schedule
                content.value = schedule.values
                    .groupBy { it.timestamp.originalDate }
                    .map { (date, values) ->
                        val day = date.dayOfMonth
                        return@map Pair(date, mutableListOf<UpNextBreakdownModel>().apply {
                            add(UpNextBreakdownModel.Divider)
                            add(UpNextBreakdownModel.Day(date.format(DateTimeFormatter.ofPattern("EEEE '" + day.ordinalAbbreviation + "' MMMM")), date))
                            addAll(values
                                .sortedBy { it.timestamp.string() }
                                .map {
                                    UpNextBreakdownModel.Item(it.label, it.timestamp)
                                })
                        })
                    }
                    .sortedBy { it.first }
                    .map { it.second }
                    .flatten()
                    .toList()
                timezones.value = listOf(
                    TimezoneItem(R.string.dashboard_up_next_your_time)
                )
                remainingDays.value = getRemainingDays(schedule.values)
            }
        }
    }

    //endregion

    private fun getRemainingDays(list: List<UpNextScheduleTimestamp>): Int {
        val earliestDate = list.minByOrNull { it.timestamp.originalDate }?.timestamp?.originalDate ?: return 0
        return daysBetween(LocalDate.now(), earliestDate).coerceAtLeast(0)
    }
}
