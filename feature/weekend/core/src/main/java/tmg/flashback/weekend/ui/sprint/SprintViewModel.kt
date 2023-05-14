package tmg.flashback.weekend.ui.sprint

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
import tmg.flashback.drivers.contract.DriverSeason
import tmg.flashback.drivers.contract.with
import tmg.flashback.formula1.constants.Formula1
import tmg.flashback.formula1.model.SprintRaceResult
import tmg.flashback.navigation.Navigator
import tmg.flashback.domain.repo.RaceRepository
import tmg.flashback.formula1.model.Constructor
import tmg.flashback.navigation.Screen
import tmg.flashback.weekend.R
import tmg.flashback.weekend.ui.race.RaceModel
import tmg.flashback.weekend.ui.race.RaceResultType
import tmg.utilities.extensions.combinePair
import javax.inject.Inject

interface SprintViewModelInputs {
    fun load(season: Int, round: Int)
    fun show(sprintResultType: SprintResultType)
    fun clickDriver(result: SprintRaceResult)
    fun clickConstructor(constructor: Constructor)
}

interface SprintViewModelOutputs {
    val list: LiveData<List<SprintModel>>
    val sprintResultType: LiveData<SprintResultType>
}

@HiltViewModel
class SprintViewModel @Inject constructor(
    private val raceRepository: RaceRepository,
    private val navigator: Navigator,
    private val ioDispatcher: CoroutineDispatcher
): ViewModel(), SprintViewModelInputs, SprintViewModelOutputs {

    val inputs: SprintViewModelInputs = this
    val outputs: SprintViewModelOutputs = this

    override val sprintResultType: MutableLiveData<SprintResultType> = MutableLiveData(SprintResultType.DRIVERS)

    private val seasonRound: MutableStateFlow<Pair<Int, Int>?> = MutableStateFlow(null)
    override val list: LiveData<List<SprintModel>> = seasonRound
        .filterNotNull()
        .flatMapLatest { (season, round) -> raceRepository.getRace(season, round) }
        .combinePair(sprintResultType.asFlow())
        .flowOn(ioDispatcher)
        .map { (race, sprintResultType) ->
            if (race == null || race.sprint.race.isEmpty()) {
                val list = mutableListOf<SprintModel>().apply {
                    if ((seasonRound.value?.first ?: Formula1.currentSeasonYear) >= Formula1.currentSeasonYear) {
                        add(SprintModel.NotAvailableYet)
                    } else {
                        add(SprintModel.NotAvailable)
                    }
                }
                return@map list
            }

            val list: MutableList<SprintModel> = mutableListOf()
            if (sprintResultType == SprintResultType.DRIVERS) {
                list.addAll(race
                    .sprint
                    .race
                    .map { model ->
                        SprintModel.DriverResult(model)
                    }
                    .sortedBy { it.result.finish })
            } else {
                list.addAll(race
                    .sprint
                    .race
                    .groupBy { it.driver.constructor }
                    .map { (constructor, listOfResult) ->
                        SprintModel.ConstructorResult(
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
                            maxTeamPoints = 15.0
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

    override fun show(sprintResultType: SprintResultType) {
        this.sprintResultType.value = sprintResultType
    }

    override fun clickDriver(result: SprintRaceResult) {
        val season = seasonRound.value?.first ?: return
        navigator.navigate(
            Screen.DriverSeason.with(
            driverId = result.driver.driver.id,
            driverName = result.driver.driver.name,
            season = season
        ))
    }

    override fun clickConstructor(constructor: Constructor) {
        val season = seasonRound.value?.first ?: return
        navigator.navigate(
            Screen.ConstructorSeason.with(
                constructorId = constructor.id,
                constructorName = constructor.name,
                season = season
            ))
    }
}

enum class SprintResultType(
    @StringRes
    val label: Int
) {
    DRIVERS(R.string.dashboard_tab_drivers),
    CONSTRUCTORS(R.string.dashboard_tab_constructors)
}