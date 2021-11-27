package tmg.flashback.statistics.ui.race

import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import tmg.flashback.device.managers.NetworkConnectivityManager
import tmg.flashback.formula1.constants.Formula1
import tmg.flashback.formula1.model.*
import tmg.flashback.formula1.model.RaceQualifyingType.*
import tmg.flashback.statistics.R
import tmg.flashback.statistics.controllers.RaceController
import tmg.flashback.statistics.repo.RaceRepository
import tmg.flashback.statistics.ui.shared.sync.SyncDataItem
import tmg.flashback.statistics.ui.shared.sync.viewholders.DataUnavailable
import tmg.flashback.statistics.ui.util.SeasonRound
import tmg.flashback.ui.controllers.ThemeController
import tmg.utilities.extensions.combinePair
import tmg.utilities.lifecycle.DataEvent

//region Inputs

interface RaceViewModelInputs {
    fun refresh()

    fun initialise(season: Int, round: Int)
    fun displayType(type: RaceDisplayType)

    fun clickConstructor(constructor: Constructor)
    fun clickDriver(driver: Driver)
}

//endregion

//region Outputs

interface RaceViewModelOutputs {
    val list: LiveData<List<RaceItem>>

    val showLoading: LiveData<Boolean>
    val showSprintQualifying: LiveData<Boolean>

    val goToConstructor: LiveData<DataEvent<Constructor>>
    val goToDriver: LiveData<DataEvent<Driver>>
}

//endregion

