package tmg.flashback.statistics.ui.race

import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import tmg.core.device.managers.NetworkConnectivityManager
import tmg.flashback.statistics.controllers.RaceController
import tmg.core.ui.controllers.ThemeController
import tmg.flashback.formula1.constants.Formula1.showComingSoonMessageForNextDays
import tmg.flashback.formula1.model.*
import tmg.flashback.statistics.repo.RaceRepository
import tmg.flashback.statistics.ui.shared.sync.SyncDataItem
import tmg.flashback.statistics.ui.shared.sync.viewholders.DataUnavailable
import tmg.flashback.statistics.ui.util.SeasonRound
import tmg.utilities.extensions.combinePair
import tmg.utilities.lifecycle.DataEvent
import java.lang.NullPointerException
import java.util.*

//region Inputs

interface RaceViewModelInputs {
    fun initialise(season: Int, round: Int)
    fun orderBy(seasonRaceAdapterType: RaceAdapterType)
    fun goToDriver(driverId: String, driverName: String)
    fun goToConstructor(constructorId: String, constructorName: String)
    fun toggleQualifyingDelta(toNewState: Boolean)

    fun refresh()
}

//endregion

//region Outputs

interface RaceViewModelOutputs {
    val raceItems: LiveData<Pair<RaceAdapterType, List<RaceModel>>>
    val goToDriverOverview: LiveData<DataEvent<Pair<String, String>>>
    val goToConstructorOverview: LiveData<DataEvent<Pair<String, String>>>

    val showLoading: LiveData<Boolean>

    val showSprintQualifying: LiveData<Boolean>
}

//endregion

