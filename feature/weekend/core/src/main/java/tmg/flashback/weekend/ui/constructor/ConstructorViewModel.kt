package tmg.flashback.weekend.ui.constructor

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import tmg.flashback.constructors.contract.Constructor
import tmg.flashback.constructors.contract.with
import tmg.flashback.formula1.constants.Formula1
import tmg.flashback.formula1.model.Driver
import tmg.flashback.formula1.model.Race
import tmg.flashback.statistics.repo.RaceRepository
import tmg.flashback.navigation.Navigator
import tmg.flashback.navigation.Screen
import javax.inject.Inject

interface ConstructorViewModelInputs {
    fun load(season: Int, round: Int)
    fun clickItem(model: ConstructorModel.Constructor)
}

interface ConstructorViewModelOutputs {
    val list: LiveData<List<ConstructorModel>>
}

@HiltViewModel
class ConstructorViewModel @Inject constructor(
    private val raceRepository: RaceRepository,
    private val navigator: Navigator,
    private val ioDispatcher: CoroutineDispatcher
): ViewModel(), ConstructorViewModelInputs, ConstructorViewModelOutputs {

    val inputs: ConstructorViewModelInputs = this
    val outputs: ConstructorViewModelOutputs = this

    private val seasonRound: MutableStateFlow<Pair<Int, Int>?> = MutableStateFlow(null)
    override val list: LiveData<List<ConstructorModel>> = seasonRound
        .filterNotNull()
        .flatMapLatest { (season, round) -> raceRepository.getRace(season, round) }
        .flowOn(ioDispatcher)
        .map { race ->
            if (race == null || race.race.isEmpty()) {
                val list = mutableListOf<ConstructorModel>().apply {
                    if ((seasonRound.value?.first ?: Formula1.currentSeasonYear) >= Formula1.currentSeasonYear) {
                        add(ConstructorModel.NotAvailableYet)
                    } else {
                        add(ConstructorModel.NotAvailable)
                    }
                }
                return@map list
            }

            val maxPoints = race.constructorStandings.maxByOrNull { it.points }?.points ?: 60.0
            race.constructorStandings
                .mapIndexed { index, standing ->
                    ConstructorModel.Constructor(
                        constructor = standing.constructor,
                        points = standing.points,
                        position = index + 1,
                        drivers = getDriverFromConstructor(race, standing.constructor.id),
                        maxTeamPoints = maxPoints
                    )
                }
        }
        .asLiveData(viewModelScope.coroutineContext)

    override fun load(season: Int, round: Int) {
        seasonRound.value = Pair(season, round)
    }

    override fun clickItem(model: ConstructorModel.Constructor) {
        navigator.navigate(
            Screen.Constructor.with(
            constructorId = model.constructor.id,
            constructorName = model.constructor.name
        ))
    }

    private fun getDriverFromConstructor(race: Race, constructorId: String): List<Pair<Driver, Double>> {
        return race.race
            .mapNotNull { raceResult ->
                if (raceResult.driver.constructor.id != constructorId) return@mapNotNull null
                val sprintQualifying = race.sprint.race.firstOrNull { it.driver.driver.id == raceResult.driver.driver.id }
                return@mapNotNull Pair(raceResult.driver.driver, raceResult.points + (sprintQualifying?.points ?: 0.0))
            }
            .sortedByDescending { it.second }
    }

}