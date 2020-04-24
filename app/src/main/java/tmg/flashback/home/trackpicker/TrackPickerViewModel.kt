package tmg.flashback.home.trackpicker

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import tmg.flashback.base.BaseViewModel
import tmg.flashback.extensions.toDataEvent
import tmg.flashback.repo.db.HistoryDB
import tmg.flashback.supportedYears
import tmg.flashback.utils.SeasonRound
import tmg.flashback.utils.Selected
import tmg.utilities.extensions.combinePair
import tmg.utilities.lifecycle.DataEvent

//region Inputs

interface TrackPickerViewModelInputs {
    fun initialSeasonRound(season: Int, round: Int)
    fun clickSeason(season: Int)
    fun clickRound(round: Int)
}

//endregion

//region Outputs

interface TrackPickerViewModelOutputs {

    val selected: LiveData<DataEvent<SeasonRound>>
    val yearList: LiveData<List<Selected<String>>>
    val trackList: LiveData<List<Selected<TrackModel>>>
}

//endregion

class TrackPickerViewModel(
    historyDB: HistoryDB
) : BaseViewModel(), TrackPickerViewModelInputs, TrackPickerViewModelOutputs {

    private var pickSeason: ConflatedBroadcastChannel<Int> = ConflatedBroadcastChannel()
    private var seasonRound: ConflatedBroadcastChannel<SeasonRound> = ConflatedBroadcastChannel()
    private var selectedEvent: BroadcastChannel<SeasonRound> = BroadcastChannel(1)

    override val yearList: LiveData<List<Selected<String>>> = pickSeason
        .asFlow()
        .map { season ->
            supportedYears
                .map { Selected(it.toString(), it == season) }
                .sortedByDescending { it.value }
        }
        .asLiveData(viewModelScope.coroutineContext)

    override val trackList: LiveData<List<Selected<TrackModel>>> = historyDB
        .allHistory()
        .combinePair(pickSeason.asFlow())
        .map { (all, season) -> all.firstOrNull { it.season == season } }
        .combinePair(seasonRound.asFlow())
        .map { (season, currentSeasonRound) ->
            if (season == null) {
                return@map emptyList<Selected<TrackModel>>()
            }
            season.rounds
                .map { race ->
                    Selected(
                        TrackModel(
                            race.season,
                            race.round,
                            race.circuitName,
                            race.country,
                            race.countryISO
                        ),
                        currentSeasonRound.first == race.season && currentSeasonRound.second == race.round
                    )
                }
                .sortedBy { it.value.round }
        }
        .asLiveData(viewModelScope.coroutineContext)

    override val selected: LiveData<DataEvent<SeasonRound>> = selectedEvent
        .asFlow()
        .toDataEvent()
        .asLiveData(viewModelScope.coroutineContext)

    var inputs: TrackPickerViewModelInputs = this
    var outputs: TrackPickerViewModelOutputs = this

    //region Inputs

    override fun initialSeasonRound(season: Int, round: Int) {

        pickSeason.offer(season)
        seasonRound.offer(SeasonRound(season, round))
    }

    override fun clickSeason(season: Int) {

        pickSeason.offer(season)
    }

    override fun clickRound(round: Int) {

        pickSeason.valueOrNull?.let {
            selectedEvent.offer(SeasonRound(it, round))
        }
    }

    //endregion
}