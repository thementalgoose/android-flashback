package tmg.flashback.drivers.presentation.overview

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
import tmg.flashback.drivers.contract.DriverNavigationComponent
import tmg.flashback.drivers.contract.model.DriverStatHistoryType
import tmg.flashback.drivers.contract.model.ScreenDriverData
import tmg.flashback.formula1.extensions.pointsDisplay
import tmg.flashback.formula1.model.DriverHistory
import tmg.flashback.ui.components.navigation.PipeType
import tmg.flashback.web.usecases.OpenWebpageUseCase
import tmg.utilities.extensions.ordinalAbbreviation
import javax.inject.Inject

//region Inputs

interface DriverOverviewViewModelInputs {
    fun back()
    fun openUrl(url: String)
    fun openSeason(season: Int)

    fun refresh()
}

//endregion

//region Outputs

interface DriverOverviewViewModelOutputs {
    val uiState: StateFlow<DriverOverviewScreenState>
}

//endregion


@Suppress("EXPERIMENTAL_API_USAGE")
@HiltViewModel
class DriverOverviewViewModel @Inject constructor(
    private val driverRepository: DriverRepository,
    private val openWebpageUseCase: OpenWebpageUseCase,
    private val networkConnectivityManager: NetworkConnectivityManager,
    savedStateHandle: SavedStateHandle,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel(), DriverOverviewViewModelInputs, DriverOverviewViewModelOutputs {

    var inputs: DriverOverviewViewModelInputs = this
    var outputs: DriverOverviewViewModelOutputs = this

    override val uiState: MutableStateFlow<DriverOverviewScreenState>

    init {
        val state = savedStateHandle.get<ScreenDriverData>("data")!!
        uiState = MutableStateFlow(DriverOverviewScreenState(
            driverId = state.driverId,
            driverName = state.driverName
        ))
        refresh()
    }

    override fun openUrl(url: String) {
        openWebpageUseCase.open(url = url, title = "")
    }

    override fun openSeason(season: Int) {
        uiState.value = uiState.value.copy(selectedSeason = season)
    }

    override fun back() {
        uiState.value = uiState.value.copy(selectedSeason = null)
    }

    override fun refresh() {
        viewModelScope.launch(ioDispatcher) {
            val driverId = uiState.value.driverId
            if (driverId.isNotEmpty()) {
                populate(driverId)
            }
            uiState.value = uiState.value.copy(isLoading = true, networkAvailable = networkConnectivityManager.isConnected)
            driverRepository.fetchDriver(driverId)
            populate(driverId)
        }
    }

    private suspend fun populate(driverId: String) {
        val overview = driverRepository.getDriverOverview(driverId).firstOrNull()
        val list = overview?.generateResultList()
        uiState.value = uiState.value.copy(
            driver = overview?.driver,
            isLoading = false,
            networkAvailable = networkConnectivityManager.isConnected,
            list = list ?: emptyList()
        )
    }

    private fun DriverHistory.generateResultList(): List<DriverOverviewModel> {
        val list = mutableListOf<DriverOverviewModel>()
        if (this.hasChampionshipCurrentlyInProgress) {
            val latestRound = this.standings.maxByOrNull { it.season }?.raceOverview?.maxByOrNull { it.raceInfo.round }
            if (latestRound != null) {
                list.add(
                    DriverOverviewModel.Message(
                        string.results_accurate_for_year,
                        listOf(
                            latestRound.raceInfo.season,
                            latestRound.raceInfo.name,
                            latestRound.raceInfo.round
                        )
                    )
                )
            }
        }

        // Add general stats
        list.addAll(getAllStats(this))

        // Add constructor history
        list.addStat(
            icon = drawable.ic_team,
            label = string.driver_overview_stat_career_team_history,
            value = ""
        )
        list.addAll(getConstructorItemList(this))
        return list
    }

    /**
     * Add career stats for the driver across their career
     */
    private fun getAllStats(history: DriverHistory): List<DriverOverviewModel> {
        val list: MutableList<DriverOverviewModel> = mutableListOf()
        list.addStat(
            isWinning = history.championshipWins >= 1,
            icon = drawable.ic_driver,
            label = string.driver_overview_stat_career_drivers_title,
            value = history.championshipWins.toString(),
            driverStatHistoryType = if (history.championshipWins >= 1) DriverStatHistoryType.CHAMPIONSHIPS else null
        )

        history.careerBestChampionship?.let {
            list.addStat(
                icon = drawable.ic_championship_order,
                label = string.driver_overview_stat_career_best_championship_position,
                value = it.ordinalAbbreviation
            )
        }

        list.addStat(
            icon = drawable.ic_standings,
            label = string.driver_overview_stat_career_wins,
            value = history.careerWins.toString(),
            driverStatHistoryType = if (history.careerWins >= 1) DriverStatHistoryType.WINS else null
        )
        list.addStat(
            icon = drawable.ic_podium,
            label = string.driver_overview_stat_career_podiums,
            value = history.careerPodiums.toString(),
            driverStatHistoryType = if (history.careerPodiums >= 1) DriverStatHistoryType.PODIUMS else null
        )
        list.addStat(
            icon = drawable.ic_race_starts,
            label = string.driver_overview_stat_race_starts,
            value = history.raceStarts.toString()
        )
        list.addStat(
            icon = drawable.ic_race_finishes,
            label = string.driver_overview_stat_race_finishes,
            value = history.raceFinishes.toString()
        )
        list.addStat(
            icon = drawable.ic_race_retirements,
            label = string.driver_overview_stat_race_retirements,
            value = history.raceRetirements.toString()
        )
        list.addStat(
            icon = drawable.ic_best_finish,
            label = string.driver_overview_stat_career_best_finish,
            value = history.careerBestFinish.ordinalAbbreviation
        )
        list.addStat(
            icon = drawable.ic_finishes_in_points,
            label = string.driver_overview_stat_career_points_finishes,
            value = history.careerFinishesInPoints.toString()
        )
        list.addStat(
            icon = drawable.ic_race_points,
            label = string.driver_overview_stat_career_points,
            value = history.careerPoints.pointsDisplay()
        )
        list.addStat(
            icon = drawable.ic_qualifying_pole,
            label = string.driver_overview_stat_career_qualifying_pole,
            value = history.careerQualifyingPoles.toString(),
            driverStatHistoryType = if (history.careerQualifyingPoles >= 1) DriverStatHistoryType.POLES else null
        )
        list.addStat(
            icon = drawable.ic_qualifying_front_row,
            label = string.driver_overview_stat_career_qualifying_top_3,
            value = history.careerQualifyingTop3.toString()
        )
        list.addStat(
            icon = drawable.ic_qualifying_top_ten,
            label = string.driver_overview_stat_career_qualifying_top_10,
            value = history.totalQualifyingAbove(10).toString()
        )

        return list
    }

    private fun MutableList<DriverOverviewModel>.addStat(
        isWinning: Boolean = false,
        @DrawableRes icon: Int,
        @StringRes label: Int,
        value: String,
        driverStatHistoryType: DriverStatHistoryType? = null
    ) {
        this.add(
            DriverOverviewModel.Stat(
                isWinning = isWinning,
                icon = icon,
                label = label,
                value = value,
                driverStatHistoryType = driverStatHistoryType
            )
        )
    }

    /**
     * Add the directional constructor list at the bottom of the page
     */
    private fun getConstructorItemList(history: DriverHistory): List<DriverOverviewModel> {
        // Team history in chronological order
        // * 2010
        // * 2009
        // * 2008
        // | 2008
        // ....

        val seasonConstructors = history.constructors
            .reversed()
            .groupBy { it.first }
            .toList()
            .map { it.first to it.second.map { it.second } }
            .toList()

        return seasonConstructors
            .mapIndexed { index, pair ->
                val (season, constructor) = pair
                DriverOverviewModel.RacedFor(
                    season, constructor, getPipeType(
                        current = season,
                        newer = seasonConstructors.getOrNull(index - 1)?.first,
                        prev = seasonConstructors.getOrNull(index + 1)?.first
                    ), history.isWorldChampionFor(season)
                )
            }
    }

    private fun getPipeType(current: Int, newer: Int?, prev: Int?): PipeType {
        if (newer == null && prev == null) {
            return PipeType.SINGLE
        }
        when {
            newer == null -> {
                return if (prev!! <= current - 2) {
                    PipeType.SINGLE
                } else {
                    PipeType.START
                }
            }
            prev == null -> {
                return if (newer >= current + 2) {
                    PipeType.SINGLE
                } else {
                    PipeType.END
                }
            }
            else -> {
                return if (newer >= current + 2 && prev <= current - 2) {
                    PipeType.SINGLE
                } else if (prev <= current - 2) {
                    PipeType.END
                } else if (newer >= current + 2) {
                    PipeType.START
                } else {
                    PipeType.START_END
                }
            }
        }
    }
}