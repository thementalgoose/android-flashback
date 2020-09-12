package tmg.flashback.driver.season

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import tmg.flashback.base.BaseViewModel
import tmg.flashback.currentYear
import tmg.flashback.maxPointsBySeason
import tmg.flashback.repo.db.PrefsDB
import tmg.flashback.repo.db.stats.DriverDB
import tmg.flashback.repo.db.stats.SeasonOverviewDB
import tmg.flashback.repo.models.stats.Constructor
import tmg.flashback.repo.models.stats.Driver
import tmg.flashback.settings.ConnectivityManager
import tmg.flashback.shared.SyncDataItem
import tmg.flashback.shared.viewholders.DataUnavailable
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

    //region Outputs

    //endregion
}
