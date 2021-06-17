package tmg.flashback.upnext.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.formula1.model.Timestamp
import tmg.flashback.upnext.controllers.UpNextController
import tmg.flashback.upnext.repository.json.UpNextItemJson
import tmg.flashback.upnext.repository.model.UpNextSchedule
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
}

//endregion


class UpNextViewModel(
    private val upNextController: UpNextController
): ViewModel(), UpNextViewModelInputs, UpNextViewModelOutputs {

    override val data: MutableLiveData<UpNextSchedule> = MutableLiveData()
    override val content: MutableLiveData<List<UpNextBreakdownModel>> = MutableLiveData()

    var inputs: UpNextViewModelInputs = this
    var outputs: UpNextViewModelOutputs = this

    init {
        refresh()
    }

    //region Inputs

    override fun refresh() {
        upNextController.getNextEvent()?.let { schedule ->
            data.value = schedule
            content.value = schedule.values
                .groupBy { it.timestamp.originalDate }
                .map { (date, values) ->
                    val day = date.dayOfMonth
                    return@map Pair(date, mutableListOf<UpNextBreakdownModel>().apply {
                        add(UpNextBreakdownModel.Divider)
                        add(UpNextBreakdownModel.Day(date.format(DateTimeFormatter.ofPattern("EEEE '" + day.ordinalAbbreviation + "' MMM"))))
                        addAll(values.map {
                            UpNextBreakdownModel.Item(it.label, it.timestamp)
                        })
                    })
                }
                .sortedBy { it.first }
                .map { it.second }
                .flatten()
                .toList()
        }
    }

    //endregion

    //region Outputs

    //endregion
}
