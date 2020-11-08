package tmg.flashback.overviews.driver

import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import tmg.flashback.R
import tmg.flashback.base.BaseViewModel
import tmg.flashback.currentYear
import tmg.flashback.di.async.ScopeProvider
import tmg.flashback.overviews.driver.summary.DriverSummaryItem
import tmg.flashback.overviews.driver.summary.RaceForPositionType
import tmg.flashback.overviews.driver.summary.addError
import tmg.flashback.repo.db.stats.DriverDB
import tmg.flashback.repo.models.stats.DriverOverview
import tmg.flashback.settings.ConnectivityManager
import tmg.flashback.shared.SyncDataItem
import tmg.flashback.shared.viewholders.DataUnavailable
import tmg.flashback.utils.position
import tmg.utilities.extensions.ordinalAbbreviation
import tmg.utilities.lifecycle.DataEvent

//region Inputs

interface DriverViewModelInputs {
    fun setup(driverId: String)
    fun openUrl(url: String)
    fun openSeason(season: Int)
}

//endregion

//region Outputs

interface DriverViewModelOutputs {
    val list: LiveData<List<DriverSummaryItem>>
    val openUrl: LiveData<DataEvent<String>>
    val openSeason: LiveData<DataEvent<Pair<String, Int>>>
}

//endregion


