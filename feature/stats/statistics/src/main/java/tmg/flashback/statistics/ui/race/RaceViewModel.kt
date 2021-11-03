package tmg.flashback.statistics.ui.race

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import org.threeten.bp.LocalDate
import androidx.lifecycle.ViewModel
import tmg.flashback.statistics.controllers.RaceController
import tmg.flashback.data.db.stats.SeasonOverviewRepository
import tmg.core.ui.controllers.ThemeController
import tmg.flashback.formula1.constants.Formula1.showComingSoonMessageForNextDays
import tmg.flashback.formula1.model.ConstructorDriver
import tmg.flashback.formula1.model.LapTime
import tmg.flashback.formula1.model.Race
import tmg.flashback.formula1.model.RaceDriverOverview
import tmg.flashback.statistics.ui.race.*
import tmg.flashback.statistics.ui.shared.sync.SyncDataItem
import tmg.flashback.statistics.ui.shared.sync.viewholders.DataUnavailable
import tmg.flashback.statistics.ui.util.SeasonRound
import tmg.utilities.extensions.combinePair
import tmg.utilities.extensions.combineTriple
import tmg.utilities.lifecycle.DataEvent
import java.util.*

//region Inputs

interface RaceViewModelInputs {
    fun initialise(season: Int, round: Int, date: LocalDate?)
    fun orderBy(seasonRaceAdapterType: RaceAdapterType)
    fun goToDriver(driverId: String, driverName: String)
    fun goToConstructor(constructorId: String, constructorName: String)
    fun toggleQualifyingDelta(toNewState: Boolean)
}

//endregion

//region Outputs

interface RaceViewModelOutputs {
    val raceItems: LiveData<Triple<RaceAdapterType, List<RaceModel>, SeasonRound>>
    val goToDriverOverview: LiveData<DataEvent<Pair<String, String>>>
    val goToConstructorOverview: LiveData<DataEvent<Pair<String, String>>>

    val showSprintQualifying: LiveData<Boolean>
}

//endregion

