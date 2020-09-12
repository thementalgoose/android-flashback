package tmg.flashback.driver.season

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import tmg.flashback.R
import tmg.flashback.base.BaseViewModel
import tmg.flashback.maxPointsBySeason
import tmg.flashback.repo.db.stats.DriverDB
import tmg.flashback.repo.models.stats.DriverOverviewStanding
import tmg.flashback.settings.ConnectivityManager
import tmg.flashback.shared.SyncDataItem
import tmg.flashback.shared.viewholders.DataUnavailable
import tmg.flashback.utils.position
import tmg.utilities.extensions.ordinalAbbreviation
import tmg.utilities.lifecycle.DataEvent

//region Inputs

interface DriverSeasonViewModelInputs {
    fun setup(driverId: String, season: Int)
    fun clickSeasonRound(result: DriverSeasonItem.Result)
}

//endregion

//region Outputs

interface DriverSeasonViewModelOutputs {
    val goToSeasonRound: MutableLiveData<DataEvent<DriverSeasonItem.Result>>
    val list: LiveData<List<DriverSeasonItem>>
}

//endregion


@FlowPreview
@ExperimentalCoroutinesApi
class DriverSeasonViewModel(
    private val driverDB: DriverDB,
    private val connectivityManager: ConnectivityManager
) : BaseViewModel(), DriverSeasonViewModelInputs, DriverSeasonViewModelOutputs {

    var inputs: DriverSeasonViewModelInputs = this
    var outputs: DriverSeasonViewModelOutputs = this

    override val goToSeasonRound: MutableLiveData<DataEvent<DriverSeasonItem.Result>> =
        MutableLiveData()

    private var driverId: ConflatedBroadcastChannel<String> = ConflatedBroadcastChannel()
    private var season: Int = -1

    override val list: LiveData<List<DriverSeasonItem>> = driverId
        .asFlow()
        .flatMapLatest { driverDB.getDriverOverview(it) }
        .map { overview ->
            val list: MutableList<DriverSeasonItem> = mutableListOf()
            val standing = overview?.standings?.firstOrNull { it.season == season }
            when {
                overview == null || standing == null -> {
                    when {
                        !connectivityManager.isConnected ->
                            list.addError(SyncDataItem.NoNetwork)
                        else ->
                            list.addError(SyncDataItem.Unavailable(DataUnavailable.DRIVER_NOT_EXIST))
                    }
                }
                else -> {

                    list.addAll(getAllStats(standing))

                    list.add(DriverSeasonItem.ResultHeader)

                    list.addAll(standing
                        .raceOverview
                        .map {
                            DriverSeasonItem.Result(
                                season = it.season,
                                round = it.round,
                                raceName = it.raceName,
                                circuitName = it.circuitName,
                                circuitId = it.circuitId,
                                raceCountry = it.circuitNationality,
                                raceCountryISO = it.circuitNationalityISO,
                                constructor = standing.constructors.last(),
                                date = it.date,
                                qualified = it.qualified,
                                finished = it.finished,
                                raceStatus = it.status,
                                points = it.points,
                                maxPoints = maxPointsBySeason(it.season)
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
        if (driverId != this.driverId.valueOrNull) {
            this.season = season
            this.driverId.offer(driverId)
        }
    }

    override fun clickSeasonRound(result: DriverSeasonItem.Result) {
        goToSeasonRound.value = DataEvent(result)
    }

    //endregion


    /**
     * Add career stats for the driver across their career
     */
    private fun getAllStats(overview: DriverOverviewStanding): List<DriverSeasonItem> {
        return listOf(
            DriverSeasonItem.Stat(
                icon = R.drawable.ic_championship_order,
                label = R.string.driver_overview_stat_career_championship_standing,
                value = overview.championshipStanding.ordinalAbbreviation
            ),
            DriverSeasonItem.Stat(
                icon = R.drawable.ic_standings,
                label = R.string.driver_overview_stat_career_wins,
                value = overview.wins.toString()
            ),
            DriverSeasonItem.Stat(
                icon = R.drawable.ic_podium,
                label = R.string.driver_overview_stat_career_podiums,
                value = overview.podiums.toString()
            ),
            DriverSeasonItem.Stat(
                icon = R.drawable.ic_status_finished,
                label = R.string.driver_overview_stat_career_best_finish,
                value = overview.bestFinish.position().toString()
            ),
            DriverSeasonItem.Stat(
                icon = R.drawable.ic_finishes_in_points,
                label = R.string.driver_overview_stat_career_points_finishes,
                value = overview.finishesInPoints.toString()
            ),
            DriverSeasonItem.Stat(
                icon = R.drawable.ic_race_points,
                label = R.string.driver_overview_stat_career_points,
                value = overview.points.toString()
            ),
            DriverSeasonItem.Stat(
                icon = R.drawable.ic_qualifying_pole,
                label = R.string.driver_overview_stat_career_qualifying_pole,
                value = overview.qualifyingPoles.toString()
            ),
            DriverSeasonItem.Stat(
                icon = R.drawable.ic_qualifying_front_row,
                label = R.string.driver_overview_stat_career_qualifying_top_3,
                value = overview.qualifyingTop3.toString()
            ),
            DriverSeasonItem.Stat(
                icon = R.drawable.ic_qualifying_top_ten,
                label = R.string.driver_overview_stat_career_qualifying_top_10,
                value = overview.totalQualifyingAbove(10).toString()
            )
        )
    }
}
