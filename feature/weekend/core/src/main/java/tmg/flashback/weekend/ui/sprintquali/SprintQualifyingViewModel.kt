package tmg.flashback.weekend.ui.sprintquali


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
import tmg.flashback.drivers.contract.DriverSeason
import tmg.flashback.drivers.contract.with
import tmg.flashback.formula1.constants.Formula1.currentSeasonYear
import tmg.flashback.formula1.model.Driver
import tmg.flashback.formula1.model.Race
import tmg.flashback.formula1.model.QualifyingType
import tmg.flashback.navigation.Screen
import tmg.flashback.domain.repo.RaceRepository
import tmg.flashback.formula1.model.SprintQualifyingType
import javax.inject.Inject

interface SprintQualifyingViewModelInputs {
    fun load(season: Int, round: Int)
    fun clickDriver(result: Driver)
}

interface SprintQualifyingViewModelOutputs {
    val list: LiveData<List<SprintQualifyingModel>>
}

typealias SprintQualifyingHeader = Triple<Boolean, Boolean, Boolean>

@HiltViewModel
class SprintQualifyingViewModel @Inject constructor(
    private val raceRepository: RaceRepository,
    private val navigator: tmg.flashback.navigation.Navigator,
    private val ioDispatcher: CoroutineDispatcher
): ViewModel(), SprintQualifyingViewModelInputs, SprintQualifyingViewModelOutputs {

    val inputs: SprintQualifyingViewModelInputs = this
    val outputs: SprintQualifyingViewModelOutputs = this

    private val seasonRound: MutableStateFlow<Pair<Int, Int>?> = MutableStateFlow(null)
    override val list: LiveData<List<SprintQualifyingModel>> = seasonRound
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
                return@map list
            }

            return@map race.getSprintShootout()
        }
        .asLiveData(viewModelScope.coroutineContext)

    override fun load(season: Int, round: Int) {
        seasonRound.value = Pair(season, round)
    }

    private fun Race.getSprintShootout(): List<SprintQualifyingModel> {
        val list = this.sprint.qualifying.firstOrNull { it.label == SprintQualifyingType.SQ3 } ?: return emptyList()

        return list.results.map {
            val overview = driverOverview(it.driver.driver.id)
            return@map SprintQualifyingModel.Result(
                driver = it.driver,
                finalQualifyingPosition = overview?.qualified,
                sq1 = overview?.sprintQ1,
                sq2 = overview?.sprintQ2,
                sq3 = overview?.sprintQ3,
                grid = overview?.race?.grid
            )
        }
    }

    override fun clickDriver(result: Driver) {
        val season = seasonRound.value?.first ?: return
        navigator.navigate(
            Screen.DriverSeason.with(
                driverId = result.id,
                driverName = result.name,
                season = season
            ))
    }

}