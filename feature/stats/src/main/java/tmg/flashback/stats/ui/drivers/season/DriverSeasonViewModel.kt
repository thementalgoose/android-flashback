package tmg.flashback.stats.ui.drivers.season

import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import tmg.flashback.device.managers.NetworkConnectivityManager
import tmg.flashback.formula1.constants.Formula1
import tmg.flashback.formula1.extensions.pointsDisplay
import tmg.flashback.formula1.model.DriverHistorySeason
import tmg.flashback.statistics.repo.DriverRepository
import tmg.flashback.stats.R
import tmg.flashback.stats.ui.drivers.overview.PipeType
import tmg.flashback.ui.repository.ThemeRepository
import tmg.utilities.extensions.ordinalAbbreviation
import tmg.utilities.lifecycle.DataEvent
import tmg.utilities.lifecycle.Event


//region Inputs

interface DriverSeasonViewModelInputs {
    fun setup(driverId: String, season: Int)

    fun clickSeasonRound(result: DriverSeasonModel.Result)

    fun refresh()
}

//endregion

//region Outputs

interface DriverSeasonViewModelOutputs {
    val openSeasonRound: LiveData<DataEvent<DriverSeasonModel.Result>>
    val list: LiveData<List<DriverSeasonModel>>

    val isLoading: LiveData<Boolean>
    val showRefreshError: LiveData<Event>
}

//endregion

@Suppress("EXPERIMENTAL_API_USAGE")
class DriverSeasonViewModel(
    private val driverRepository: DriverRepository,
    private val connectivityManager: NetworkConnectivityManager,
    private val themeRepository: ThemeRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel(),
    DriverSeasonViewModelInputs,
    DriverSeasonViewModelOutputs {

    var inputs: DriverSeasonViewModelInputs = this
    var outputs: DriverSeasonViewModelOutputs = this

    override val openSeasonRound: MutableLiveData<DataEvent<DriverSeasonModel.Result>> = MutableLiveData()
    override val isLoading: MutableLiveData<Boolean> = MutableLiveData()
    override val showRefreshError: MutableLiveData<Event> = MutableLiveData()

    private var driverId: MutableStateFlow<String?> = MutableStateFlow(null)
    private var season: Int = -1

    override val list: LiveData<List<DriverSeasonModel>> = driverId
        .filterNotNull()
        .flatMapLatest { driverRepository.getDriverOverview(it) }
        .map { overview ->
            val list: MutableList<DriverSeasonModel> = mutableListOf()
            val standing = overview?.standings?.firstOrNull { it.season == season }
            when {
                overview == null || standing == null -> {
                    when {
                        !connectivityManager.isConnected -> list.add(DriverSeasonModel.NetworkError)
                        else -> list.add(DriverSeasonModel.InternalError)
                    }
                }
                else -> {

                    if (standing.isInProgress) {
                        standing.raceOverview.maxByOrNull { it.raceInfo.round }?.let {
                            list.add(
                                DriverSeasonModel.Message(
                                    R.string.results_accurate_for,
                                    listOf(it.raceInfo.name, it.raceInfo.round)
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
                            DriverSeasonModel.RacedFor(
                                null,
                                const,
                                PipeType.SINGLE,
                                false
                            )
                        )
                    }
                    else {
                        if (standing.constructors.size == 1) {
                            list.add(DriverSeasonModel.RacedFor(
                                null,
                                standing.constructors.first(),
                                PipeType.SINGLE,
                                false
                            ))
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
                                        PipeType.SINGLE
                                    }
                                }
                                list.add(
                                    DriverSeasonModel.RacedFor(
                                        null,
                                        const,
                                        dotType,
                                        false
                                    )
                                )
                            }
                        }
                    }

                    list.addAll(getAllStats(standing))

                    list.add(DriverSeasonModel.ResultHeader)

                    list.addAll(standing
                        .raceOverview
                        .map {
                            DriverSeasonModel.Result(
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
                                maxPoints = Formula1.maxPointsBySeason(it.raceInfo.season),
                                animationSpeed = themeRepository.animationSpeed
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
        if (driverId != this.driverId.value) {
            this.season = season
            this.driverId.value = driverId
        }
        refresh()
    }

    override fun clickSeasonRound(result: DriverSeasonModel.Result) {
        openSeasonRound.value = DataEvent(result)
    }

    override fun refresh() {
        isLoading.value = true
        viewModelScope.launch(context = ioDispatcher) {
            driverId.value?.let {
                val result = driverRepository.fetchDriver(it)
                isLoading.postValue(false)
                if (!result) {
                    showRefreshError.postValue(Event())
                }
            }
        }
    }

    //endregion


    /**
     * Add career stats for the driver across their career
     */
    private fun getAllStats(overview: DriverHistorySeason): List<DriverSeasonModel> {
        val list: MutableList<DriverSeasonModel> = mutableListOf()

        list.addStat(
            isWinning = !overview.isInProgress && overview.championshipStanding == 1,
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
            value = overview.bestFinish?.ordinalAbbreviation ?: "N/A"
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

    private fun MutableList<DriverSeasonModel>.addStat(isWinning: Boolean = false, @DrawableRes icon: Int, @StringRes label: Int, value: String) {
        this.add(
            DriverSeasonModel.Stat(
                isWinning = isWinning,
                icon = icon,
                label = label,
                value = value
            )
        )
    }
}