@Suppress("EXPERIMENTAL_API_USAGE")
class DriverViewModel(
    private val driverDB: DriverDB,
    private val connectivityManager: ConnectivityManager,
    scopeProvider: ScopeProvider
): BaseViewModel(
    scopeProvider
), DriverViewModelInputs, DriverViewModelOutputs {

    var inputs: DriverViewModelInputs = this
    var outputs: DriverViewModelOutputs = this

    private val driverId: ConflatedBroadcastChannel<String> = ConflatedBroadcastChannel()
    override val list: LiveData<List<DriverSummaryItem>> = driverId
        .asFlow()
        .flatMapLatest { driverDB.getDriverOverview(it) }
        .map {
            val list: MutableList<DriverSummaryItem> = mutableListOf()
            when (it) {
                null -> {
                    when {
                        !connectivityManager.isConnected ->
                            list.addError(SyncDataItem.NoNetwork)
                        else ->
                            list.addError(SyncDataItem.Unavailable(DataUnavailable.DRIVER_NOT_EXIST))
                    }
                }
                else -> {
                    list.add(DriverSummaryItem.Header(
                        driverFirstname = it.firstName,
                        driverSurname = it.lastName,
                        driverNumber = it.number,
                        driverImg = it.photoUrl ?: "",
                        driverBirthday = it.dateOfBirth,
                        driverWikiUrl = it.wikiUrl,
                        driverNationalityISO = it.nationalityISO
                    ))

                    if (it.hasChampionshipCurrentlyInProgress) {
                        val latestRound = it.standings.maxBy { it.season }?.raceOverview?.maxBy { it.round }
                        if (latestRound != null) {
                            list.add(DriverSummaryItem.ErrorItem(SyncDataItem.MessageRes(R.string.results_accurate_for_year, listOf(latestRound.season, latestRound.raceName, latestRound.round))))
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
        .asLiveData(scope.coroutineContext)

    override val openUrl: MutableLiveData<DataEvent<String>> = MutableLiveData()
    override val openSeason: MutableLiveData<DataEvent<Pair<String, Int>>> = MutableLiveData()

    init {

    }

    //region Inputs

    override fun setup(driverId: String) {
        if (this.driverId.valueOrNull != driverId) {
            this.driverId.offer(driverId)
        }
    }

    override fun openUrl(url: String) {
        openUrl.postValue(DataEvent(url))
    }

    override fun openSeason(season: Int) {
        openSeason.postValue(DataEvent(Pair(driverId.value, season)))
    }

    //endregion

    //region Outputs

    //endregion

    /**
     * Add career stats for the driver across their career
     */
    private fun getAllStats(overview: DriverOverview): List<DriverSummaryItem> {
        val list: MutableList<DriverSummaryItem> = mutableListOf()
        list.addStat(
            tint = if (overview.championshipWins > 0) R.attr.f1Favourite else R.attr.f1TextSecondary,
            icon = R.drawable.ic_menu_drivers,
            label = R.string.driver_overview_stat_career_drivers_title,
            value = overview.championshipWins.toString()
        )

        overview.careerBestChampionship?.let {
            list.addStat(
                icon = R.drawable.ic_championship_order,
                label = R.string.driver_overview_stat_career_best_championship_position,
                value = it.ordinalAbbreviation
            )
        }

        list.addStat(
            icon = R.drawable.ic_standings,
            label = R.string.driver_overview_stat_career_wins,
            value = overview.careerWins.toString()
        )
        list.addStat(
            icon = R.drawable.ic_podium,
            label = R.string.driver_overview_stat_career_podiums,
            value = overview.careerPodiums.toString()
        )
        list.addStat(
            icon = R.drawable.ic_status_finished,
            label = R.string.driver_overview_stat_career_best_finish,
            value = overview.careerBestFinish.position()
        )
        list.addStat(
            icon = R.drawable.ic_finishes_in_points,
            label = R.string.driver_overview_stat_career_points_finishes,
            value = overview.careerFinishesInPoints.toString()
        )
        list.addStat(
            icon = R.drawable.ic_race_points,
            label = R.string.driver_overview_stat_career_points,
            value = overview.careerPoints.toString()
        )
        list.addStat(
            icon = R.drawable.ic_qualifying_pole,
            label = R.string.driver_overview_stat_career_qualifying_pole,
            value = overview.careerQualifyingPoles.toString()
        )
        list.addStat(
            icon = R.drawable.ic_qualifying_front_row,
            label = R.string.driver_overview_stat_career_qualifying_top_3,
            value = overview.careerQualifyingTop3.toString()
        )
        list.addStat(
            icon = R.drawable.ic_qualifying_top_ten,
            label = R.string.driver_overview_stat_career_qualifying_top_10,
            value = overview.totalQualifyingAbove(10).toString()
        )

        return list
    }

    private fun MutableList<DriverSummaryItem>.addStat(@AttrRes tint: Int = R.attr.f1TextSecondary, @DrawableRes icon: Int, @StringRes label: Int, value: String) {
        this.add(
            DriverSummaryItem.Stat(
            tint = tint,
            icon = icon,
            label = label,
            value = value
        ))
    }

    /**
     * Add the directional constructor list at the bottom of the page
     */
    private fun getConstructorItemList(overview: DriverOverview): List<DriverSummaryItem> {
        return this.generateConstructorsListInDescendingOrder(overview)
            .reversed()
            .map {
                val type = when (it.type) {
                    RaceForPositionType.START -> RaceForPositionType.END
                    RaceForPositionType.END -> RaceForPositionType.START
                    else -> it.type
                }
                DriverSummaryItem.RacedFor(it.season, it.constructors, type, overview.isWorldChampionFor(it.season))
            }
    }

    private fun generateConstructorsListInDescendingOrder(overview: DriverOverview): List<DriverSummaryItem.RacedFor> {
        // Team history in chronological order
        // * 2008
        // | 2008
        // * 2009
        // * 2010
        // ....
        return overview.constructors.mapIndexed { index, pair ->
            val (season, constructor) = pair
            val nextItem = overview.constructors.getOrNull(index + 1)
            // Handle first item
            val dotType: RaceForPositionType
            // First year
            if (index == 0) {
                dotType = when {
                    // Raced for one and only year
                    nextItem == null -> {
                        RaceForPositionType.SINGLE
                    }
                    // Raced for one year, then took one or more years off but returned later
                    nextItem.first >= season + 2 -> {
                        RaceForPositionType.SINGLE
                    }
                    // Next result is either same year constructor change or next year.
                    else -> {
                        RaceForPositionType.START
                    }
                }
            }
            // Next year
            else {
                val previousItem = overview.constructors[index - 1]
                dotType = when {
                    // Nothing afterwards. End of career
                    nextItem == null -> {
                        when {
                            previousItem.first <= season - 2 && currentYear == season -> RaceForPositionType.START
                            previousItem.first <= season - 2 && currentYear != season -> RaceForPositionType.SINGLE
                            currentYear == season -> RaceForPositionType.SEASON
                            else -> RaceForPositionType.END
                        }
                    }

                    // Driver took one or more years off. Last item should be end, so need start
                    previousItem.first <= season - 2 -> {
                        RaceForPositionType.START
                    }
                    // Ending before temporarily retiring
                    nextItem.first >= season + 2 -> {
                        RaceForPositionType.END
                    }
                    // Next year constructor
                    previousItem.first == season - 1 -> {
                        RaceForPositionType.SEASON
                    }
                    // Same year, constructor change mid season
                    else -> {
                        RaceForPositionType.MID_SEASON_CHANGE
                    }
                }
            }

            DriverSummaryItem.RacedFor(season, constructor, dotType, overview.isWorldChampionFor(season))
        }
    }
}
