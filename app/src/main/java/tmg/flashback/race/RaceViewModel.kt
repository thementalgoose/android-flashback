package tmg.flashback.race

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import tmg.flashback.base.BaseViewModel
import tmg.flashback.race.RaceDisplayMode.*
import tmg.flashback.repo.db.PrefsDB
import tmg.flashback.repo.db.SeasonOverviewDB
import tmg.flashback.repo.models.*
import tmg.flashback.settings.ConnectivityManager
import tmg.flashback.utils.SeasonRound
import tmg.utilities.extensions.combineTriple
import tmg.utilities.extensions.then

//region Inputs

interface RaceViewModelInputs {
    fun initialise(season: Int, round: Int)
    fun orderBy(seasonRaceAdapterType: RaceAdapterType)
}

//endregion

//region Outputs

interface RaceViewModelOutputs {
    val circuitInfo: LiveData<Round>
    val raceItems: LiveData<Triple<RaceAdapterType, List<RaceAdapterModel>, SeasonRound>>
    val seasonRoundData: LiveData<SeasonRound>
    val raceDisplayMode: MutableLiveData<RaceDisplayMode>
}

//endregion

class RaceViewModel(
    seasonOverviewDB: SeasonOverviewDB,
    private val prefsDB: PrefsDB,
    connectivityManager: ConnectivityManager
) : BaseViewModel(), RaceViewModelInputs, RaceViewModelOutputs {

    var inputs: RaceViewModelInputs = this
    var outputs: RaceViewModelOutputs = this

    private val seasonRound: ConflatedBroadcastChannel<SeasonRound> = ConflatedBroadcastChannel()
    private var viewType: ConflatedBroadcastChannel<RaceAdapterType> = ConflatedBroadcastChannel()

    private val seasonRoundFlow: Flow<Round?> = seasonRound
        .asFlow()
        .flatMapLatest { (season, round) -> seasonOverviewDB.getSeasonRound(season, round) }

    override val seasonRoundData: LiveData<SeasonRound> = seasonRound
        .asFlow()
        .asLiveData()

    override val circuitInfo: LiveData<Round> = seasonRoundFlow
        .then {
            if (it == null) {
                raceDisplayMode.postValue(NOT_FOUND)
            }
        }
        .filterNotNull()
        .flowOn(Dispatchers.IO)
        .asLiveData(viewModelScope.coroutineContext)

    override val raceItems: LiveData<Triple<RaceAdapterType, List<RaceAdapterModel>, SeasonRound>> =
        seasonRound
            .asFlow()
            .flatMapLatest { (season, round) -> seasonOverviewDB.getSeasonRound(season, round) }
            .combineTriple(viewType.asFlow(), seasonRound.asFlow())
            .filter { (roundData, _, sr) -> sr.first == roundData?.season && sr.second == roundData.round }
            .map { (roundData, viewType, seasonRoundValue) ->
                if (roundData == null) {
                    return@map Triple(viewType, emptyList<RaceAdapterModel>(), seasonRoundValue)
                }

                if (viewType == RaceAdapterType.CONSTRUCTOR_STANDINGS) {
                    val list: List<RaceAdapterModel.ConstructorStandings> = roundData
                        .constructorStandings
                        .map {
                            RaceAdapterModel.ConstructorStandings(it.constructor, it.points, getDriverFromConstructor(roundData, it.constructor.id))
                        }
                        .sortedByDescending { it.points }

                    return@map Triple(
                        viewType,
                        list,
                        SeasonRound(roundData.season, roundData.round)
                    )
                } else {
                    val driverIds: List<String> = getOrderedDriverIds(roundData, viewType)
                    val list: MutableList<RaceAdapterModel> = mutableListOf()
                    val showQualifying = ShowQualifying(
                        q1 = roundData.q1.count { it.value.time != null } > 0,
                        q2 = roundData.q2.count { it.value.time != null } > 0,
                        q3 = roundData.q3.count { it.value.time != null } > 0,
                        deltas = prefsDB.showQualifyingDelta
                    )

                    when (viewType) {
                        RaceAdapterType.RACE -> {
                            var startIndex = 0
                            if (driverIds.size >= 3) {
                                list.add(
                                    RaceAdapterModel.Podium(
                                        driverFirst = getDriverModel(
                                            roundData,
                                            driverIds[0],
                                            showQualifying
                                        ),
                                        driverSecond = getDriverModel(
                                            roundData,
                                            driverIds[1],
                                            showQualifying
                                        ),
                                        driverThird = getDriverModel(
                                            roundData,
                                            driverIds[2],
                                            showQualifying
                                        )
                                    )
                                )
                                startIndex = 3
                                list.add(
                                    RaceAdapterModel.RaceHeader(
                                        roundData.season,
                                        roundData.round
                                    )
                                )
                            }
                            for (i in startIndex until driverIds.size) {
                                list.add(getDriverModel(roundData, driverIds[i], showQualifying))
                            }
                        }
                        RaceAdapterType.QUALIFYING_POS_1,
                        RaceAdapterType.QUALIFYING_POS_2,
                        RaceAdapterType.QUALIFYING_POS -> {
                            list.add(RaceAdapterModel.QualifyingHeader(showQualifying))
                            list.addAll(driverIds.mapIndexed { _, driverId ->
                                getDriverModel(
                                    roundData,
                                    driverId,
                                    showQualifying
                                )
                            })
                        }
                        else -> throw Error("Unsupported view type")
                    }
                    Log.i("Flashback", "Returning triple")
                    return@map Triple(
                        viewType,
                        list,
                        SeasonRound(roundData.season, roundData.round)
                    )
                }
            }
            .then {
                raceDisplayMode.postValue(LIST)
            }
            .asLiveData(viewModelScope.coroutineContext)

    override val raceDisplayMode: MutableLiveData<RaceDisplayMode> =
        MutableLiveData(if (connectivityManager.isConnected) LOADING else NO_NETWORK)

    //region Inputs

    override fun initialise(season: Int, round: Int) {
        seasonRound.offer(SeasonRound(season, round))
    }

    override fun orderBy(seasonRaceAdapterType: RaceAdapterType) {
        viewType.offer(seasonRaceAdapterType)
    }

    //endregion

    private fun getDriverFromConstructor(round: Round, constructorId: String): List<Pair<RoundDriver, Int>> {
        return round
            .drivers
            .filter { it.constructor.id == constructorId }
            .map { Pair(it, round.race[it.id]?.points ?: 0)}
    }

    /**
     * Get a list of ordered driver Ids based on the view type
     */
    private fun getOrderedDriverIds(roundData: Round?, viewType: RaceAdapterType): List<String> {
        if (roundData == null) {
            return emptyList()
        }
        return roundData
            .race
            .values
            .sortedBy {
                val driverOverview: RoundDriverOverview = roundData.driverOverview(it.driver.id)
                when (viewType) {
                    RaceAdapterType.QUALIFYING_POS_1 -> {
                        return@sortedBy driverOverview.q1?.position ?: driverOverview.q2?.position
                        ?: driverOverview.race.qualified ?: driverOverview.race.grid
                    }
                    RaceAdapterType.QUALIFYING_POS_2 -> {
                        return@sortedBy driverOverview.q2?.position ?: driverOverview.race.qualified
                        ?: driverOverview.race.grid
                    }
                    RaceAdapterType.QUALIFYING_POS -> driverOverview.race.qualified
                        ?: driverOverview.race.grid
                    else -> it.finish
                }
            }
            .map { it.driver.id }
    }

    /**
     * Get a [RaceAdapterModel.Single] instance for a given driver
     */
    private fun getDriverModel(
        round: Round,
        driverId: String,
        showQualifying: ShowQualifying
    ): RaceAdapterModel.Single {
        val overview = round.driverOverview(driverId)
        return RaceAdapterModel.Single(
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
            q1Delta = if (prefsDB.showQualifyingDelta) round.q1FastestLap?.deltaTo(overview.q1?.time) else null,
            q2Delta = if (prefsDB.showQualifyingDelta) round.q2FastestLap?.deltaTo(overview.q2?.time) else null,
            q3Delta = if (prefsDB.showQualifyingDelta) round.q3FastestLap?.deltaTo(overview.q3?.time) else null,
            showQualifying = showQualifying
        )
    }
}