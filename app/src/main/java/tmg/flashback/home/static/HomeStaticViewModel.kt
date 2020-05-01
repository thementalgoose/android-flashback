package tmg.flashback.home.static

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import tmg.flashback.base.BaseViewModel
import tmg.flashback.dateFormatter
import tmg.flashback.home.trackpicker.TrackModel
import tmg.flashback.repo.db.DataDB
import tmg.flashback.repo.db.HistoryDB
import tmg.flashback.repo.db.PrefsDB
import tmg.flashback.repo.db.SeasonOverviewDB
import tmg.flashback.repo.models.*
import tmg.flashback.race.RaceAdapterType
import tmg.flashback.race.RaceAdapterModel
import tmg.flashback.race.ShowQualifying
import tmg.flashback.utils.SeasonRound
import tmg.utilities.extensions.combineTriple
import tmg.utilities.extensions.then
import tmg.utilities.lifecycle.DataEvent
import tmg.utilities.lifecycle.Event

//region Inputs

interface HomeStaticViewModelInputs {
//    fun select(season: Int, round: Int)
//    fun orderBy(seasonRaceAdapterType: RaceAdapterType)
//    fun clickTrackList()
//    fun closeTrackList()
}

//endregion

//region Outputs

interface HomeStaticViewModelOutputs {
//    val circuitInfo: LiveData<TrackModel>
//    val date: LiveData<String>
//
//    val items: LiveData<Triple<RaceAdapterType, List<RaceAdapterModel>, SeasonRound>>
//
//    val homeScreenState: LiveData<HomeStaticScreenState>
}

//endregion

