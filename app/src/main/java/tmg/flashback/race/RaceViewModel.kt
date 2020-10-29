package tmg.flashback.race

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import org.threeten.bp.LocalDate
import tmg.flashback.base.BaseViewModel
import tmg.flashback.repo.db.PrefsDB
import tmg.flashback.repo.db.stats.SeasonOverviewDB
import tmg.flashback.repo.models.stats.*
import tmg.flashback.settings.ConnectivityManager
import tmg.flashback.shared.viewholders.DataUnavailable
import tmg.flashback.showComingSoonMessageForNextDays
import tmg.flashback.di.async.ScopeProvider
import tmg.flashback.utils.SeasonRound
import tmg.utilities.extensions.combineTriple
import tmg.utilities.lifecycle.DataEvent

//region Inputs

interface RaceViewModelInputs {
    fun initialise(season: Int, round: Int, date: LocalDate?)
    fun orderBy(seasonRaceAdapterType: RaceAdapterType)
    fun goToDriver(driverId: String, driverName: String)
    fun clickWikipedia()
}

//endregion

//region Outputs

interface RaceViewModelOutputs {
    val circuitInfo: LiveData<Round>
    val raceItems: LiveData<Triple<RaceAdapterType, List<RaceAdapterModel>, SeasonRound>>
    val seasonRoundData: LiveData<SeasonRound>
    val goToDriverOverview: MutableLiveData<DataEvent<Pair<String, String>>>

    val showWikipedia: MutableLiveData<Boolean>
    val goToWikipedia: MutableLiveData<DataEvent<String>>
}

//endregion

