package tmg.flashback.statistics.ui.overview.driver.season

import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import tmg.flashback.device.managers.NetworkConnectivityManager
import tmg.flashback.formula1.constants.Formula1.maxPointsBySeason
import tmg.flashback.formula1.extensions.pointsDisplay
import tmg.flashback.formula1.model.DriverHistorySeason
import tmg.flashback.statistics.R
import tmg.flashback.statistics.repo.DriverRepository
import tmg.flashback.statistics.ui.overview.driver.summary.PipeType
import tmg.flashback.statistics.ui.shared.sync.SyncDataItem
import tmg.flashback.statistics.ui.shared.sync.viewholders.DataUnavailable
import tmg.flashback.statistics.ui.util.position
import tmg.flashback.ui.controllers.ThemeController
import tmg.utilities.extensions.ordinalAbbreviation
import tmg.utilities.lifecycle.DataEvent
import tmg.utilities.lifecycle.Event

//region Inputs

interface DriverSeasonViewModelInputs {
    fun setup(driverId: String, season: Int)

    fun clickSeasonRound(result: DriverSeasonItem.Result)

    fun refresh()
}

//endregion

//region Outputs

interface DriverSeasonViewModelOutputs {
    val openSeasonRound: LiveData<DataEvent<DriverSeasonItem.Result>>
    val list: LiveData<List<DriverSeasonItem>>

    val isLoading: LiveData<Boolean>
    val showRefreshError: LiveData<Event>
}

//endregion

