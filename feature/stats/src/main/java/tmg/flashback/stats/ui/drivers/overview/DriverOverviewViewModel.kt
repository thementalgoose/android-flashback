package tmg.flashback.stats.ui.drivers.overview

import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import tmg.flashback.device.managers.NetworkConnectivityManager
import tmg.flashback.formula1.extensions.pointsDisplay
import tmg.flashback.formula1.model.DriverHistory
import tmg.flashback.statistics.repo.DriverRepository
import tmg.flashback.stats.R
import tmg.utilities.extensions.ordinalAbbreviation
import tmg.utilities.lifecycle.DataEvent
import tmg.utilities.lifecycle.Event

//region Inputs

interface DriverOverviewViewModelInputs {
    fun setup(driverId: String)
    fun openUrl(url: String)
    fun openSeason(season: Int)

    fun refresh()
}

//endregion

//region Outputs

interface DriverOverviewViewModelOutputs {
    val list: LiveData<List<DriverOverviewModel>>
    val openUrl: LiveData<DataEvent<String>>
    val openSeason: LiveData<DataEvent<Pair<String, Int>>>
    val showLoading: LiveData<Boolean>
    val showRefreshError: LiveData<Event>
}

//endregion


@Suppress("EXPERIMENTAL_API_USAGE")
class DriverOverviewViewModel(
    private val driverRepository: DriverRepository,
    private val networkConnectivityManager: NetworkConnectivityManager,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel(), DriverOverviewViewModelInputs, DriverOverviewViewModelOutputs {

    var inputs: DriverOverviewViewModelInputs = this
    var outputs: DriverOverviewViewModelOutputs = this

    private val isConnected: Boolean
        get() = networkConnectivityManager.isConnected

    private val driverId: MutableStateFlow<String?> = MutableStateFlow(null)
    private val driverIdWithRequest: Flow<String?> = driverId
        .filterNotNull()
        .flatMapLatest { id ->
            return@flatMapLatest flow {
                if (driverRepository.getDriverSeasonCount(id) == 0) {
                    showLoading.postValue(true)
                    emit(null)
                    driverRepository.fetchDriver(id)
                    showLoading.postValue(false)
                    emit(id)
                }
                else {
                    emit(id)
                }
            }
        }
        .flowOn(ioDispatcher)

    override val list: LiveData<List<DriverOverviewModel>> = driverIdWithRequest
        .flatMapLatest { id ->

            if (id == null) {
                return@flatMapLatest flow {
                    emit(mutableListOf<DriverOverviewModel>(DriverOverviewModel.ErrorItem(SyncDataItem.Skeleton)))
                }
            }

            return@flatMapLatest driverRepository.getDriverOverview(id)
                .map {
                    val list: MutableList<DriverOverviewModel> = mutableListOf()
                    if (it != null) {
                        list.add(
                            DriverOverviewModel.Header(
                                driverFirstname = it.driver.firstName,
                                driverSurname = it.driver.lastName,
                                driverNumber = it.driver.number,
                                driverImg = it.driver.photoUrl ?: "",
                                driverBirthday = it.driver.dateOfBirth,
                                driverWikiUrl = it.driver.wikiUrl ?: "",
                                driverNationalityISO = it.driver.nationalityISO,
                                constructors = it.constructors.map { (_, constructor) -> constructor }
                            )
                        )
                    }
                    when {
                        (it == null || it.standings.isEmpty()) && !isConnected -> list.addError(SyncDataItem.PullRefresh)
                        (it == null || it.standings.isEmpty()) -> list.addError(SyncDataItem.Unavailable(DataUnavailable.CONSTRUCTOR_HISTORY_INTERNAL_ERROR))
                        else -> {
                            if (it.hasChampionshipCurrentlyInProgress) {
                                val latestRound = it.standings.maxByOrNull { it.season }?.raceOverview?.maxByOrNull { it.raceInfo.round }
                                if (latestRound != null) {
                                    list.add(DriverOverviewModel.ErrorItem(SyncDataItem.MessageRes(R.string.results_accurate_for_year, listOf(latestRound.raceInfo.season, latestRound.raceInfo.name, latestRound.raceInfo.round))))
                                }
                            }

                            // Add general stats
                            list.addAll(getAllStats(it))

                            // Add constructor history
                            list.addStat(
                                icon = R.drawable.ic_team,
                                label = R.string.driver_overview_stat_career_team_history,
                                value = ""
                            )
                            list.addAll(getConstructorItemList(it))
                        }
                    }
                    return@map list
                }
        }
        .asLiveData(viewModelScope.coroutineContext)

    override val openUrl: MutableLiveData<DataEvent<String>> = MutableLiveData()
    override val openSeason: MutableLiveData<DataEvent<Pair<String, Int>>> = MutableLiveData()
    override val showLoading: MutableLiveData<Boolean> = MutableLiveData()
    override val showRefreshError: MutableLiveData<Event> = MutableLiveData()

    init {

    }

    //region Inputs

    override fun setup(driverId: String) {
        this.driverId.value = driverId
    }

    override fun openUrl(url: String) {
        openUrl.postValue(DataEvent(url))
    }

    override fun openSeason(season: Int) {
        driverId.value?.let {
            openSeason.postValue(DataEvent(Pair(it, season)))
        }
    }

    override fun refresh() {
        this.refresh(driverId.value)
    }
    private fun refresh(driverId: String? = this.driverId.value) {
        viewModelScope.launch(context = ioDispatcher) {
            driverId?.let {
                driverRepository.fetchDriver(driverId)
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
    private fun getAllStats(history: DriverHistory): List<DriverOverviewModel> {
        val list: MutableList<DriverOverviewModel> = mutableListOf()
        list.addStat(
            icon = R.drawable.ic_menu_drivers,
            label = R.string.driver_overview_stat_career_drivers_title,
            value = history.championshipWins.toString()
        )

        history.careerBestChampionship?.let {
            list.addStat(
                icon = R.drawable.ic_championship_order,
                label = R.string.driver_overview_stat_career_best_championship_position,
                value = it.ordinalAbbreviation
            )
        }

        list.addStat(
            icon = R.drawable.ic_standings,
            label = R.string.driver_overview_stat_career_wins,
            value = history.careerWins.toString()
        )
        list.addStat(
            icon = R.drawable.ic_podium,
            label = R.string.driver_overview_stat_career_podiums,
            value = history.careerPodiums.toString()
        )
        list.addStat(
            icon = R.drawable.ic_race_starts,
            label = R.string.driver_overview_stat_race_starts,
            value = history.raceStarts.toString()
        )
        list.addStat(
            icon = R.drawable.ic_race_finishes,
            label = R.string.driver_overview_stat_race_finishes,
            value = history.raceFinishes.toString()
        )
        list.addStat(
            icon = R.drawable.ic_race_retirements,
            label = R.string.driver_overview_stat_race_retirements,
            value = history.raceRetirements.toString()
        )
        list.addStat(
            icon = R.drawable.ic_best_finish,
            label = R.string.driver_overview_stat_career_best_finish,
            value = history.careerBestFinish.ordinalAbbreviation
        )
        list.addStat(
            icon = R.drawable.ic_finishes_in_points,
            label = R.string.driver_overview_stat_career_points_finishes,
            value = history.careerFinishesInPoints.toString()
        )
        list.addStat(
            icon = R.drawable.ic_race_points,
            label = R.string.driver_overview_stat_career_points,
            value = history.careerPoints.pointsDisplay()
        )
        list.addStat(
            icon = R.drawable.ic_qualifying_pole,
            label = R.string.driver_overview_stat_career_qualifying_pole,
            value = history.careerQualifyingPoles.toString()
        )
        list.addStat(
            icon = R.drawable.ic_qualifying_front_row,
            label = R.string.driver_overview_stat_career_qualifying_top_3,
            value = history.careerQualifyingTop3.toString()
        )
        list.addStat(
            icon = R.drawable.ic_qualifying_top_ten,
            label = R.string.driver_overview_stat_career_qualifying_top_10,
            value = history.totalQualifyingAbove(10).toString()
        )

        return list
    }

    private fun MutableList<DriverOverviewModel>.addStat(@AttrRes tint: Int = R.attr.contentSecondary, @DrawableRes icon: Int, @StringRes label: Int, value: String) {
        this.add(
            DriverOverviewModel.Stat(
                tint = tint,
                icon = icon,
                label = label,
                value = value
            ))
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
                DriverOverviewModel.RacedFor(season, constructor, getPipeType(
                    current = season,
                    newer = seasonConstructors.getOrNull(index - 1)?.first,
                    prev = seasonConstructors.getOrNull(index + 1)?.first
                ), history.isWorldChampionFor(season))
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

    private fun MutableList<DriverOverviewModel>.addError(syncDataItem: SyncDataItem) {
        this.add(
            DriverOverviewModel.ErrorItem(
                syncDataItem
            )
        )
    }
}