class HomeStaticViewModel(
    historyDB: HistoryDB,
    seasonOverviewDB: SeasonOverviewDB,
    private val prefsDB: PrefsDB,
    dataDB: DataDB
) : BaseViewModel(), HomeStaticViewModelInputs, HomeStaticViewModelOutputs {

//    private val seasonRound: ConflatedBroadcastChannel<SeasonRound> = ConflatedBroadcastChannel()
//
//    private var viewType: ConflatedBroadcastChannel<RaceAdapterType> =
//        ConflatedBroadcastChannel(RaceAdapterType.RACE)
//    private val homeScreenStateEvent: ConflatedBroadcastChannel<HomeStaticScreenState> =
//        ConflatedBroadcastChannel(HomeStaticScreenState.LOADING)
//
//    /**
//     * Getting the round based on the selection
//     */
//    private val roundValue: ConflatedBroadcastChannel<Round?> = ConflatedBroadcastChannel()
//
//    /**
//     * Flow to Represent the currently selected history round model
//     */
//    private val selectionFlow: Flow<HistoryRound?> = historyDB.allHistory()
//        .combine(seasonRound.asFlow()) { list, seasonRound ->
//
//            if (list.isEmpty()) {
//                homeScreenStateEvent.offer(HomeStaticScreenState.ERROR)
//            }
//
//            val historyRound: HistoryRound? = list.firstOrNull { it.season == seasonRound.first }
//                ?.rounds
//                ?.firstOrNull { it.round == seasonRound.second }
//
//            if (list.isNotEmpty() && historyRound == null) {
//                homeScreenStateEvent.offer(HomeStaticScreenState.NOT_AVAILABLE)
//            } else if (historyRound?.hasResults == false) {
//                homeScreenStateEvent.offer(HomeStaticScreenState.NOT_AVAILABLE)
//            }
//            return@combine historyRound
//        }
//
//    //region Circuit info at top of the screen
//
//    override val circuitInfo: LiveData<TrackModel> = selectionFlow
//        .filterNotNull()
//        .map { round ->
//            TrackModel(
//                season = round.season,
//                round = round.round,
//                circuitName = round.circuitName,
//                country = round.country,
//                countryKey = round.countryISO
//            )
//        }
//        .flowOn(Dispatchers.IO)
//        .asLiveData(viewModelScope.coroutineContext)
//
//    override val date: LiveData<String> = roundValue
//        .asFlow()
//        .map { it?.date?.format(dateFormatter) ?: "" }
//        .asLiveData(viewModelScope.coroutineContext)
//
//    //endregion
//
//    //region Home Screen State
//
//    override val homeScreenState: LiveData<HomeStaticScreenState> = homeScreenStateEvent
//        .asFlow()
//        .asLiveData(viewModelScope.coroutineContext)
//
//    //endregion
//
//    //region Race items
//
//    override val items: LiveData<Triple<RaceAdapterType, List<RaceAdapterModel>, SeasonRound>> = roundValue
//        .asFlow()
//        .combineTriple(viewType.asFlow(), seasonRound.asFlow())
//        .filter { (roundData, _, sr) -> sr.first == roundData?.season && sr.second == roundData.round }
//        .map { (roundData, viewType, seasonRoundValue) ->
//            val driverIds: List<String> = getOrderedDriverIds(roundData, viewType)
//            if (roundData == null) {
//                return@map Triple(viewType, emptyList<RaceAdapterModel>(), seasonRoundValue)
//            }
//            val list: MutableList<RaceAdapterModel> = mutableListOf(
//                RaceAdapterModel.RacePlaceholder)
//            val showQualifying: ShowQualifying =
//                ShowQualifying(
//                    q1 = roundData.q1.count { it.value.time != null } > 0,
//                    q2 = roundData.q2.count { it.value.time != null } > 0,
//                    q3 = roundData.q3.count { it.value.time != null } > 0,
//                    deltas = prefsDB.showQualifyingDelta
//                )
//
//            when (viewType) {
//                RaceAdapterType.RACE -> {
//                    var startIndex = 0
//                    if (driverIds.size >= 3) {
//                        list.add(
//                            RaceAdapterModel.Podium(
//                                driverFirst = getDriverModel(roundData, driverIds[0], showQualifying),
//                                driverSecond = getDriverModel(roundData, driverIds[1], showQualifying),
//                                driverThird = getDriverModel(roundData, driverIds[2], showQualifying)
//                            )
//                        )
//                        startIndex = 3
//                        list.add(RaceAdapterModel.RaceHeader(roundData.season, roundData.round))
//                    }
//                    for (i in startIndex until driverIds.size) {
//                        list.add(getDriverModel(roundData, driverIds[i], showQualifying))
//                    }
//                }
//                RaceAdapterType.QUALIFYING_POS_1,
//                RaceAdapterType.QUALIFYING_POS_2,
//                RaceAdapterType.QUALIFYING_POS -> {
//                    list.add(RaceAdapterModel.QualifyingHeader(showQualifying))
//                    list.addAll(driverIds.mapIndexed { _, driverId -> getDriverModel(roundData, driverId, showQualifying) })
//                }
//            }
//            return@map Triple(viewType, list, SeasonRound(roundData.season, roundData.round))
//        }
//        .then {
//            if (it.second.isNotEmpty()) {
//                homeScreenStateEvent.offer(HomeStaticScreenState.DATA)
//            }
//        }
//        .asLiveData(viewModelScope.coroutineContext)
//
//    //endregion
//
//    var inputs: HomeStaticViewModelInputs = this
//    var outputs: HomeStaticViewModelOutputs = this
//
//    init {
//        viewModelScope.launch {
//            seasonRound
//                .asFlow()
//                .flatMapLatest { (season, round) -> seasonOverviewDB.getSeasonRound(season, round) }
//                .flowOn(Dispatchers.IO)
//                .collect {
//                    roundValue.send(it)
//                }
//        }
//
//        select(prefsDB.selectedYear, 1)
//    }
//
//    //region Inputs
//
//    override fun orderBy(seasonRaceAdapterType: RaceAdapterType) {
//
//        viewType.offer(seasonRaceAdapterType)
//    }
//
//    override fun select(season: Int, round: Int) {
//
//        homeScreenStateEvent.offer(HomeStaticScreenState.LOADING)
//        seasonRound.offer(SeasonRound(season, round))
//    }
//
//    override fun clickTrackList() {
//
//        openTrackEvent.offer(Unit)
//    }
//
//    override fun closeTrackList() {
//
//        closedTrackEvent.offer(Unit)
//    }
//
//    //endregion
//
//    /**
//     * Get a list of ordered driver Ids based on the view type
//     */
//    private fun getOrderedDriverIds(roundData: Round?, viewType: RaceAdapterType): List<String> {
//        if (roundData == null) {
//            return emptyList()
//        }
//        return roundData
//            .race
//            .values
//            .sortedBy {
//                val driverOverview: RoundDriverOverview = roundData.driverOverview(it.driver.id)
//                when (viewType) {
//                    RaceAdapterType.RACE -> it.finish
//                    RaceAdapterType.QUALIFYING_POS_1 -> {
//                        return@sortedBy driverOverview.q1?.position ?: driverOverview.q2?.position ?: driverOverview.race.qualified ?: driverOverview.race.grid
//                    }
//                    RaceAdapterType.QUALIFYING_POS_2 -> {
//                        return@sortedBy driverOverview.q2?.position ?: driverOverview.race.qualified ?: driverOverview.race.grid
//                    }
//                    RaceAdapterType.QUALIFYING_POS -> driverOverview.race.qualified ?: driverOverview.race.grid
//                }
//            }
//            .map { it.driver.id }
//    }
//
//    /**
//     * Get a [RaceAdapterModel.Single] instance for a given driver
//     */
//    private fun getDriverModel(round: Round, driverId: String, showQualifying: ShowQualifying): RaceAdapterModel.Single {
//        val overview = round.driverOverview(driverId)
//        return RaceAdapterModel.Single(
//            season = round.season,
//            round = round.round,
//            driver = round.drivers.first { it.id == driverId },
//            q1 = overview.q1,
//            q2 = overview.q2,
//            q3 = overview.q3,
//            raceResult = overview.race.time ?: LapTime(),
//            racePos = overview.race.finish,
//            gridPos = overview.race.grid,
//            qualified = overview.race.qualified,
//            racePoints = overview.race.points,
//            status = overview.race.status,
//            fastestLap = overview.race.fastestLap?.rank == 1,
//            q1Delta = if (prefsDB.showQualifyingDelta) round.q1FastestLap?.deltaTo(overview.q1?.time) else null,
//            q2Delta = if (prefsDB.showQualifyingDelta) round.q2FastestLap?.deltaTo(overview.q2?.time) else null,
//            q3Delta = if (prefsDB.showQualifyingDelta) round.q3FastestLap?.deltaTo(overview.q3?.time) else null,
//            showQualifying = showQualifying
//        )
//    }
}