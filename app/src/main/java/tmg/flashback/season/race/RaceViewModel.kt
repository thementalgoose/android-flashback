package tmg.flashback.season.race

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import tmg.flashback.base.BaseViewModel
import tmg.flashback.extensions.combinePair
import tmg.flashback.extensions.then
import tmg.flashback.repo.db.PrefsDB
import tmg.flashback.repo.db.SeasonOverviewDB
import tmg.flashback.repo.models.*
import tmg.flashback.repo.utils.toMaxIfZero
import tmg.flashback.utils.DataEvent
import tmg.flashback.utils.SeasonRound
import tmg.flashback.utils.localLog

//region Inputs

interface RaceViewModelInputs {
    fun initialise(season: Int, round: Int)
    fun orderBy(seasonRaceAdapterType: RaceAdapterType)
}

//endregion

//region Outputs

interface RaceViewModelOutputs {
    val items: LiveData<Pair<RaceAdapterType, List<RaceModel>>>
    val date: LiveData<LocalDate>
    val time: LiveData<LocalTime>

    val loading: MutableLiveData<Boolean>
}

//endregion

class RaceViewModel(
    private val seasonOverviewDB: SeasonOverviewDB,
    private val prefsDB: PrefsDB
) : BaseViewModel(), RaceViewModelInputs, RaceViewModelOutputs {

    private val seasonRound: ConflatedBroadcastChannel<SeasonRound> = ConflatedBroadcastChannel()
    private var viewType: ConflatedBroadcastChannel<RaceAdapterType> = ConflatedBroadcastChannel()

    override val loading: MutableLiveData<Boolean> = MutableLiveData()

    private val roundFlow: Flow<Round> = seasonRound
        .asFlow()
        .flatMapLatest { (season, round) -> seasonOverviewDB.getSeasonRound(season, round) }
        .filter { it != null }
        .map { it!! }
        .flowOn(Dispatchers.IO)

    override val items: LiveData<Pair<RaceAdapterType, List<RaceModel>>> = roundFlow
        .combinePair(viewType.asFlow())
        .map { (roundData, viewType) ->
            localLog("Combining view type with race model data")
            val driverIds: List<String> = roundData
                .race
                .values
                .sortedBy {
                    val driverOverview: RoundDriverOverview = roundData.driverOverview(it.driver.id)
                    when (viewType) {
                        RaceAdapterType.RACE -> it.finish
                        RaceAdapterType.QUALIFYING_POS_1 -> driverOverview.q1?.position
                        RaceAdapterType.QUALIFYING_POS_2 -> driverOverview.q2?.position
                        RaceAdapterType.QUALIFYING_POS -> it.qualified
                    }
                }
                .map { it.driver.id }
            val list: MutableList<RaceModel> = mutableListOf()
            when (viewType) {
                RaceAdapterType.RACE -> {
                    var startIndex = 0
                    if (driverIds.size >= 3) {
                        list.add(
                            RaceModel.Podium(
                                driverFirst = getDriverModel(roundData, driverIds[0]),
                                driverSecond = getDriverModel(roundData, driverIds[1]),
                                driverThird = getDriverModel(roundData, driverIds[2])
                            )
                        )
                        startIndex = 3
                    }
                    for (i in startIndex until driverIds.size) {
                        list.add(getDriverModel(roundData, driverIds[i]))
                    }
                }
                RaceAdapterType.QUALIFYING_POS_1,
                RaceAdapterType.QUALIFYING_POS_2,
                RaceAdapterType.QUALIFYING_POS -> {
                    list.add(RaceModel.QualifyingHeader)
                    list.addAll(driverIds.map { getDriverModel(roundData, it) })
                }
            }

            localLog("List constructed")
            return@map Pair(viewType, list)
        }
        .then {
            loading.postValue(false)
        }
        .asLiveData(viewModelScope.coroutineContext)

    override val date: LiveData<LocalDate> = roundFlow
        .map { it.date }
        .asLiveData(viewModelScope.coroutineContext)

    override val time: LiveData<LocalTime> = roundFlow
        .map { it.time }
        .asLiveData(viewModelScope.coroutineContext)

    var inputs: RaceViewModelInputs = this
    var outputs: RaceViewModelOutputs = this

    init {
        viewType.offer(RaceAdapterType.RACE)
    }

    //region Inputs

    override fun initialise(season: Int, round: Int) {

        loading.value = true
        seasonRound.offer(SeasonRound(season, round))
    }

    override fun orderBy(seasonRaceAdapterType: RaceAdapterType) {

        viewType.offer(seasonRaceAdapterType)
    }

    //endregion

    private fun getDriverModel(round: Round, driverId: String): RaceModel.Single {
        val overview = round.driverOverview(driverId)
        return RaceModel.Single(
            season = round.season,
            round = round.round,
            driver = round.drivers.first { it.id == driverId },
            q1 = overview.q1,
            q2 = overview.q2,
            q3 = overview.q3,
            raceResult = overview.race.time ?: LapTime(),
            racePos = overview.race.finish,
            gridPos = overview.race.grid,
            qualified = overview.race.qualified,
            racePoints = overview.race.points,
            status = overview.race.status,
            fastestLap = overview.race.fastestLap?.rank == 1,
            q1Delta = if (prefsDB.showQualifyingDelta) round.q1.getTopLapTime()?.deltaTo(overview.q1?.time) else null,
            q2Delta = if (prefsDB.showQualifyingDelta) round.q2.getTopLapTime()?.deltaTo(overview.q2?.time) else null,
            q3Delta = if (prefsDB.showQualifyingDelta) round.q3.getTopLapTime()?.deltaTo(overview.q3?.time) else null
        )
    }

    private fun Map<String, RoundQualifyingResult>.getTopLapTime(): LapTime? {
        return this
            .toSortedMap()
            .toList()
            .minBy { it.second.position }!!
            .second
            .time
    }
}