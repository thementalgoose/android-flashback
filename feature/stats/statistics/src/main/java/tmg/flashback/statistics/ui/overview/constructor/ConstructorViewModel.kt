package tmg.flashback.statistics.ui.overview.constructor

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
import tmg.flashback.statistics.ui.overview.constructor.summary.ConstructorSummaryItem
import tmg.flashback.statistics.ui.overview.constructor.summary.addError
import tmg.flashback.statistics.ui.overview.driver.summary.PipeType
import tmg.flashback.formula1.model.ConstructorHistory
import tmg.flashback.firebase.extensions.pointsDisplay
import tmg.flashback.statistics.R
import tmg.flashback.statistics.repo.ConstructorRepository
import tmg.flashback.statistics.ui.shared.sync.SyncDataItem
import tmg.flashback.statistics.ui.shared.sync.viewholders.DataUnavailable
import tmg.utilities.extensions.ordinalAbbreviation
import tmg.utilities.lifecycle.DataEvent
import tmg.utilities.lifecycle.Event

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

    val isLoading: LiveData<Boolean>
    val showRefreshError: LiveData<Event>
}

//endregion

@Suppress("EXPERIMENTAL_API_USAGE")
class ConstructorViewModel(
    private val constructorRepository: ConstructorRepository,
    private val connectivityManager: tmg.core.device.managers.NetworkConnectivityManager
): ViewModel(), ConstructorViewModelInputs, ConstructorViewModelOutputs {

    var inputs: ConstructorViewModelInputs = this
    var outputs: ConstructorViewModelOutputs = this

    private val constructorId: ConflatedBroadcastChannel<String> = ConflatedBroadcastChannel()

    override val list: LiveData<List<ConstructorSummaryItem>> = constructorId
        .asFlow()
        .flatMapLatest { constructorRepository.getConstructorOverview(it) }
        .map {
            val list: MutableList<ConstructorSummaryItem> = mutableListOf()
            when (it) {
                null -> {
                    when {
                        !connectivityManager.isConnected ->
                            list.addError(SyncDataItem.NoNetwork)
                        else ->
                            list.addError(SyncDataItem.Unavailable(DataUnavailable.CONSTRUCTOR_NOT_EXIST))
                    }
                }
                else -> {
                    list.add(
                        ConstructorSummaryItem.Header(
                        constructorName = it.constructor.name,
                        constructorColor = it.constructor.color,
                        constructorNationality = it.constructor.nationality,
                        constructorNationalityISO = it.constructor.nationalityISO,
                        constructorWikiUrl = it.constructor.wikiUrl
                    ))

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
        .asLiveData(viewModelScope.coroutineContext)

    override val openUrl: MutableLiveData<DataEvent<String>> = MutableLiveData()
    override val openSeason: MutableLiveData<DataEvent<Pair<String, Int>>> = MutableLiveData()

    override val isLoading: MutableLiveData<Boolean> = MutableLiveData()
    override val showRefreshError: MutableLiveData<Event> = MutableLiveData()

    init {

    }

    //region Inputs

    override fun setup(constructorId: String) {
        if (this.constructorId.valueOrNull != constructorId) {
            this.constructorId.offer(constructorId)
        }
    }

    override fun openUrl(url: String) {
        openUrl.postValue(DataEvent(url))
    }

    override fun openSeason(season: Int) {
        openSeason.postValue(DataEvent(Pair(constructorId.value, season)))
    }

    override fun refresh() {
        isLoading.value = true
        viewModelScope.launch(context = Dispatchers.IO) {
            val result = constructorRepository.fetchConstructor(constructorId.value)
            isLoading.postValue(false)
            if (!result) {
                showRefreshError.postValue(Event())
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
//        list.addStat(
//                icon = R.drawable.ic_status_finished,
//                label = R.string.constructor_overview_stat_best_finish,
//                value = overview.bestFinish.position()
//        )
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
//        list.addStat(
//                icon = R.drawable.ic_qualifying_front_row,
//                label = R.string.constructor_overview_stat_qualifying_top_3,
//                value = overview.totalQualifyingTop3.toString()
//        )

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

    fun getPipeType(currentItem: Int, newer: Int?, prev: Int?): PipeType {
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
