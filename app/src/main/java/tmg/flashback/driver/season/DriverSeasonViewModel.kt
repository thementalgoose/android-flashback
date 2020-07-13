package tmg.flashback.driver.season

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import org.koin.ext.scope
import org.threeten.bp.LocalDate
import tmg.flashback.base.BaseViewModel
import tmg.flashback.currentYear
import tmg.flashback.driver.season.list.DriverSeasonItem
import tmg.flashback.driver.season.list.viewholders.DriverSeasonHeaderViewHolder
import tmg.flashback.race.RaceAdapterModel
import tmg.flashback.repo.db.PrefsDB
import tmg.flashback.repo.db.stats.SeasonOverviewDB
import tmg.flashback.repo.models.stats.Constructor
import tmg.flashback.repo.models.stats.Driver
import tmg.flashback.settings.ConnectivityManager
import tmg.flashback.shared.viewholders.DataUnavailable
import tmg.flashback.showComingSoonMessageForNextDays

//region Inputs

interface DriverSeasonViewModelInputs {
    fun initialise(season: Int, driverId: String)
}

//endregion

//region Outputs

interface DriverSeasonViewModelOutputs {
    val list: LiveData<List<DriverSeasonItem>>
}

//endregion

class DriverSeasonViewModel(
        private val seasonDB: SeasonOverviewDB,
        private val prefDB: PrefsDB,
        private val connectivityManager: ConnectivityManager
) : BaseViewModel(), DriverSeasonViewModelInputs, DriverSeasonViewModelOutputs {

    var inputs: DriverSeasonViewModelInputs = this
    var outputs: DriverSeasonViewModelOutputs = this

    private lateinit var driverId: String
    private var season: ConflatedBroadcastChannel<Int> = ConflatedBroadcastChannel()

    override val list: LiveData<List<DriverSeasonItem>> = season
            .asFlow()
            .flatMapLatest { seasonDB.getSeasonOverview(it) }
            .map { (season, rounds) ->
                val list: MutableList<DriverSeasonItem> = mutableListOf()
                when {
                    rounds.isEmpty() -> {
                        when {
                            !connectivityManager.isConnected ->
                                list.add(DriverSeasonItem.NoNetwork)
                            else ->
                                list.add(DriverSeasonItem.Unavailable(DataUnavailable.EARLY_IN_SEASON))
                        }
                    }
                    season > currentYear -> {
                        list.add(DriverSeasonItem.Unavailable(DataUnavailable.IN_FUTURE_SEASON))
                    }
                    else -> {
                        val driver: Driver? = rounds
                                .map { round ->
                                    round.drivers.firstOrNull {
                                        it.id == driverId
                                    }
                                }
                                .firstOrNull()
                        val constructors: List<Constructor> = rounds.mapNotNull { it.race[driverId]?.driver }
                                .map { it.constructor }
                                .distinct()

                        if (driver != null) {
                            list.add(DriverSeasonItem.Header(driver, constructors))
                        }

                        list.add(DriverSeasonItem.ResultHeader)

                        list.addAll(rounds
                                .map {
                                    val race = it.race[driverId]
                                    if (race != null) {
                                        DriverSeasonItem.Result(
                                                season = it.season,
                                                round = it.round,
                                                raceName = it.name,
                                                circuitName = it.circuit.name,
                                                circuitId = it.circuit.id,
                                                raceCountry = it.circuit.country,
                                                raceCountryISO = it.circuit.countryISO,
                                                constructor = race.driver.constructor,
                                                date = it.date,
                                                qualified = race.qualified ?: 0,
                                                finished = race.finish,
                                                raceStatus = race.status,
                                                points = race.points,
                                                maxPoints = it.race.maxBy { it.value.points }?.value?.points
                                                        ?: 25
                                        )
                                    } else {
                                        null
                                    }
                                }
                                .filterNotNull()
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

    override fun initialise(season: Int, driverId: String) {
        this.driverId = driverId
        this.season.offer(season)
    }

    //endregion

    //region Outputs

    //endregion
}