package tmg.flashback.weekend.ui.race

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import tmg.flashback.constructors.contract.ConstructorSeason
import tmg.flashback.constructors.contract.with
import tmg.flashback.domain.repo.RaceRepository
import tmg.flashback.drivers.contract.DriverSeason
import tmg.flashback.drivers.contract.with
import tmg.flashback.formula1.constants.Formula1
import tmg.flashback.formula1.model.Constructor
import tmg.flashback.formula1.model.Driver
import tmg.flashback.formula1.model.Race
import tmg.flashback.formula1.model.RaceResult
import tmg.flashback.navigation.Screen
import tmg.flashback.weekend.R
import tmg.utilities.extensions.combinePair
import javax.inject.Inject

interface RaceViewModelInputs {
    fun load(season: Int, round: Int)
    fun show(raceResultType: RaceResultType)
    fun clickDriver(result: RaceResult)
    fun clickConstructor(constructor: Constructor)
}

interface RaceViewModelOutputs {
    val list: LiveData<List<RaceModel>>
    val raceResultType: LiveData<RaceResultType>
}

@HiltViewModel
class RaceViewModel @Inject constructor(
    private val raceRepository: RaceRepository,
    private val navigator: tmg.flashback.navigation.Navigator,
    private val ioDispatcher: CoroutineDispatcher
): ViewModel(), RaceViewModelInputs, RaceViewModelOutputs {

    val inputs: RaceViewModelInputs = this
    val outputs: RaceViewModelOutputs = this

    override val raceResultType: MutableLiveData<RaceResultType> = MutableLiveData(RaceResultType.DRIVERS)

    private val seasonRound: MutableStateFlow<Pair<Int, Int>?> = MutableStateFlow(null)
    override val list: LiveData<List<RaceModel>> = seasonRound
        .filterNotNull()
        .flatMapLatest { (season, round) -> raceRepository.getRace(season, round) }
        .combinePair(raceResultType.asFlow())
        .flowOn(ioDispatcher)
        .map { (race, raceDisplayType) ->
            val raceResults = race?.race ?: emptyList()
            if (race == null || race.race.isEmpty()) {
                val list = mutableListOf<RaceModel>().apply {
                    if ((seasonRound.value?.first ?: Formula1.currentSeasonYear) >= Formula1.currentSeasonYear) {
                        add(RaceModel.NotAvailableYet)
                    } else {
                        add(RaceModel.NotAvailable)
                    }
                }
                return@map list
            }

            val list: MutableList<RaceModel> = mutableListOf()
            if (raceDisplayType == RaceResultType.DRIVERS) {
                if (raceResults.size >= 3) {
                    val podium = RaceModel.DriverPodium(
                        p1 = raceResults[0],
                        p2 = raceResults[1],
                        p3 = raceResults[2]
                    )
                    list.add(podium)
                    for (x in raceResults.drop(3)) {
                        list.add(RaceModel.DriverResult(x))
                    }
                } else {
                    list.addAll(raceResults.map { RaceModel.DriverResult(it) })
                }
            } else {
                list.addAll(
                    race.race
                        .groupBy { it.driver.constructor }
                        .map { (constructor, listOfResult) ->
                            RaceModel.ConstructorResult(
                                constructor = constructor,
                                points = listOfResult.sumOf { it.points },
                                position = 0,
                                drivers = listOfResult.map {
                                    it.driver.driver to it.points
                                },
                                maxTeamPoints = 0.0,
                                highestDriverPosition = listOfResult.minOf { it.finish }
                            )
                        }
                        .sortedBy { it.highestDriverPosition }
                        .sortedByDescending { it.points }
                        .mapIndexed { index, constructorResult ->
                            constructorResult.copy(
                                position = index + 1,
                                maxTeamPoints = 45.0
                            )
                        }
                )
            }
            return@map list
        }
        .asLiveData(viewModelScope.coroutineContext)


    override fun load(season: Int, round: Int) {
        seasonRound.value = Pair(season, round)
    }

    override fun show(raceResultType: RaceResultType) {
        this.raceResultType.value = raceResultType
    }

    override fun clickConstructor(constructor: Constructor) {
        val season = seasonRound.value?.first ?: return
        navigator.navigate(
            Screen.ConstructorSeason.with(
                constructorId = constructor.id,
                constructorName = constructor.name,
                season = season
            )
        )
    }

    override fun clickDriver(result: RaceResult) {
        val season = seasonRound.value?.first ?: return
        navigator.navigate(
            Screen.DriverSeason.with(
            driverId = result.driver.driver.id,
            driverName = result.driver.driver.name,
            season = season
        ))
    }

    private fun getDriverFromConstructor(race: Race, constructorId: String): List<Pair<Driver, Double>> {
        return race.race
            .mapNotNull { raceResult ->
                if (raceResult.driver.constructor.id != constructorId) return@mapNotNull null
                return@mapNotNull Pair(raceResult.driver.driver, raceResult.points)
            }
            .sortedByDescending { it.second }
    }
}

enum class RaceResultType(
    @StringRes
    val label: Int
) {
    DRIVERS(R.string.dashboard_tab_drivers),
    CONSTRUCTORS(R.string.dashboard_tab_constructors)
}