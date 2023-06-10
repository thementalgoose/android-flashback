package tmg.flashback.constructors.ui.overview

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import tmg.flashback.constructors.R
import tmg.flashback.constructors.contract.ConstructorSeason
import tmg.flashback.constructors.contract.with
import tmg.flashback.device.managers.NetworkConnectivityManager
import tmg.flashback.formula1.extensions.pointsDisplay
import tmg.flashback.formula1.model.ConstructorHistory
import tmg.flashback.navigation.Screen
import tmg.flashback.domain.repo.ConstructorRepository
import tmg.flashback.ui.components.navigation.PipeType
import tmg.flashback.web.usecases.OpenWebpageUseCase
import tmg.utilities.extensions.ordinalAbbreviation
import javax.inject.Inject


//region Inputs

interface ConstructorOverviewViewModelInputs {
    fun setup(constructorId: String, constructorName: String)
    fun openUrl(url: String)
    fun openSeason(season: Int)

    fun refresh()
}

//endregion

//region Outputs

interface ConstructorOverviewViewModelOutputs {
    val list: StateFlow<List<ConstructorOverviewModel>>

    val showLoading: StateFlow<Boolean>
}

//endregion

@Suppress("EXPERIMENTAL_API_USAGE")
@HiltViewModel
class ConstructorOverviewViewModel @Inject constructor(
    private val constructorRepository: ConstructorRepository,
    private val networkConnectivityManager: NetworkConnectivityManager,
    private val openWebpageUseCase: OpenWebpageUseCase,
    private val navigator: tmg.flashback.navigation.Navigator,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel(), ConstructorOverviewViewModelInputs, ConstructorOverviewViewModelOutputs {

    var inputs: ConstructorOverviewViewModelInputs = this
    var outputs: ConstructorOverviewViewModelOutputs = this

    private val constructorIdAndName: MutableStateFlow<Pair<String, String>?> = MutableStateFlow(null)
    private val constructorIdWithRequest: Flow<Pair<String, String>?> = constructorIdAndName
        .filterNotNull()
        .flatMapLatest { id ->
            return@flatMapLatest flow {
                if (constructorRepository.getConstructorSeasonCount(id.first) == 0) {
                    showLoading.value = true
                    emit(null)
                    constructorRepository.fetchConstructor(id.first)
                    showLoading.value = false
                    emit(id)
                }
                else {
                    emit(id)
                    showLoading.value = true
                    constructorRepository.fetchConstructor(id.first)
                    showLoading.value = false
                }
            }
        }
        .flowOn(ioDispatcher)

    private val isConnected: Boolean
        get() = networkConnectivityManager.isConnected

    override val list: StateFlow<List<ConstructorOverviewModel>> = constructorIdWithRequest
        .flatMapLatest { idAndName ->

            if (idAndName == null) {
                return@flatMapLatest flow {
                    emit(mutableListOf<ConstructorOverviewModel>(ConstructorOverviewModel.Loading))
                }
            }

            val (id, name) = idAndName
            return@flatMapLatest constructorRepository.getConstructorOverview(id)
                .map {
                    val list: MutableList<ConstructorOverviewModel> = mutableListOf()
                    if (it != null) {
                        list.add(
                            ConstructorOverviewModel.Header(
                                constructorName = it.constructor.name,
                                constructorPhotoUrl = it.constructor.photoUrl,
                                constructorColor = it.constructor.color,
                                constructorNationality = it.constructor.nationality,
                                constructorNationalityISO = it.constructor.nationalityISO,
                                constructorWikiUrl = it.constructor.wikiUrl
                            )
                        )
                    }
                    when {
                        (it == null || it.standings.isEmpty()) && !isConnected -> list.add(
                            ConstructorOverviewModel.NetworkError
                        )
                        (it == null || it.standings.isEmpty()) -> list.add(ConstructorOverviewModel.InternalError)
                        else -> {
                            if (it.hasChampionshipCurrentlyInProgress) {
                                val lastStanding = it.standings.maxByOrNull { it.season }
                                if (lastStanding != null) {
                                    list.add(
                                        ConstructorOverviewModel.Message(
                                            R.string.results_accurate_for_round,
                                            listOf(lastStanding.season, lastStanding.races)
                                        )
                                    )
                                }
                            }
                            list.addAll(getAllStats(it))
                            list.add(ConstructorOverviewModel.ListHeader)
                            list.addAll(getHistory(it))
                        }
                    }
                    return@map list
                }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, listOf(ConstructorOverviewModel.Loading))

    override val showLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)


    //region Inputs

    override fun setup(constructorId: String, constructorName: String) {
        this.constructorIdAndName.value = Pair(constructorId, constructorName)
    }

    override fun openUrl(url: String) {
        openWebpageUseCase.open(url = url, title = "")
    }

    override fun openSeason(season: Int) {
        constructorIdAndName.value?.let { (id, name) ->
            navigator.navigate(
                Screen.ConstructorSeason.with(
                constructorId = id,
                constructorName = name,
                season = season
            ))
        }
    }

    override fun refresh() {
        this.refresh(constructorIdAndName.value)
    }
    private fun refresh(constructorId: Pair<String, String>? = this.constructorIdAndName.value) {
        viewModelScope.launch(context = ioDispatcher) {
            constructorId?.let {
                constructorRepository.fetchConstructor(it.first)
                showLoading.value = false
            }
        }
    }

    //endregion

    //region Outputs

    //endregion

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