@Suppress("EXPERIMENTAL_API_USAGE")
class DriverSeasonViewModel(
    private val driverRepository: DriverRepository,
    private val connectivityManager: NetworkConnectivityManager,
    private val themeController: ThemeController
) : ViewModel(),
    DriverSeasonViewModelInputs,
    DriverSeasonViewModelOutputs {

    var inputs: DriverSeasonViewModelInputs = this
    var outputs: DriverSeasonViewModelOutputs = this

    override val openSeasonRound: MutableLiveData<DataEvent<DriverSeasonItem.Result>> = MutableLiveData()
    override val isLoading: MutableLiveData<Boolean> = MutableLiveData()
    override val showRefreshError: MutableLiveData<Event> = MutableLiveData()

    private var driverId: ConflatedBroadcastChannel<String> = ConflatedBroadcastChannel()
    private var season: Int = -1

    override val list: LiveData<List<DriverSeasonItem>> = driverId
        .asFlow()
        .flatMapLatest { driverRepository.getDriverOverview(it) }
        .map { overview ->
            val list: MutableList<DriverSeasonItem> = mutableListOf()
            val standing = overview?.standings?.firstOrNull { it.season == season }
            when {
                overview == null || standing == null -> {
                    when {
                        !connectivityManager.isConnected ->
                            list.addError(SyncDataItem.NoNetwork)
                        else ->
                            list.addError(SyncDataItem.Unavailable(DataUnavailable.DRIVER_NOT_EXIST))
                    }
                }
                else -> {

                    if (standing.isInProgress) {
                        standing.raceOverview.maxByOrNull { it.raceInfo.round }?.let {
                            list.add(
                                DriverSeasonItem.ErrorItem(
                                    SyncDataItem.MessageRes(
                                        R.string.results_accurate_for,
                                        listOf(it.raceInfo.name, it.raceInfo.round)
                                    )
                                )
                            )
                        }
                    }

                    list.addStat(
                            icon = R.drawable.ic_team,
                            label = R.string.driver_overview_stat_career_team,
                            value = ""
                    )
                    if (standing.constructors.size == 1) {
                        val const = standing.constructors.first()
                        list.add(
                            DriverSeasonItem.RacedFor(
                                null,
                                const,
                                PipeType.SINGLE,
                                false
                            )
                        )
                    }
                    else {
                        for (x in standing.constructors.indices) {
                            val const = standing.constructors[(standing.constructors.size - 1) - x]
                            val dotType: PipeType = when (x) {
                                0 -> {
                                    PipeType.START
                                }
                                standing.constructors.size - 1 -> {
                                    PipeType.END
                                }
                                else -> {
                                    PipeType.SINGLE_PIPE
                                }
                            }
                            list.add(
                                DriverSeasonItem.RacedFor(
                                    null,
                                    const,
                                    dotType,
                                    false
                                )
                            )
                        }
                    }

                    list.addAll(getAllStats(standing))

                    list.add(DriverSeasonItem.ResultHeader)

                    list.addAll(standing
                        .raceOverview
                        .map {
                            DriverSeasonItem.Result(
                                season = it.raceInfo.season,
                                round = it.raceInfo.round,
                                raceName = it.raceInfo.name,
                                circuitName = it.raceInfo.circuit.name,
                                circuitId = it.raceInfo.circuit.id,
                                raceCountry = it.raceInfo.circuit.country,
                                raceCountryISO = it.raceInfo.circuit.countryISO,
                                showConstructorLabel = standing.constructors.size > 1,
                                constructor = it.constructor ?: standing.constructors.last(),
                                date = it.raceInfo.date,
                                qualified = it.qualified,
                                finished = it.finished,
                                raceStatus = it.status,
                                points = it.points,
                                maxPoints = maxPointsBySeason(it.raceInfo.season),
                                animationSpeed = themeController.animationSpeed
                            )
                        }
                        .sortedBy { it.round }
                    )
                }
            }
            return@map list
        }
        .asLiveData(viewModelScope.coroutineContext)

    init {

    }

    //region Inputs

    override fun setup(driverId: String, season: Int) {
        if (driverId != this.driverId.valueOrNull) {
            this.season = season
            this.driverId.offer(driverId)
        }
    }

    override fun clickSeasonRound(result: DriverSeasonItem.Result) {
        openSeasonRound.value = DataEvent(result)
    }

    override fun refresh() {
        isLoading.value = true
        viewModelScope.launch(context = Dispatchers.IO) {
            val result = driverRepository.fetchDriver(driverId.value)
            isLoading.postValue(false)
            if (!result) {
                showRefreshError.postValue(Event())
            }
        }
    }

    //endregion


    /**
     * Add career stats for the driver across their career
     */
    private fun getAllStats(overview: DriverHistorySeason): List<DriverSeasonItem> {
        val list: MutableList<DriverSeasonItem> = mutableListOf()

        list.addStat(
            tint = if (!overview.isInProgress && overview.championshipStanding == 1) R.attr.f1Championship else R.attr.contentSecondary,
            icon = if (!overview.isInProgress && overview.championshipStanding == 1) R.drawable.ic_star_filled_coloured else R.drawable.ic_championship_order,
            label = if (overview.isInProgress) R.string.driver_overview_stat_career_championship_standing_so_far else R.string.driver_overview_stat_career_championship_standing,
            value = overview.championshipStanding?.ordinalAbbreviation ?: "N/A"
        )
        list.addStat(
            icon = R.drawable.ic_standings,
            label = R.string.driver_overview_stat_career_wins,
            value = overview.wins.toString()
        )
        list.addStat(
            icon = R.drawable.ic_podium,
            label = R.string.driver_overview_stat_career_podiums,
            value = overview.podiums.toString()
        )
        list.addStat(
            icon = R.drawable.ic_race_starts,
            label = R.string.driver_overview_stat_race_starts,
            value = overview.raceStarts.toString()
        )
        list.addStat(
            icon = R.drawable.ic_race_finishes,
            label = R.string.driver_overview_stat_race_finishes,
            value = overview.raceFinishes.toString()
        )
        list.addStat(
            icon = R.drawable.ic_race_retirements,
            label = R.string.driver_overview_stat_race_retirements,
            value = overview.raceRetirements.toString()
        )
        list.addStat(
            icon = R.drawable.ic_best_finish,
            label = R.string.driver_overview_stat_career_best_finish,
            value = overview.bestFinish?.position() ?: "N/A"
        )
        list.addStat(
            icon = R.drawable.ic_finishes_in_points,
            label = R.string.driver_overview_stat_career_points_finishes,
            value = overview.finishesInPoints.toString()
        )
        list.addStat(
            icon = R.drawable.ic_race_points,
            label = R.string.driver_overview_stat_career_points,
            value = overview.points.pointsDisplay()
        )
        list.addStat(
            icon = R.drawable.ic_qualifying_pole,
            label = R.string.driver_overview_stat_career_qualifying_pole,
            value = overview.qualifyingPoles.toString()
        )
        list.addStat(
            icon = R.drawable.ic_qualifying_front_row,
            label = R.string.driver_overview_stat_career_qualifying_top_3,
            value = overview.qualifyingTop3.toString()
        )
        list.addStat(
            icon = R.drawable.ic_qualifying_top_ten,
            label = R.string.driver_overview_stat_career_qualifying_top_10,
            value = overview.totalQualifyingAbove(10).toString()
        )
        return list
    }

    private fun MutableList<DriverSeasonItem>.addStat(@AttrRes tint: Int = R.attr.contentSecondary, @DrawableRes icon: Int, @StringRes label: Int, value: String) {
        this.add(
            DriverSeasonItem.Stat(
                tint = tint,
                icon = icon,
                label = label,
                value = value
            )
        )
    }
}
