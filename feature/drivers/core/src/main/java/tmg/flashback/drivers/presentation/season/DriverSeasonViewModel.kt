package tmg.flashback.drivers.presentation.season

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import tmg.flashback.device.managers.NetworkConnectivityManager
import tmg.flashback.data.repo.DriverRepository
import tmg.flashback.drivers.R
import tmg.flashback.formula1.R.drawable
import tmg.flashback.strings.R.string
import tmg.flashback.formula1.constants.Formula1
import tmg.flashback.formula1.extensions.pointsDisplay
import tmg.flashback.formula1.model.DriverHistorySeason
import tmg.flashback.ui.components.navigation.PipeType
import tmg.flashback.ui.repository.ThemeRepository
import tmg.utilities.extensions.ordinalAbbreviation
import javax.inject.Inject

//region Inputs

interface DriverSeasonViewModelInputs {
    fun setup(driverId: String, season: Int)

    fun clickSeasonRound(result: DriverSeasonModel.Result)

    fun refresh()
}

//endregion

//region Outputs

interface DriverSeasonViewModelOutputs {
    val list: StateFlow<List<DriverSeasonModel>>

    val isLoading: StateFlow<Boolean>
}

//endregion

@Suppress("EXPERIMENTAL_API_USAGE")
@HiltViewModel
class DriverSeasonViewModel @Inject constructor(
    private val driverRepository: DriverRepository,
    private val connectivityManager: NetworkConnectivityManager,
    private val themeRepository: ThemeRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel(), DriverSeasonViewModelInputs, DriverSeasonViewModelOutputs {

    var inputs: DriverSeasonViewModelInputs = this
    var outputs: DriverSeasonViewModelOutputs = this

    override val isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private val driverIdAndSeason: MutableStateFlow<Pair<String, Int>?> = MutableStateFlow(null)
    private val driverIdWithRequest: Flow<Pair<String, Int>?> = driverIdAndSeason
        .filterNotNull()
        .flatMapLatest { id ->
            return@flatMapLatest flow {
                if (driverRepository.getDriverSeasonCount(id.first) == 0) {
                    isLoading.value = true
                    emit(null)
                    driverRepository.fetchDriver(id.first)
                    isLoading.value = false
                    emit(id)
                } else {
                    emit(id)
                    isLoading.value = true
                    driverRepository.fetchDriver(id.first)
                    isLoading.value = false
                }
            }
        }
        .flowOn(ioDispatcher)

    override val list: StateFlow<List<DriverSeasonModel>> = driverIdWithRequest
        .flatMapLatest { idAndSeason ->
            if (idAndSeason == null) {
                return@flatMapLatest flow {
                    emit(mutableListOf<DriverSeasonModel>(DriverSeasonModel.Loading))
                }
            }

            val (id, season) = idAndSeason
            return@flatMapLatest driverRepository.getDriverOverview(id)
                .map { overview ->
                    val list: MutableList<DriverSeasonModel> = mutableListOf()
                    val standing = overview?.standings?.firstOrNull { it.season == season }
                    when {
                        overview == null || standing == null -> {
                            if (overview != null) {
                                list.add(DriverSeasonModel.Header(overview.driver))
                            }
                            when {
                                !connectivityManager.isConnected -> list.add(DriverSeasonModel.NetworkError)
                                else -> list.add(DriverSeasonModel.InternalError)
                            }
                        }

                        else -> {
                            list.add(DriverSeasonModel.Header(overview.driver))

                            if (standing.isInProgress) {
                                standing.raceOverview.maxByOrNull { it.raceInfo.round }?.let {
                                    list.add(
                                        DriverSeasonModel.Message(
                                            string.results_accurate_for,
                                            listOf(it.raceInfo.name, it.raceInfo.round)
                                        )
                                    )
                                }
                            }

                            list.addStat(
                                icon = drawable.ic_team,
                                label = string.driver_overview_stat_career_team,
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
                            } else {
                                for (x in standing.constructors.indices) {
                                    val const =
                                        standing.constructors[(standing.constructors.size - 1) - x]
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

                            list.addAll(getAllStats(standing))

                            list.add(DriverSeasonModel.ResultHeader)

                            list.addAll(standing
                                .raceOverview
                                .map {
                                    DriverSeasonModel.Result(
                                        isSprint = it.isSprint,
                                        season = it.raceInfo.season,
                                        round = it.raceInfo.round,
                                        raceName = it.raceInfo.name,
                                        circuitName = it.raceInfo.circuit.name,
                                        circuitId = it.raceInfo.circuit.id,
                                        raceCountry = it.raceInfo.circuit.country,
                                        raceCountryISO = it.raceInfo.circuit.countryISO,
                                        showConstructorLabel = standing.constructors.size > 1,
                                        constructor = it.constructor
                                            ?: standing.constructors.last(),
                                        date = it.raceInfo.date,
                                        qualified = it.qualified,
                                        finished = it.finished,
                                        raceStatus = it.status,
                                        points = it.points,
                                        maxPoints = Formula1.maxDriverPointsBySeason(it.raceInfo.season)
                                    )
                                }
                                .sortedBy { it.isSprint }
                                .sortedBy { it.round }
                            )
                        }
                    }
                    return@map list
                }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    //region Inputs

    override fun setup(driverId: String, season: Int) {
        this.driverIdAndSeason.value = Pair(driverId, season)
    }

    override fun clickSeasonRound(result: DriverSeasonModel.Result) {
        // TODO: Do something with this?
    }

    override fun refresh() {
        isLoading.value = true
        viewModelScope.launch(context = ioDispatcher) {
            driverIdAndSeason.value?.let {
                val result = driverRepository.fetchDriver(it.first)
                isLoading.value = false
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
            icon = if (!overview.isInProgress && overview.championshipStanding == 1) drawable.ic_star_filled_coloured else drawable.ic_championship_order,
            label = if (overview.isInProgress) string.driver_overview_stat_career_championship_standing_so_far else string.driver_overview_stat_career_championship_standing,
            value = overview.championshipStanding?.ordinalAbbreviation ?: "N/A"
        )
        list.addStat(
            icon = drawable.ic_standings,
            label = string.driver_overview_stat_career_wins,
            value = overview.wins.toString()
        )
        list.addStat(
            icon = drawable.ic_podium,
            label = string.driver_overview_stat_career_podiums,
            value = overview.podiums.toString()
        )
        list.addStat(
            icon = drawable.ic_race_starts,
            label = string.driver_overview_stat_race_starts,
            value = overview.raceStarts.toString()
        )
        list.addStat(
            icon = drawable.ic_race_finishes,
            label = string.driver_overview_stat_race_finishes,
            value = overview.raceFinishes.toString()
        )
        list.addStat(
            icon = drawable.ic_race_retirements,
            label = string.driver_overview_stat_race_retirements,
            value = overview.raceRetirements.toString()
        )
        list.addStat(
            icon = drawable.ic_best_finish,
            label = string.driver_overview_stat_career_best_finish,
            value = overview.bestFinish?.ordinalAbbreviation ?: "N/A"
        )
        list.addStat(
            icon = drawable.ic_finishes_in_points,
            label = string.driver_overview_stat_career_points_finishes,
            value = overview.finishesInPoints.toString()
        )
        list.addStat(
            icon = drawable.ic_race_points,
            label = string.driver_overview_stat_career_points,
            value = overview.points.pointsDisplay()
        )
        list.addStat(
            icon = drawable.ic_qualifying_pole,
            label = string.driver_overview_stat_career_qualifying_pole,
            value = overview.qualifyingPoles.toString()
        )
        list.addStat(
            icon = drawable.ic_qualifying_front_row,
            label = string.driver_overview_stat_career_qualifying_top_3,
            value = overview.qualifyingTop3.toString()
        )
        list.addStat(
            icon = drawable.ic_qualifying_top_ten,
            label = string.driver_overview_stat_career_qualifying_top_10,
            value = overview.totalQualifyingAbove(10).toString()
        )
        return list
    }

    private fun MutableList<DriverSeasonModel>.addStat(
        isWinning: Boolean = false,
        @DrawableRes icon: Int,
        @StringRes label: Int,
        value: String
    ) {
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