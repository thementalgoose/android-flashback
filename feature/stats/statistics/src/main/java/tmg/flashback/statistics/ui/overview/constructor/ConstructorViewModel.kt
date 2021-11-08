package tmg.flashback.statistics.ui.overview.constructor

import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import tmg.core.device.managers.NetworkConnectivityManager
import tmg.flashback.formula1.extensions.pointsDisplay
import tmg.flashback.formula1.model.ConstructorHistory
import tmg.flashback.statistics.R
import tmg.flashback.statistics.repo.ConstructorRepository
import tmg.flashback.statistics.ui.overview.constructor.summary.ConstructorSummaryItem
import tmg.flashback.statistics.ui.overview.driver.summary.PipeType
import tmg.flashback.statistics.ui.shared.sync.SyncDataItem
import tmg.flashback.statistics.ui.shared.sync.viewholders.DataUnavailable
import tmg.utilities.extensions.ordinalAbbreviation
import tmg.utilities.lifecycle.DataEvent

//region Inputs

interface ConstructorViewModelInputs {
    fun setup(constructorId: String)
    fun openUrl(url: String)
    fun openSeason(season: Int)

    fun refresh()
}

//endregion

//region Outputs

interface ConstructorViewModelOutputs {
    val list: LiveData<List<ConstructorSummaryItem>>
    val openUrl: LiveData<DataEvent<String>>
    val openSeason: LiveData<DataEvent<Pair<String, Int>>>

    val showLoading: LiveData<Boolean>
}

//endregion

@Suppress("EXPERIMENTAL_API_USAGE")
class ConstructorViewModel(
    private val constructorRepository: ConstructorRepository,
    private val networkConnectivityManager: NetworkConnectivityManager,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel(), ConstructorViewModelInputs, ConstructorViewModelOutputs {

    var inputs: ConstructorViewModelInputs = this
    var outputs: ConstructorViewModelOutputs = this

    private val constructorId: MutableStateFlow<String?> = MutableStateFlow(null)
    private val constructorIdWithRequest: Flow<String?> = constructorId
        .filterNotNull()
        .flatMapLatest { id ->
            return@flatMapLatest flow {
                if (constructorRepository.getConstructorSeasonCount(id) == 0) {
                    showLoading.postValue(true)
                    emit(null)
                    val result = constructorRepository.fetchConstructor(id)
                    showLoading.postValue(false)
                    emit(id)
                }
                else {
                    emit(id)
                }
            }
        }
        .flowOn(ioDispatcher)

    private val isConnected: Boolean
        get() = networkConnectivityManager.isConnected

    override val list: LiveData<List<ConstructorSummaryItem>> = constructorIdWithRequest
        .flatMapLatest { id ->

            if (id == null) {
                return@flatMapLatest flow {
                    emit(listOf<ConstructorSummaryItem>(ConstructorSummaryItem.ErrorItem(SyncDataItem.Skeleton)))
                }
            }

            return@flatMapLatest constructorRepository.getConstructorOverview(id)
                .map {
                    val list: MutableList<ConstructorSummaryItem> = mutableListOf()
                    if (it != null) {
                        list.add(
                            ConstructorSummaryItem.Header(
                                constructorName = it.constructor.name,
                                constructorColor = it.constructor.color,
                                constructorNationality = it.constructor.nationality,
                                constructorNationalityISO = it.constructor.nationalityISO,
                                constructorWikiUrl = it.constructor.wikiUrl
                            )
                        )
                    }
                    when {
                        (it == null || it.standings.isEmpty()) && !isConnected -> list.addError(SyncDataItem.PullRefresh)
                        (it == null || it.standings.isEmpty()) -> list.addError(SyncDataItem.Unavailable(DataUnavailable.CONSTRUCTOR_HISTORY_INTERNAL_ERROR))
                        else -> {
                            if (it.hasChampionshipCurrentlyInProgress) {
                                val lastStanding = it.standings.maxByOrNull { it.season }
                                if (lastStanding != null) {
                                    list.add(ConstructorSummaryItem.ErrorItem(SyncDataItem.MessageRes(R.string.results_accurate_for_round, listOf(lastStanding.season, lastStanding.races))))
                                }
                            }
                            list.addAll(getAllStats(it))
                            list.add(ConstructorSummaryItem.ListHeader)
                            list.addAll(getHistory(it))
                        }
                    }
                    return@map list
                }
        }
        .asLiveData(viewModelScope.coroutineContext)

    override val openUrl: MutableLiveData<DataEvent<String>> = MutableLiveData()
    override val openSeason: MutableLiveData<DataEvent<Pair<String, Int>>> = MutableLiveData()

    override val showLoading: MutableLiveData<Boolean> = MutableLiveData()

    init {

    }

    //region Inputs

    override fun setup(constructorId: String) {
        this.constructorId.value = constructorId
    }

    override fun openUrl(url: String) {
        openUrl.postValue(DataEvent(url))
    }

    override fun openSeason(season: Int) {
        constructorId.value?.let {
            openSeason.postValue(DataEvent(Pair(it, season)))
        }
    }

    override fun refresh() {
        this.refresh(constructorId.value)
    }
    private fun refresh(constructorId: String? = this.constructorId.value) {
        viewModelScope.launch(context = ioDispatcher) {
            constructorId?.let {
                val result = constructorRepository.fetchConstructor(it)
                showLoading.postValue(false)
            }
        }
    }

    //endregion

    //region Outputs

    //endregion

    /**
     * Add career stats for the driver across their career
     */
    private fun getAllStats(history: ConstructorHistory): List<ConstructorSummaryItem> {
        val list: MutableList<ConstructorSummaryItem> = mutableListOf()

        list.addStat(
                tint = if (history.championshipWins > 0) R.attr.f1Championship else R.attr.contentSecondary,
                icon = R.drawable.ic_menu_constructors,
                label = R.string.constructor_overview_stat_titles,
                value = history.championshipWins.toString()
        )

        history.bestChampionship?.let {
            list.addStat(
                    icon = R.drawable.ic_championship_order,
                    label = R.string.constructor_overview_stat_best_championship_position,
                    value = it.ordinalAbbreviation
            )
        }

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

    private fun MutableList<ConstructorSummaryItem>.addStat(@AttrRes tint: Int = R.attr.contentSecondary, @DrawableRes icon: Int, @StringRes label: Int, value: String) {
        this.add(
                ConstructorSummaryItem.Stat(
                        tint = tint,
                        icon = icon,
                        label = label,
                        value = value
                ))
    }

    private fun getHistory(history: ConstructorHistory): List<ConstructorSummaryItem> {
        val sortedList = history.standings.sortedByDescending { it.season }
        return sortedList
                .mapIndexed { index, item ->
                    ConstructorSummaryItem.History(
                            pipe = getPipeType(
                                currentItem = item.season,
                                newer = sortedList.getOrNull(index - 1)?.season,
                                prev = sortedList.getOrNull(index + 1)?.season
                            ),
                            season = item.season,
                            colour = history.constructor.color,
                            championshipPosition = item.championshipStanding,
                            points = item.points,
                            isInProgress = item.isInProgress,
                            drivers = item.drivers.values.toList()
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

private fun MutableList<ConstructorSummaryItem>.addError(syncDataItem: SyncDataItem) {
    this.add(
        ConstructorSummaryItem.ErrorItem(
            syncDataItem
        )
    )
}