@Suppress("EXPERIMENTAL_API_USAGE")
class RaceViewModel(
    seasonOverviewDB: SeasonOverviewDB,
    private val prefsDB: PrefsDB,
    connectivityManager: ConnectivityManager,
    scopeProvider: ScopeProvider
) : BaseViewModel(scopeProvider), RaceViewModelInputs, RaceViewModelOutputs {

    var inputs: RaceViewModelInputs = this
    var outputs: RaceViewModelOutputs = this

    private var wikipedia: String? = null

    private var roundDate: LocalDate? = null
    private val seasonRound: ConflatedBroadcastChannel<SeasonRound> = ConflatedBroadcastChannel()
    private var viewType: ConflatedBroadcastChannel<RaceAdapterType> = ConflatedBroadcastChannel()

    override val goToDriverOverview: MutableLiveData<DataEvent<Pair<String, String>>> = MutableLiveData()

    override val showWikipedia: MutableLiveData<Boolean> = MutableLiveData(false)
    override val goToWikipedia: MutableLiveData<DataEvent<String>> = MutableLiveData()

    private val seasonRoundFlow: Flow<Round?> = seasonRound
        .asFlow()
        .flatMapLatest { (season, round) -> seasonOverviewDB.getSeasonRound(season, round) }

    override val seasonRoundData: LiveData<SeasonRound> = seasonRound
        .asFlow()
        .asLiveData(scope.coroutineContext)

    override val circuitInfo: LiveData<Round> = seasonRoundFlow
        .filterNotNull()
        .asLiveData(scope.coroutineContext)

    override val raceItems: LiveData<Triple<RaceAdapterType, List<RaceAdapterModel>, SeasonRound>> = seasonRoundFlow
            .combineTriple(viewType.asFlow(), seasonRound.asFlow())
            .map { (roundData, viewType, seasonRoundValue) ->
                if (roundData == null) {
                    val list = mutableListOf<RaceAdapterModel>()
                    when {
                        !connectivityManager.isConnected ->
                            list.add(RaceAdapterModel.NoNetwork)
                        roundDate != null && roundDate!! > LocalDate.now() ->
                            list.add(RaceAdapterModel.Unavailable(DataUnavailable.IN_FUTURE_RACE))
                        roundDate != null && roundDate!! <= LocalDate.now() && roundDate!! >= LocalDate.now().minusDays(showComingSoonMessageForNextDays.toLong()) ->
                            list.add(RaceAdapterModel.Unavailable(DataUnavailable.COMING_SOON_RACE))
                        else ->
                            list.add(RaceAdapterModel.Unavailable(DataUnavailable.MISSING_RACE))
                    }
                    return@map Triple(viewType, list, seasonRoundValue)
                }

                wikipedia = roundData.wikipediaUrl
                showWikipedia.value = wikipedia != null

                // Constructor standings, models are constructors
                if (viewType == RaceAdapterType.CONSTRUCTOR_STANDINGS) {
                    val list: List<RaceAdapterModel.ConstructorStandings> = roundData
                        .constructorStandings
                        .map {
                            val drivers: List<Pair<Driver, Int>> = if (prefsDB.showDriversBehindConstructor) {
                                getDriverFromConstructor(roundData, it.constructor.id)
                            }
                            else {
                                emptyList()
                            }
                            RaceAdapterModel.ConstructorStandings(it.constructor, it.points, drivers, prefsDB.barAnimation)
                        }
                        .sortedByDescending { it.points }

                    return@map Triple(
                        viewType,
                        list,
                        SeasonRound(roundData.season, roundData.round)
                    )
                }
                // Race or qualifying - Models are driver models
                else {
                    val driverIds: List<String> = getOrderedDriverIds(roundData, viewType)
                    val list: MutableList<RaceAdapterModel> = mutableListOf()
                    val showQualifying = ShowQualifying(
                        q1 = roundData.q1.count { it.value.time != null } > 0,
                        q2 = roundData.q2.count { it.value.time != null } > 0,
                        q3 = roundData.q3.count { it.value.time != null } > 0,
                        deltas = prefsDB.showQualifyingDelta,
                        penalties = prefsDB.showGridPenaltiesInQualifying
                    )

                    when (viewType) {
                        RaceAdapterType.RACE -> {
                            if (roundData.race.isNotEmpty()) {
                                var startIndex = 0
                                if (driverIds.size >= 3) {
                                    list.add(
                                        RaceAdapterModel.Podium(
                                            driverFirst = getDriverModel(
                                                roundData,
                                                viewType,
                                                driverIds[0],
                                                showQualifying
                                            ),
                                            driverSecond = getDriverModel(
                                                roundData,
                                                viewType,
                                                driverIds[1],
                                                showQualifying
                                            ),
                                            driverThird = getDriverModel(
                                                roundData,
                                                viewType,
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
                                    list.add(
                                        getDriverModel(
                                            roundData,
                                            viewType,
                                            driverIds[i],
                                            showQualifying
                                        )
                                    )
                                }
                            }
                            else {
                                when {
                                    roundData.date > LocalDate.now() -> list.add(RaceAdapterModel.Unavailable(DataUnavailable.IN_FUTURE_RACE))
                                    else -> list.add(RaceAdapterModel.Unavailable(DataUnavailable.COMING_SOON_RACE))
                                }
                            }
                        }
                        RaceAdapterType.QUALIFYING_POS_1,
                        RaceAdapterType.QUALIFYING_POS_2,
                        RaceAdapterType.QUALIFYING_POS -> {
                            list.add(RaceAdapterModel.QualifyingHeader(showQualifying))
                            list.addAll(driverIds.mapIndexed { _, driverId ->
                                getDriverModel(
                                    roundData,
                                    viewType,
                                    driverId,
                                    showQualifying
                                )
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
            .asLiveData(scope.coroutineContext)

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

    override fun clickWikipedia() {
        goToWikipedia.postValue(DataEvent(wikipedia ?: ""))
    }

    //endregion

    private fun getDriverFromConstructor(round: Round, constructorId: String): List<Pair<Driver, Int>> {
        return round
            .drivers
            .filter { it.constructor.id == constructorId }
            .map { Pair(it.toDriver(), round.race[it.id]?.points ?: 0) }
            .sortedByDescending { it.second }
    }

    /**
     * Get a list of ordered driver Ids based on the view type
     */
    private fun getOrderedDriverIds(roundData: Round?, viewType: RaceAdapterType): List<String> {
        if (roundData == null) {
            return emptyList()
        }
        if (roundData.race.isNotEmpty()) {
            return roundData
                .race
                .values
                .sortedBy {
                    val driverOverview: RoundDriverOverview = roundData.driverOverview(it.driver.id)

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
                        else -> it.finish
                    }
                }
                .map { it.driver.id }
        }
        else {
            return roundData
                .drivers
                .sortedBy {

                    val q1 = roundData.q1[it.id]
                    val q2 = roundData.q2[it.id]
                    val q3 = roundData.q3[it.id]

                    return@sortedBy when (viewType) {
                        RaceAdapterType.QUALIFYING_POS_1 -> q1?.position
                        RaceAdapterType.QUALIFYING_POS_2 -> q2?.position ?: q1?.position
                        else -> q3?.position ?: q2?.position ?: q1?.position
                    }
                }
                .map { it.id }
        }
    }

    /**
     * Get a [RaceAdapterModel.Single] instance for a given driver
     */
    private fun getDriverModel(
        round: Round,
        @Suppress("UNUSED_PARAMETER")
        viewType: RaceAdapterType,
        driverId: String,
        showQualifying: ShowQualifying
    ): RaceAdapterModel.Single {
        val overview = round.driverOverview(driverId)
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
        return RaceAdapterModel.Single(
            season = round.season,
            round = round.round,
            driver = round.drivers.first { it.id == driverId },
            q1 = overview.q1,
            q2 = overview.q2,
            q3 = overview.q3,
            race = race,
            qualified = overview.race?.qualified ?: round.getQualifyingOnlyPosByDriverId(driverId),
            q1Delta = if (prefsDB.showQualifyingDelta) round.q1FastestLap?.deltaTo(overview.q1?.time) else null,
            q2Delta = if (prefsDB.showQualifyingDelta) round.q2FastestLap?.deltaTo(overview.q2?.time) else null,
            q3Delta = if (prefsDB.showQualifyingDelta) round.q3FastestLap?.deltaTo(overview.q3?.time) else null,
            showQualifying = showQualifying
        )
    }

    private fun Round.getQualifyingOnlyPosByDriverId(driverId: String): Int? {

        val q1 = this.q1[driverId]
        val q2 = this.q2[driverId]
        val q3 = this.q3[driverId]

        return q3?.position ?: q2?.position ?: q1?.position
    }
}