class RaceViewModel(
    private val raceRepository: RaceRepository,
    private val raceController: RaceController,
    private val themeController: ThemeController,
    private val networkConnectivityManager: NetworkConnectivityManager,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel(), RaceViewModelInputs, RaceViewModelOutputs {

    var inputs: RaceViewModelInputs = this
    var outputs: RaceViewModelOutputs = this

    override val showLoading: MutableLiveData<Boolean> = MutableLiveData()
    override val goToConstructor: MutableLiveData<DataEvent<Constructor>> = MutableLiveData()
    override val goToDriver: MutableLiveData<DataEvent<Driver>> = MutableLiveData()

    override val showSprintQualifying: MutableLiveData<Boolean> = MutableLiveData()

    private val isConnected: Boolean
        get() = networkConnectivityManager.isConnected

    private val displayType: MutableStateFlow<RaceDisplayType> = MutableStateFlow(RaceDisplayType.RACE)
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

    override val list: LiveData<List<RaceItem>> = seasonRoundWithRequest
        .combinePair(displayType)
        .flatMapLatest { (seasonRound, viewType) ->
            if (seasonRound == null) {
                return@flatMapLatest flow {
                    emit(listOf<RaceItem>(RaceItem.ErrorItem(SyncDataItem.Skeleton)))
                }
            }

            val (season, round) = seasonRound
            return@flatMapLatest raceRepository.getRace(season, round)
                .map { race ->
                    val list = mutableListOf<RaceItem>()
                    if (race != null) {
                        list.addAll(race.toOverview())
                    }
                    showSprintQualifying.postValue(race?.hasSprintQualifying == true)
                    when {
                        race == null && !isConnected ->
                            list.addError(SyncDataItem.PullRefresh)
                        race == null ->
                            list.addError(SyncDataItem.Unavailable(DataUnavailable.RACE_DATA_EMPTY))
                        !race.hasData && race.raceInfo.date > LocalDate.now() ->
                            list.addError(SyncDataItem.Unavailable(DataUnavailable.RACE_IN_FUTURE))
                        !race.hasData && race.raceInfo.date <= LocalDate.now() && race.raceInfo.date.isWithin(Formula1.showComingSoonMessageForNextDays) ->
                            list.addError(SyncDataItem.Unavailable(DataUnavailable.COMING_SOON_RACE))
                        !race.hasData ->
                            list.addError(SyncDataItem.Unavailable(DataUnavailable.RACE_DATA_EMPTY))
                        else -> {

                            race.qualifying.forEach {
                                println(":: ${it.label}")
                                it.results.forEach {
                                    println("  (${it.position}) --> ${it.driver.driver.id} ${it.lapTime}")
                                }
                            }

                            when (viewType) {
                                RaceDisplayType.CONSTRUCTOR -> {
                                    list.addAll(race
                                        .constructorStandings
                                        .map {
                                            val drivers: List<Pair<Driver, Double>> = getDriverFromConstructor(race, it.constructor.id)
                                            RaceItem.Constructor(it.constructor, it.points, drivers, themeController.animationSpeed, race.constructorStandings.maxByOrNull { it.points }?.points ?: 50.0)
                                        }
                                        .sortedByDescending {
                                            it.points
                                        })
                                }
                                RaceDisplayType.RACE -> {
                                    if (race.race.isNotEmpty()) {

                                        val results = race.race.sortedBy { it.finish }

                                        var startIndex = 0
                                        if (results.size >= 3) {
                                            list.add(RaceItem.Podium(
                                                driverFirst = results[0],
                                                driverSecond = results[1],
                                                driverThird = results[2]
                                            ))
                                            startIndex = 3
                                        }
                                        list.add(RaceItem.RaceHeader)
                                        list.addAll(results
                                            .filterIndexed { index, model -> index >= startIndex }
                                            .map { RaceItem.RaceResult(it) }
                                        )
                                    }
                                    else {
                                        when {
                                            race.raceInfo.date > LocalDate.now() -> list.add(RaceItem.ErrorItem(SyncDataItem.Unavailable(DataUnavailable.RACE_IN_FUTURE)))
                                            else -> list.add(RaceItem.ErrorItem(SyncDataItem.Unavailable(DataUnavailable.COMING_SOON_RACE)))
                                        }
                                    }
                                }
                                RaceDisplayType.QUALIFYING_SPRINT -> {
                                    val results = race.qualifying.firstOrNull { it.label == SPRINT }?.results
                                    if (results != null) {
                                        list.addAll(results.map {
                                            RaceItem.SprintQualifyingResult(
                                                qSprint = it as RaceQualifyingRoundDriver.SprintQualifying
                                            )
                                        })
                                    } else {
                                        when {
                                            race.raceInfo.date > LocalDate.now() -> list.add(RaceItem.ErrorItem(SyncDataItem.Unavailable(DataUnavailable.RACE_IN_FUTURE)))
                                            else -> list.add(RaceItem.ErrorItem(SyncDataItem.Unavailable(DataUnavailable.SEASON_INTERNAL_ERROR)))
                                        }
                                    }
                                }
                                RaceDisplayType.QUALIFYING -> {
                                    if (race.hasSprintQualifying) {
                                        list.add(RaceItem.ErrorItem(SyncDataItem.MessageRes(R.string.race_sprint_qualifying_message)))
                                    }
                                    when {
                                        race.has(Q3) -> {
                                            list.add(RaceItem.QualifyingHeader(true, true, true))
                                            list.addAll(race.getQ1Q2Q3QualifyingList(Q3))
                                        }
                                        race.has(Q2) -> {
                                            list.add(RaceItem.QualifyingHeader(true, true, false))
                                            list.addAll(race.getQ1Q2QualifyingList())
                                        }
                                        race.has(Q1) -> {
                                            list.add(RaceItem.QualifyingHeader(true, false, false))
                                            list.addAll(race.getQ1QualifyingList())
                                        }
                                        else -> {
                                            when {
                                                race.raceInfo.date > LocalDate.now() -> list.add(RaceItem.ErrorItem(SyncDataItem.Unavailable(DataUnavailable.RACE_IN_FUTURE)))
                                                else -> list.add(RaceItem.ErrorItem(SyncDataItem.Unavailable(DataUnavailable.COMING_SOON_RACE)))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    return@map list
                }
        }
        .asLiveData(viewModelScope.coroutineContext)

    init {

    }

    //region Inputs

    override fun initialise(season: Int, round: Int) {
        seasonRound.value = SeasonRound(season, round)
    }

    override fun displayType(type: RaceDisplayType) {
        displayType.value = type
    }

    override fun clickConstructor(constructor: Constructor) {
        goToConstructor.value = DataEvent(constructor)
    }

    override fun clickDriver(driver: Driver) {
        goToDriver.value = DataEvent(driver)
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

    private fun Race.toOverview(): List<RaceItem> {
        return mutableListOf<RaceItem>().apply {
            add(RaceItem.Overview(
                raceName = raceInfo.name,
                country = raceInfo.circuit.country,
                countryISO = raceInfo.circuit.countryISO,
                circuitId = raceInfo.circuit.id,
                circuitName = raceInfo.circuit.name,
                round = raceInfo.round,
                season = raceInfo.season,
                raceDate = raceInfo.date,
                wikipedia = raceInfo.wikipediaUrl,
                schedule = schedule
            ))
            if (raceInfo.date >= LocalDate.now() && schedule.isNotEmpty()) {
                add(RaceItem.ScheduleMax(
                    schedule = schedule
                ))
            }
        }
    }

    private fun Race.getQ1Q2Q3QualifyingList(forType: RaceQualifyingType): List<RaceItem.QualifyingResultQ1Q2Q3> {
        val list = when (forType) {
            Q1, Q2, Q3 -> this.qualifying.firstOrNull { it.label == forType } ?: return emptyList()
            else -> return emptyList()
        }

        return list.results.map {
            val overview = driverOverview(it.driver.driver.id)
            return@map RaceItem.QualifyingResultQ1Q2Q3(
                driver = it.driver,
                finalQualifyingPosition = overview?.qualified,
                q1 = overview?.q1,
                q2 = overview?.q2,
                q3 = overview?.q3,
                q1Delta = if (raceController.showQualifyingDelta) q1FastestLap?.deltaTo(overview?.q1?.lapTime) else null,
                q2Delta = if (raceController.showQualifyingDelta) q2FastestLap?.deltaTo(overview?.q2?.lapTime) else null,
                q3Delta = if (raceController.showQualifyingDelta) q3FastestLap?.deltaTo(overview?.q3?.lapTime) else null,
            )
        }
    }

    private fun Race.getQ1Q2QualifyingList(): List<RaceItem.QualifyingResultQ1Q2> {
        return qualifying.firstOrNull()
            ?.results
            ?.map {
                val overview = driverOverview(it.driver.driver.id)
                return@map RaceItem.QualifyingResultQ1Q2(
                    driver = it.driver,
                    finalQualifyingPosition = overview?.qualified,
                    q1 = overview?.q1,
                    q2 = overview?.q2,
                    q1Delta = if (raceController.showQualifyingDelta) q1FastestLap?.deltaTo(overview?.q1?.lapTime) else null,
                    q2Delta = if (raceController.showQualifyingDelta) q2FastestLap?.deltaTo(overview?.q2?.lapTime) else null,
                )
            }
            ?.sortedBy { it.qualified }
            ?: emptyList()
    }

    private fun Race.getQ1QualifyingList(): List<RaceItem.QualifyingResultQ1> {
        return qualifying.firstOrNull()
            ?.results
            ?.map {
                val overview = driverOverview(it.driver.driver.id)
                return@map RaceItem.QualifyingResultQ1(
                    driver = it.driver,
                    finalQualifyingPosition = overview?.qualified,
                    q1 = overview?.q1,
                    q1Delta = if (raceController.showQualifyingDelta) q1FastestLap?.deltaTo(overview?.q1?.lapTime) else null,
                )
            }
            ?.sortedBy { it.qualified }
            ?: emptyList()
    }

    private fun getDriverFromConstructor(race: Race, constructorId: String): List<Pair<Driver, Double>> {
        return race.race
            .mapNotNull { raceResult ->
                if (raceResult.driver.constructor.id != constructorId) return@mapNotNull null
                val sprintQualifying = race.qualifying.firstOrNull { it.isSprintQualifying }?.results?.firstOrNull { it.driver.driver.id == raceResult.driver.driver.id } as? RaceQualifyingRoundDriver.SprintQualifying
                return@mapNotNull Pair(raceResult.driver.driver, raceResult.points + (sprintQualifying?.points ?: 0.0))
            }
            .sortedByDescending { it.second }
    }


    private fun LocalDate.isWithin(days: Int) = this >= LocalDate.now().minusDays(days.toLong())

    private fun  MutableList<RaceItem>.addError(syncDataItem: SyncDataItem) {
        this.add(RaceItem.ErrorItem(syncDataItem))
    }
}