class RaceViewModel(
    private val seasonOverviewRepository: SeasonOverviewRepository,
    private val raceController: RaceController,
    private val themeController: ThemeController,
    private val connectivityManager: tmg.core.device.managers.NetworkConnectivityManager
) : ViewModel(), RaceViewModelInputs, RaceViewModelOutputs {

    var inputs: RaceViewModelInputs = this
    var outputs: RaceViewModelOutputs = this

    private var roundDate: LocalDate? = null
    private var toggleQualifyingDelta: Boolean? = null
    private val seasonRound: ConflatedBroadcastChannel<SeasonRound> = ConflatedBroadcastChannel()

    private var viewType: ConflatedBroadcastChannel<RaceAdapterType> = ConflatedBroadcastChannel()
    private var viewTypeRefresh: ConflatedBroadcastChannel<String> = ConflatedBroadcastChannel(UUID.randomUUID().toString())

    private var viewTypeFlow = viewType.asFlow()
        .combinePair(viewTypeRefresh.asFlow())

    override val goToDriverOverview: MutableLiveData<DataEvent<Pair<String, String>>> = MutableLiveData()
    override val goToConstructorOverview: MutableLiveData<DataEvent<Pair<String, String>>> = MutableLiveData()
    override val showSprintQualifying: MutableLiveData<Boolean> = MutableLiveData(false)

    private val seasonRaceFlow: Flow<Race?> = seasonRound
        .asFlow()
        .flatMapLatest { (season, round) -> seasonOverviewRepository.getSeasonRound(season, round) }
        .shareIn(viewModelScope, SharingStarted.Lazily)

    override val raceItems: LiveData<Triple<RaceAdapterType, List<RaceModel>, SeasonRound>> = seasonRaceFlow
            .combineTriple(viewTypeFlow, seasonRound.asFlow())
            .map { (roundData, refreshableViewType, seasonRoundValue) ->
                val (viewType, _) = refreshableViewType
                if (roundData == null) {
                    val list = mutableListOf<RaceModel>()
                    when {
                        !connectivityManager.isConnected ->
                            list.add(RaceModel.ErrorItem(SyncDataItem.NoNetwork))
                        roundDate != null && roundDate!! > LocalDate.now() ->
                            list.add(RaceModel.ErrorItem(SyncDataItem.Unavailable((DataUnavailable.IN_FUTURE_RACE))))
                        roundDate != null && roundDate!! <= LocalDate.now() && roundDate!! >= LocalDate.now().minusDays(showComingSoonMessageForNextDays.toLong()) ->
                            list.add(RaceModel.ErrorItem(SyncDataItem.Unavailable((DataUnavailable.COMING_SOON_RACE))))
                        else ->
                            list.add(RaceModel.ErrorItem(SyncDataItem.Unavailable((DataUnavailable.MISSING_RACE))))
                    }
                    return@map Triple(viewType, list, seasonRoundValue)
                }

                showSprintQualifying.value = roundData.hasSprintQualifying

                // Constructor standings, models are constructors
                if (viewType == RaceAdapterType.CONSTRUCTOR_STANDINGS) {
                    val list: List<RaceModel> = mutableListOf<RaceModel>().apply {
                        add(getRaceOverview(roundData))
                        addAll(roundData
                            .constructorStandings
                            .map {
                                val drivers: List<Pair<ConstructorDriver, Double>> =
                                    getDriverFromConstructor(roundData, it.constructor.id)
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
                    }

                    return@map Triple(
                        viewType,
                        list,
                        SeasonRound(roundData.season, roundData.round)
                    )
                }
                // Race or qualifying - Models are driver models
                else {
                    val driverIds: List<String> = getOrderedDriverIds(roundData, viewType)
                    val list: MutableList<RaceModel> = mutableListOf(getRaceOverview(roundData))
                    val showQualifying = DisplayPrefs(
                        q1 = roundData.q1.count { it.value.time != null } > 0,
                        q2 = roundData.q2.count { it.value.time != null } > 0,
                        q3 = roundData.q3.count { it.value.time != null } > 0,
                        deltas = toggleQualifyingDelta ?: raceController.showQualifyingDelta,
                        penalties = raceController.showGridPenaltiesInQualifying,
                        fadeDNF = raceController.fadeDNF
                    )

                    when (viewType) {
                        RaceAdapterType.RACE -> {
                            if (roundData.race.isNotEmpty()) {
                                var startIndex = 0
                                if (driverIds.size >= 3) {
                                    list.add(RaceModel.Podium(
                                        driverFirst = getDriverModel(roundData, viewType, driverIds[0], showQualifying),
                                        driverSecond = getDriverModel(roundData, viewType, driverIds[1], showQualifying),
                                        driverThird = getDriverModel(roundData, viewType, driverIds[2], showQualifying)
                                    ))
                                    startIndex = 3
                                    list.add(RaceModel.RaceHeader(roundData.season, roundData.round))
                                }
                                for (i in startIndex until driverIds.size) {
                                    list.add(
                                        getDriverModel(roundData, viewType, driverIds[i], showQualifying)
                                    )
                                }
                            }
                            else {
                                when {
                                    roundData.date > LocalDate.now() -> list.add(RaceModel.ErrorItem(SyncDataItem.Unavailable(DataUnavailable.IN_FUTURE_RACE)))
                                    else -> list.add(RaceModel.ErrorItem(SyncDataItem.Unavailable(DataUnavailable.COMING_SOON_RACE)))
                                }
                            }
                        }
                        RaceAdapterType.QUALIFYING_SPRINT -> {
                            list.add(RaceModel.RaceHeader(roundData.season, roundData.round))
                            list.addAll(driverIds.mapIndexed { _, driverId ->
                                getDriverModel(roundData, viewType, driverId, showQualifying)
                            })
                        }
                        RaceAdapterType.QUALIFYING_POS_1,
                        RaceAdapterType.QUALIFYING_POS_2,
                        RaceAdapterType.QUALIFYING_POS -> {
                            list.add(RaceModel.QualifyingHeader(showQualifying))
                            list.addAll(driverIds.mapIndexed { _, driverId ->
                                getDriverModel(roundData, viewType, driverId, showQualifying)
                            })
                        }
                        else -> throw Error("Unsupported view type")
                    }
                    return@map Triple(
                        viewType,
                        list,
                        SeasonRound(roundData.season, roundData.round)
                    )
                }
            }
            .asLiveData(viewModelScope.coroutineContext)

    //region Inputs

    override fun initialise(season: Int, round: Int, date: LocalDate?) {
        val existing: SeasonRound? = seasonRound.valueOrNull
        if (existing?.first != season || existing.second != round) {
            roundDate = date
            seasonRound.offer(SeasonRound(season, round))
        }
    }

    override fun orderBy(seasonRaceAdapterType: RaceAdapterType) {
        viewType.offer(seasonRaceAdapterType)
    }

    override fun goToDriver(driverId: String, driverName: String) {
        goToDriverOverview.value = DataEvent(Pair(driverId, driverName))
    }

    override fun goToConstructor(constructorId: String, constructorName: String) {
        goToConstructorOverview.value = DataEvent(Pair(constructorId, constructorName))
    }

    override fun toggleQualifyingDelta(toNewState: Boolean) {
        toggleQualifyingDelta = toNewState
        viewTypeRefresh.offer(UUID.randomUUID().toString())
    }

    //endregion

    private fun getDriverFromConstructor(race: Race, constructorId: String): List<Pair<ConstructorDriver, Double>> {
        return race
            .drivers
            .filter { it.constructor.id == constructorId }
            .map { Pair(it, race.race[it.id]?.points ?: 0.0) }
            .sortedByDescending { it.second }
    }

    private fun getRaceOverview(raceData: Race): RaceModel.Overview {
        return RaceModel.Overview(
            raceName = raceData.name,
            country = raceData.circuit.country,
            countryISO = raceData.circuit.countryISO,
            circuitId = raceData.circuit.id,
            circuitName = raceData.circuit.name,
            round = raceData.round,
            season = raceData.season,
            raceDate = raceData.date,
            wikipedia = raceData.wikipediaUrl
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
            return raceData
                .race
                .values
                .sortedBy {
                    val driverOverview: RaceDriverOverview = raceData.driverOverview(it.driver.id)

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
                .map { it.driver.id }
        }
        else {
            return raceData
                .drivers
                .sortedBy {

                    val q1 = raceData.q1[it.id]
                    val q2 = raceData.q2[it.id]
                    val q3 = raceData.q3[it.id]

                    return@sortedBy when (viewType) {
                        RaceAdapterType.QUALIFYING_SPRINT -> raceData.qSprint[it.id]?.finish
                        RaceAdapterType.QUALIFYING_POS_1 -> q1?.position
                        RaceAdapterType.QUALIFYING_POS_2 -> q2?.position ?: q1?.position
                        else -> q3?.position ?: q2?.position ?: q1?.position
                    }
                }
                .map { it.id }
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
        val overview = race.driverOverview(driverId)
        val race = overview.race?.let {
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
            season = race.season,
            round = race.round,
            driver = race.drivers.first { it.id == driverId },
            q1 = overview.q1,
            q2 = overview.q2,
            q3 = overview.q3,
            qSprint = overview.qSprint,
            race = race,
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
}