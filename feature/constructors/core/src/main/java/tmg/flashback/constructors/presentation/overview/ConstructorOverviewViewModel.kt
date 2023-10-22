package tmg.flashback.constructors.presentation.overview

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import tmg.flashback.constructors.R
import tmg.flashback.constructors.contract.model.ScreenConstructorData
import tmg.flashback.device.managers.NetworkConnectivityManager
import tmg.flashback.domain.repo.ConstructorRepository
import tmg.flashback.formula1.extensions.pointsDisplay
import tmg.flashback.formula1.model.ConstructorHistory
import tmg.flashback.ui.components.navigation.PipeType
import tmg.flashback.web.usecases.OpenWebpageUseCase
import tmg.utilities.extensions.ordinalAbbreviation
import javax.inject.Inject


//region Inputs

interface ConstructorOverviewViewModelInputs {
    fun openUrl(url: String)
    fun openSeason(season: Int)

    fun refresh()
    fun back()
}

//endregion

//region Outputs

interface ConstructorOverviewViewModelOutputs {
    val uiState: StateFlow<ConstructorOverviewScreenState>
}

//endregion

@Suppress("EXPERIMENTAL_API_USAGE")
@HiltViewModel
class ConstructorOverviewViewModel @Inject constructor(
    private val constructorRepository: ConstructorRepository,
    private val openWebpageUseCase: OpenWebpageUseCase,
    private val networkConnectivityManager: NetworkConnectivityManager,
    savedStateHandle: SavedStateHandle,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel(), ConstructorOverviewViewModelInputs, ConstructorOverviewViewModelOutputs {

    var inputs: ConstructorOverviewViewModelInputs = this
    var outputs: ConstructorOverviewViewModelOutputs = this

    override val uiState: MutableStateFlow<ConstructorOverviewScreenState>

    init {
        val state = savedStateHandle.get<ScreenConstructorData>("data")!!
        uiState = MutableStateFlow(ConstructorOverviewScreenState(
            constructorId = state.constructorId,
            constructorName = state.constructorName
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
            val constructorId = uiState.value.constructorId
            if (constructorId.isNotEmpty()) {
                populate()
            }
            uiState.value = uiState.value.copy(isLoading = true, networkAvailable = networkConnectivityManager.isConnected)
            constructorRepository.fetchConstructor(constructorId)
            populate()
        }
    }

    private suspend fun populate() {
        val constructorId = uiState.value.constructorId
        val overview = constructorRepository.getConstructorOverview(constructorId).firstOrNull()
        val list = overview?.generateResultList()
        uiState.value = uiState.value.copy(
            constructor = overview?.constructor,
            isLoading = false,
            networkAvailable = networkConnectivityManager.isConnected,
            list = list ?: emptyList()
        )
    }

    private fun ConstructorHistory.generateResultList(): List<ConstructorOverviewModel> {
        val list = mutableListOf<ConstructorOverviewModel>()
        if (this.hasChampionshipCurrentlyInProgress) {
            val lastStanding = this.standings.maxByOrNull { it.season }
            if (lastStanding != null) {
                list.add(
                    ConstructorOverviewModel.Message(
                        R.string.results_accurate_for_round,
                        listOf(lastStanding.season, lastStanding.races)
                    )
                )
            }
        }
        list.addAll(getAllStats(this))
        list.add(ConstructorOverviewModel.ListHeader)
        list.addAll(getHistory(this))
        return list
    }

    /**
     * Add career stats for the driver across their career
     */
    private fun getAllStats(history: ConstructorHistory): List<ConstructorOverviewModel> {
        val list: MutableList<ConstructorOverviewModel> = mutableListOf()

        list.addStat(
            isWinning = history.championshipWins > 0,
            icon = R.drawable.ic_menu_constructors,
            label = R.string.constructor_overview_stat_titles,
            value = history.championshipWins.toString()
        )

        history.bestChampionship?.let {
            if (it != 1) {
                list.addStat(
                    icon = R.drawable.ic_championship_order,
                    label = R.string.constructor_overview_stat_best_championship_position,
                    value = it.ordinalAbbreviation
                )
            }
        }

        list.addStat(
            isWinning = history.driversChampionships > 0,
            icon = R.drawable.ic_menu_drivers,
            label = R.string.constructor_overview_stat_drivers_titles,
            value = history.driversChampionships.toString()
        )

        list.addStat(
            icon = R.drawable.ic_race_grid,
            label = R.string.constructor_overview_stat_races,
            value = history.races.toString()
        )
        list.addStat(
            icon = R.drawable.ic_standings,
            label = R.string.constructor_overview_stat_race_wins,
            value = history.totalWins.toString()
        )
        list.addStat(
            icon = R.drawable.ic_podium,
            label = R.string.constructor_overview_stat_race_podiums,
            value = history.totalPodiums.toString()
        )
        list.addStat(
            icon = R.drawable.ic_race_points,
            label = R.string.constructor_overview_stat_points,
            value = history.totalPoints.pointsDisplay()
        )
        list.addStat(
            icon = R.drawable.ic_finishes_in_points,
            label = R.string.constructor_overview_stat_points_finishes,
            value = history.finishesInPoints.toString()
        )
        list.addStat(
            icon = R.drawable.ic_qualifying_pole,
            label = R.string.constructor_overview_stat_qualifying_poles,
            value = history.totalQualifyingPoles.toString()
        )

        return list
    }

    private fun MutableList<ConstructorOverviewModel>.addStat(isWinning: Boolean = false, @DrawableRes icon: Int, @StringRes label: Int, value: String) {
        this.add(
            ConstructorOverviewModel.Stat(
                isWinning = isWinning,
                icon = icon,
                label = label,
                value = value
            )
        )
    }

    private fun getHistory(history: ConstructorHistory): List<ConstructorOverviewModel> {
        val sortedList = history.standings.sortedByDescending { it.season }
        return sortedList
            .mapIndexed { index, item ->
                ConstructorOverviewModel.History(
                    pipe = getPipeType(
                        currentItem = item.season,
                        newer = sortedList.getOrNull(index - 1)?.season,
                        prev = sortedList.getOrNull(index + 1)?.season
                    ),
                    season = item.season,
                    championshipPosition = item.championshipStanding,
                    points = item.points,
                    isInProgress = item.isInProgress,
                    drivers = item.drivers.values
                        .sortedByDescending { it.points }
                )
            }
    }

    private fun getPipeType(currentItem: Int, newer: Int?, prev: Int?): PipeType {
        if (newer == null && prev == null) {
            return PipeType.SINGLE
        }
        when {
            newer == null -> {
                return if (prev!! == currentItem - 1) {
                    PipeType.START
                } else {
                    PipeType.SINGLE
                }
            }
            prev == null -> {
                return if (newer == currentItem + 1) {
                    PipeType.END
                } else {
                    PipeType.SINGLE
                }
            }
            else -> {
                return if (newer == currentItem + 1 && prev == currentItem - 1) {
                    PipeType.START_END
                } else if (newer == currentItem + 1) {
                    PipeType.END
                } else if (prev == currentItem - 1) {
                    PipeType.START
                } else {
                    PipeType.SINGLE
                }
            }
        }
    }
}