class RaceViewModel(
    private val raceRepository: RaceRepository,
    private val raceController: RaceController,
    private val themeController: ThemeController,
    private val networkConnectivityManager: NetworkConnectivityManager,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel(), RaceViewModelInputs, RaceViewModelOutputs {

    var inputs: RaceViewModelInputs = this
    var outputs: RaceViewModelOutputs = this

    private var toggleQualifyingDelta: Boolean? = null

    override val showLoading: MutableLiveData<Boolean> = MutableLiveData()

    private val seasonRound: MutableStateFlow<SeasonRound?> = MutableStateFlow(null)
    private val seasonRoundWithRequest: Flow<SeasonRound?> = seasonRound
        .filterNotNull()
        .flatMapLatest { (season, round) ->
            return@flatMapLatest flow {
                if (raceRepository.shouldSyncRace(season, round)) {
                    showLoading.postValue(true)
                    emit(null)
                    val result = raceRepository.fetchRaces(season)
                    showLoading.postValue(false)
                    emit(SeasonRound(season, round))
                }
                else {
                    emit(SeasonRound(season, round))
                }
            }
        }
        .flowOn(ioDispatcher)

    private val isConnected: Boolean
        get() = networkConnectivityManager.isConnected

    private val viewType: MutableStateFlow<RaceAdapterType> = MutableStateFlow(RaceAdapterType.RACE)
    private val viewTypeRefresh: MutableStateFlow<String> = MutableStateFlow(UUID.randomUUID().toString())

    private val viewTypeFlow: Flow<RaceAdapterType> = viewType.combinePair(viewTypeRefresh).map { it.first }

    override val goToDriverOverview: MutableLiveData<DataEvent<Pair<String, String>>> = MutableLiveData()
    override val goToConstructorOverview: MutableLiveData<DataEvent<Pair<String, String>>> = MutableLiveData()
    override val showSprintQualifying: MutableLiveData<Boolean> = MutableLiveData(false)

    override val raceItems: LiveData<Pair<RaceAdapterType, List<RaceModel>>> = seasonRoundWithRequest
            .combinePair(viewTypeFlow)
            .flatMapLatest { (seasonRound, viewType) ->
                if (seasonRound == null) {
                    return@flatMapLatest flow {
                        emit(Pair(viewType, listOf<RaceModel>(RaceModel.ErrorItem(SyncDataItem.Skeleton))))
                    }
                }

                val (season, round) = seasonRound
                return@flatMapLatest raceRepository.getRace(season, round)
                    .map { race ->
                        val list = mutableListOf<RaceModel>()
                        when {
                            race == null && !isConnected ->
                                list.addError(SyncDataItem.PullRefresh)
                            race == null ->
                                list.addError(SyncDataItem.Unavailable(DataUnavailable.RACE_DATA_EMPTY))
                            !race.hasData && race.raceInfo.date > LocalDate.now() ->
                                list.addError(SyncDataItem.Unavailable(DataUnavailable.RACE_IN_FUTURE))
                            !race.hasData && race.raceInfo.date <= LocalDate.now() && race.raceInfo.date.isWithin(showComingSoonMessageForNextDays) ->
                                list.addError(SyncDataItem.Unavailable(DataUnavailable.COMING_SOON_RACE))
                            !race.hasData ->
                                list.addError(SyncDataItem.Unavailable(DataUnavailable.RACE_DATA_EMPTY))
                            else -> {
                                list.add(getRaceOverview(race))

                                val driverIds: List<String> = getOrderedDriverIds(race, viewType)
                                val showQualifying = DisplayPrefs(
                                    q1 = race.q1.count { it.value.time != null } > 0,
                                    q2 = race.q2.count { it.value.time != null } > 0,
                                    q3 = race.q3.count { it.value.time != null } > 0,
                                    deltas = toggleQualifyingDelta ?: raceController.showQualifyingDelta,
                                    penalties = raceController.showGridPenaltiesInQualifying,
                                    fadeDNF = raceController.fadeDNF
                                )

                                when (viewType) {
                                    RaceAdapterType.CONSTRUCTOR_STANDINGS -> {
                                        list.addAll(race
                                            .constructorStandings
                                            .map {
                                                val drivers: List<Pair<Driver, Double>> = getDriverFromConstructor(race, it.constructor.id)
                                                RaceModel.ConstructorStandings(
                                                    it.constructor,
                                                    it.points,
                                                    drivers,
                                                    themeController.animationSpeed
                                                )
                                            }
                                            .sortedByDescending {
                                                it.points
                                            })

                                        return@map Pair(
                                            viewType,
                                            list
                                        )
                                    }
                                    RaceAdapterType.RACE -> {
                                        if (race.race.isNotEmpty()) {
                                            var startIndex = 0
                                            if (driverIds.size >= 3) {
                                                list.add(RaceModel.Podium(
                                                    driverFirst = getDriverModel(race, viewType, driverIds[0], showQualifying),
                                                    driverSecond = getDriverModel(race, viewType, driverIds[1], showQualifying),
                                                    driverThird = getDriverModel(race, viewType, driverIds[2], showQualifying)
                                                ))
                                                startIndex = 3
                                                list.add(RaceModel.RaceHeader(race.raceInfo.season, race.raceInfo.round))
                                            }
                                            for (i in startIndex until driverIds.size) {
                                                list.add(
                                                    getDriverModel(race, viewType, driverIds[i], showQualifying)
                                                )
                                            }
                                        }
                                        else {
                                            when {
                                                race.raceInfo.date > LocalDate.now() -> list.add(RaceModel.ErrorItem(SyncDataItem.Unavailable(DataUnavailable.RACE_IN_FUTURE)))
                                                else -> list.add(RaceModel.ErrorItem(SyncDataItem.Unavailable(DataUnavailable.COMING_SOON_RACE)))
                                            }
                                        }
                                    }
                                    RaceAdapterType.QUALIFYING_SPRINT -> {
                                        list.add(RaceModel.RaceHeader(race.raceInfo.season, race.raceInfo.round))
                                        list.addAll(driverIds.mapIndexed { _, driverId ->
                                            getDriverModel(race, viewType, driverId, showQualifying)
                                        })
                                    }
                                    RaceAdapterType.QUALIFYING_POS_1,
                                    RaceAdapterType.QUALIFYING_POS_2,
                                    RaceAdapterType.QUALIFYING_POS -> {
                                        list.add(RaceModel.QualifyingHeader(showQualifying))
                                        list.addAll(driverIds.mapIndexed { _, driverId ->
                                            getDriverModel(race, viewType, driverId, showQualifying)
                                        })
                                    }
                                    else -> throw Error("Unsupported view type")
                                }
                            }
                        }
                        return@map Pair(viewType, list.toList())
                    }
            }
            .asLiveData(viewModelScope.coroutineContext)

    //region Inputs

    override fun initialise(season: Int, round: Int) {
        this.seasonRound.value = SeasonRound(season, round)
    }

    override fun orderBy(seasonRaceAdapterType: RaceAdapterType) {
        viewType.value = seasonRaceAdapterType
    }

    override fun goToDriver(driverId: String, driverName: String) {
        goToDriverOverview.value = DataEvent(Pair(driverId, driverName))
    }

    override fun goToConstructor(constructorId: String, constructorName: String) {
        goToConstructorOverview.value = DataEvent(Pair(constructorId, constructorName))
    }

    override fun toggleQualifyingDelta(toNewState: Boolean) {
        toggleQualifyingDelta = toNewState
        viewTypeRefresh.value = UUID.randomUUID().toString()
    }

    override fun refresh() {
        this.refresh(this.seasonRound.value)
    }
    private fun refresh(seasonRound: SeasonRound? = this.seasonRound.value) {
        viewModelScope.launch(context = ioDispatcher) {
            seasonRound?.let { (season, _) ->
                val result = raceRepository.fetchRaces(season)
                showLoading.postValue(false)
            }
        }
    }

    //endregion

    private fun LocalDate.isWithin(days: Int) = this >= LocalDate.now().minusDays(days.toLong())

    private fun getDriverFromConstructor(race: Race, constructorId: String): List<Pair<Driver, Double>> {
        return race.race.values
            .mapNotNull { raceResult ->
                if (raceResult.driver.constructor.id != constructorId) return@mapNotNull null
                return@mapNotNull Pair(raceResult.driver.driver, raceResult.points)
            }
            .sortedByDescending { it.second }
    }

    private fun getRaceOverview(race: Race): RaceModel.Overview {
        return RaceModel.Overview(
            raceName = race.raceInfo.name,
            country = race.raceInfo.circuit.country,
            countryISO = race.raceInfo.circuit.countryISO,
            circuitId = race.raceInfo.circuit.id,
            circuitName = race.raceInfo.circuit.name,
            round = race.raceInfo.round,
            season = race.raceInfo.season,
            raceDate = race.raceInfo.date,
            wikipedia = race.raceInfo.wikipediaUrl
        )
    }

    /**
     * Get a list of ordered driver Ids based on the view type
     */
    private fun getOrderedDriverIds(raceData: Race?, viewType: RaceAdapterType): List<String> {
        if (raceData == null) {
            return emptyList()
        }
        if (raceData.race.isNotEmpty()) {
            return raceData.race.values
                .sortedBy {
                    val driverOverview: RaceDriverOverview = raceData.driverOverview(it.driver.driver.id) ?: return@sortedBy Int.MAX_VALUE

                    if (viewType.isQualifying() &&
                        driverOverview.q1?.position == null &&
                        driverOverview.q2?.position == null &&
                        driverOverview.race?.qualified == null &&
                        (driverOverview.race?.grid == 0 || driverOverview.race?.grid == null)
                    ) {
                        return@sortedBy Int.MAX_VALUE
                    }

                    return@sortedBy when (viewType) {
                        RaceAdapterType.QUALIFYING_POS_1 -> driverOverview.q1?.position
                            ?: driverOverview.q2?.position
                            ?: driverOverview.race?.qualified
                            ?: driverOverview.race?.grid ?: Int.MAX_VALUE
                        RaceAdapterType.QUALIFYING_POS_2 -> driverOverview.q2?.position
                            ?: driverOverview.race?.qualified
                            ?: driverOverview.race?.grid ?: Int.MAX_VALUE
                        RaceAdapterType.QUALIFYING_POS -> driverOverview.race?.qualified
                            ?: driverOverview.race?.grid ?: Int.MAX_VALUE
                        RaceAdapterType.QUALIFYING_SPRINT -> driverOverview.qSprint?.finish
                        else -> it.finish
                    }
                }
                .map { it.driver.driver.id }
        }
        else {
            return raceData
                .drivers
                .sortedBy {

                    val q1 = raceData.q1[it.driver.id]
                    val q2 = raceData.q2[it.driver.id]
                    val q3 = raceData.q3[it.driver.id]

                    return@sortedBy when (viewType) {
                        RaceAdapterType.QUALIFYING_SPRINT -> raceData.qSprint[it.driver.id]?.finish
                        RaceAdapterType.QUALIFYING_POS_1 -> q1?.position
                        RaceAdapterType.QUALIFYING_POS_2 -> q2?.position ?: q1?.position
                        else -> q3?.position ?: q2?.position ?: q1?.position
                    }
                }
                .map { it.driver.id }
        }
    }

    /**
     * Get a [RaceModel.Single] instance for a given driver
     */
    private fun getDriverModel(
        race: Race,
        @Suppress("UNUSED_PARAMETER")
        viewType: RaceAdapterType,
        driverId: String,
        displayPrefs: DisplayPrefs
    ): RaceModel.Single {
        val overview = race.driverOverview(driverId) ?: throw NullPointerException("Driver id $driverId was requested when displaying ${race.raceInfo.season} / ${race.raceInfo.round}")
        val singleRace = overview.race?.let {
            SingleRace(
                points = it.points,
                result = it.time ?: LapTime(),
                pos = it.finish,
                gridPos = it.grid,
                status = it.status,
                fastestLap = it.fastestLap?.rank == 1
            )
        }
        return RaceModel.Single(
            season = race.raceInfo.season,
            round = race.raceInfo.round,
            driver = overview.driver,
            q1 = overview.q1,
            q2 = overview.q2,
            q3 = overview.q3,
            qSprint = overview.qSprint,
            race = singleRace,
            qualified = overview.race?.qualified ?: race.getQualifyingOnlyPosByDriverId(driverId),
            q1Delta = if (toggleQualifyingDelta ?: raceController.showQualifyingDelta) race.q1FastestLap?.deltaTo(overview.q1?.time) else null,
            q2Delta = if (toggleQualifyingDelta ?: raceController.showQualifyingDelta) race.q2FastestLap?.deltaTo(overview.q2?.time) else null,
            q3Delta = if (toggleQualifyingDelta ?: raceController.showQualifyingDelta) race.q3FastestLap?.deltaTo(overview.q3?.time) else null,
            displayPrefs = displayPrefs
        )
    }

    private fun Race.getQualifyingOnlyPosByDriverId(driverId: String): Int? {

        val q1 = this.q1[driverId]
        val q2 = this.q2[driverId]
        val q3 = this.q3[driverId]

        return q3?.position ?: q2?.position ?: q1?.position
    }

    private fun  MutableList<RaceModel>.addError(syncDataItem: SyncDataItem) {
        this.add(
            RaceModel.ErrorItem(
                syncDataItem
            )
        )
    }
}