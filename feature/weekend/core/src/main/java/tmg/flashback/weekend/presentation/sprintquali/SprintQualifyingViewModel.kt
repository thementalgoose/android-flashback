package tmg.flashback.weekend.presentation.sprintquali


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
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import okhttp3.internal.immutableListOf
import tmg.flashback.domain.repo.RaceRepository
import tmg.flashback.drivers.contract.Driver
import tmg.flashback.drivers.contract.with
import tmg.flashback.formula1.constants.Formula1.currentSeasonYear
import tmg.flashback.formula1.model.Driver
import tmg.flashback.formula1.model.Race
import tmg.flashback.formula1.model.SprintQualifyingType
import tmg.flashback.navigation.Screen
import javax.annotation.concurrent.Immutable
import javax.inject.Inject

interface SprintQualifyingViewModelInputs {
    fun load(season: Int, round: Int)
    fun clickDriver(result: Driver)
}

interface SprintQualifyingViewModelOutputs {
    val list: StateFlow<ImmutableList<SprintQualifyingModel>>
}

@HiltViewModel
class SprintQualifyingViewModel @Inject constructor(
    private val raceRepository: RaceRepository,
    private val navigator: tmg.flashback.navigation.Navigator,
    private val ioDispatcher: CoroutineDispatcher
): ViewModel(), SprintQualifyingViewModelInputs, SprintQualifyingViewModelOutputs {

    val inputs: SprintQualifyingViewModelInputs = this
    val outputs: SprintQualifyingViewModelOutputs = this

    private val seasonRound: MutableStateFlow<Pair<Int, Int>?> = MutableStateFlow(null)
    override val list: StateFlow<ImmutableList<SprintQualifyingModel>> = seasonRound
        .filterNotNull()
        .flatMapLatest { (season, round) -> raceRepository.getRace(season, round) }
        .flowOn(ioDispatcher)
        .map { race ->
            if (race == null || race.qualifying.isEmpty()) {
                val list = mutableListOf<SprintQualifyingModel>().apply {
                    if ((seasonRound.value?.first ?: currentSeasonYear) >= currentSeasonYear) {
                        add(SprintQualifyingModel.NotAvailableYet)
                    } else {
                        add(SprintQualifyingModel.NotAvailable)
                    }
                }
                return@map list.toImmutableList()
            }

            return@map race.getSprintShootout().toImmutableList()
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, persistentListOf())

    override fun load(season: Int, round: Int) {
        seasonRound.value = Pair(season, round)
    }

    private fun Race.getSprintShootout(): List<SprintQualifyingModel> {
        val list = this.sprint.qualifying.firstOrNull { it.label == SprintQualifyingType.SQ3 } ?: return emptyList()

        return list.results
            .mapIndexed { index, it ->
                val overview = driverOverview(it.entry.driver.id)
                return@mapIndexed SprintQualifyingModel.Result(
                    driver = it.entry,
                    finalQualifyingPosition = overview?.sprintQ3?.position ?: overview?.sprintQ2?.position ?: overview?.sprintQ1?.position ?: (index + 1),
                    sq1 = overview?.sprintQ1,
                    sq2 = overview?.sprintQ2,
                    sq3 = overview?.sprintQ3,
                    grid = overview?.sprintRace?.grid
                )
            }
    }

    override fun clickDriver(result: Driver) {
        val season = seasonRound.value?.first ?: return
        navigator.navigate(
            Screen.Driver.with(
                driverId = result.id,
                driverName = result.name,
//                season = season
            ))
    }

}