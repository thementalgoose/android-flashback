package tmg.flashback.driver.overview

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.android.synthetic.main.view_driver_overview_raced_for.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import org.threeten.bp.LocalDate
import tmg.flashback.R
import tmg.flashback.base.BaseViewModel
import tmg.flashback.currentYear
import tmg.flashback.driver.overview.RaceForPositionType.*
import tmg.flashback.driver.season.addError
import tmg.flashback.repo.db.stats.DriverDB
import tmg.flashback.repo.models.stats.DriverOverview
import tmg.flashback.settings.ConnectivityManager
import tmg.flashback.shared.SyncDataItem
import tmg.flashback.shared.viewholders.DataUnavailable
import tmg.flashback.utils.position

//region Inputs

interface DriverOverviewViewModelInputs {
    fun setup(driverId: String)
}

//endregion

//region Outputs

interface DriverOverviewViewModelOutputs {
    val list: LiveData<List<DriverOverviewItem>>
}

//endregion

@FlowPreview
@ExperimentalCoroutinesApi
class DriverOverviewViewModel(
        private val driverDB: DriverDB,
        private val connectivityManager: ConnectivityManager
): BaseViewModel(), DriverOverviewViewModelInputs, DriverOverviewViewModelOutputs {

    var inputs: DriverOverviewViewModelInputs = this
    var outputs: DriverOverviewViewModelOutputs = this

    private val driverId: ConflatedBroadcastChannel<String> = ConflatedBroadcastChannel()

    override val list: LiveData<List<DriverOverviewItem>> = driverId
            .asFlow()
            .flatMapLatest { driverDB.getDriverOverview(it) }
            .map {
                val list: MutableList<DriverOverviewItem> = mutableListOf()
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
                        list.add(DriverOverviewItem.Header(
                                driverFirstname = it.firstName,
                                driverSurname = it.lastName,
                                driverNumber = it.number,
                                driverImg = it.photoUrl ?: "",
                                driverBirthday = it.dateOfBirth,
                                driverWikiUrl = it.firstName,
                                driverNationalityISO = it.nationalityISO
                        ))

                        // Add individual stats
                        list.addStat(
                                icon = R.drawable.ic_standings,
                                label = R.string.driver_overview_stat_career_wins,
                                value = it.careerWins.toString()
                        )

                        list.addStat(
                                icon = R.drawable.ic_podium,
                                label = R.string.driver_overview_stat_career_podiums,
                                value = it.careerPodiums.toString()
                        )

                        list.addStat(
                                icon = R.drawable.ic_race_points,
                                label = R.string.driver_overview_stat_career_points,
                                value = it.careerPoints.toString()
                        )

                        list.addStat(
                                icon = R.drawable.ic_status_finished,
                                label = R.string.driver_overview_stat_career_best_finish,
                                value = it.careerBestFinish.position().toString()
                        )

                        list.addStat(
                                icon = R.drawable.ic_finishes_in_points,
                                label = R.string.driver_overview_stat_career_points_finishes,
                                value = it.careerFinishesInPoints.toString()
                        )

                        list.addStat(
                                icon = R.drawable.ic_qualifying_pole,
                                label = R.string.driver_overview_stat_career_qualifying_pole,
                                value = it.careerQualifyingPoles.toString()
                        )

                        list.addStat(
                                icon = R.drawable.ic_qualifying_front_row,
                                label = R.string.driver_overview_stat_career_qualifying_front_row,
                                value = it.careerQualifyingFrontRow.toString()
                        )

                        list.addStat(
                                icon = R.drawable.ic_qualifying_top_ten,
                                label = R.string.driver_overview_stat_career_qualifying_top_10,
                                value = it.totalQualifyingAbove(10).toString()
                        )

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
            .asLiveData(viewModelScope.coroutineContext)

    //region Inputs

    override fun setup(driverId: String) {
        if (this.driverId.valueOrNull != driverId) {
            this.driverId.offer(driverId)
        }
    }

    //endregion

    //region Outputs

    //endregion

    private fun MutableList<DriverOverviewItem>.addStat(@DrawableRes icon: Int, @StringRes label: Int, value: String) {
        this.add(DriverOverviewItem.Stat(icon, label, value))
    }

    private fun getConstructorItemList(overview: DriverOverview): List<DriverOverviewItem> {
        return this.generateConstructorsListInDescendingOrder(overview)
                .reversed()
                .map {
                    val type = when (it.type) {
                        START -> END
                        END -> START
                        else -> it.type
                    }
                    DriverOverviewItem.RacedFor(it.season, it.constructors, type)
                }
    }

    private fun generateConstructorsListInDescendingOrder(overview: DriverOverview): List<DriverOverviewItem.RacedFor> {
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
                        SINGLE
                    }
                    // Raced for one year, then took one or more years off but returned later
                    nextItem.first >= season + 2 -> {
                        SINGLE
                    }
                    // Next result is either same year constructor change or next year.
                    else -> {
                        START
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
                            previousItem.first <= season - 2 && currentYear == season -> START
                            previousItem.first <= season - 2 && currentYear != season -> SINGLE
                            currentYear == season -> SEASON
                            else -> END
                        }
                    }

                    // Driver took one or more years off. Last item should be end, so need start
                    previousItem.first <= season - 2 -> {
                        START
                    }
                    // Ending before temporarily retiring
                    nextItem.first >= season + 2 -> {
                        END
                    }
                    // Next year constructor
                    previousItem.first == season - 1 -> {
                        SEASON
                    }
                    // Same year, constructor change mid season
                    else -> {
                        MID_SEASON_CHANGE
                    }
                }
            }

            DriverOverviewItem.RacedFor(season, constructor, dotType)
        }
    }
}
