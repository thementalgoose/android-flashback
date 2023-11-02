package tmg.flashback.weekend.presentation.sprint

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import tmg.flashback.constructors.contract.Constructor
import tmg.flashback.constructors.contract.with
import tmg.flashback.domain.repo.RaceRepository
import tmg.flashback.drivers.contract.Driver
import tmg.flashback.drivers.contract.with
import tmg.flashback.formula1.constants.Formula1
import tmg.flashback.formula1.model.Constructor
import tmg.flashback.formula1.model.DriverEntry
import tmg.flashback.navigation.Navigator
import tmg.flashback.navigation.Screen
import tmg.flashback.weekend.R
import javax.inject.Inject

interface SprintViewModelInputs {
    fun load(season: Int, round: Int)
    fun show(sprintResultType: SprintResultType)
    fun clickDriver(result: DriverEntry)
    fun clickConstructor(constructor: Constructor)
}

interface SprintViewModelOutputs {
    val list: StateFlow<ImmutableList<SprintModel>>
    val sprintResultType: StateFlow<SprintResultType>
}

@HiltViewModel
class SprintViewModel @Inject constructor(
    private val raceRepository: RaceRepository,
    private val navigator: Navigator,
    private val ioDispatcher: CoroutineDispatcher
): ViewModel(), SprintViewModelInputs, SprintViewModelOutputs {

    val inputs: SprintViewModelInputs = this
    val outputs: SprintViewModelOutputs = this

    override val sprintResultType: MutableStateFlow<SprintResultType> = MutableStateFlow(SprintResultType.DRIVERS)

    private val seasonRound: MutableStateFlow<Pair<Int, Int>?> = MutableStateFlow(null)
    override val list: StateFlow<ImmutableList<SprintModel>> = seasonRound
        .filterNotNull()
        .flatMapLatest { (season, round) -> raceRepository.getRace(season, round) }
        .combine(sprintResultType.asStateFlow()) { a, b -> a to b }
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
                return@map list.toImmutableList()
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
                    .groupBy { it.entry.constructor }
                    .map { (constructor, listOfResult) ->
                        SprintModel.ConstructorResult(
                            constructor = constructor,
                            points = listOfResult.sumOf { it.points },
                            position = 0,
                            drivers = listOfResult.map {
                                it.entry.driver to it.points
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
            return@map list.toImmutableList()
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, persistentListOf())

    override fun load(season: Int, round: Int) {
        seasonRound.value = Pair(season, round)
    }

    override fun show(sprintResultType: SprintResultType) {
        this.sprintResultType.value = sprintResultType
    }

    override fun clickDriver(result: DriverEntry) {
        val season = seasonRound.value?.first ?: return
        navigator.navigate(
            Screen.Driver.with(
            driverId = result.driver.id,
            driverName = result.driver.name,
//            season = season
        ))
    }

    override fun clickConstructor(constructor: Constructor) {
        val season = seasonRound.value?.first ?: return
        navigator.navigate(
            Screen.Constructor.with(
                constructorId = constructor.id,
                constructorName = constructor.name,
//                season = season
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