package tmg.flashback.season.presentation.dashboard.drivers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.skip
import kotlinx.coroutines.launch
import tmg.flashback.domain.repo.SeasonRepository
import tmg.flashback.domain.repo.usecases.FetchSeasonUseCase
import tmg.flashback.drivers.contract.Driver
import tmg.flashback.drivers.contract.DriverSeason
import tmg.flashback.drivers.contract.with
import tmg.flashback.formula1.constants.Formula1.currentSeasonYear
import tmg.flashback.formula1.model.SeasonDriverStandingSeason
import tmg.flashback.navigation.Navigator
import tmg.flashback.navigation.Screen
import tmg.flashback.season.presentation.dashboard.shared.seasonpicker.CurrentSeasonHolder
import tmg.flashback.season.usecases.DefaultSeasonUseCase
import tmg.utilities.extensions.combinePair
import javax.inject.Inject

data class DriverStandingsScreenState(
    val season: Int,
    val standings: List<SeasonDriverStandingSeason> = emptyList(),
    val inProgress: Pair<String, Int>? = null,
    val isLoading: Boolean = true,
    val maxPoints: Double = 0.0,
    val currentlySelected: SeasonDriverStandingSeason? = null
)

interface DriverStandingsViewModelInputs {
    fun selectDriver(driver: SeasonDriverStandingSeason)
    fun closeDriverDetails()
    fun refresh()
}

interface DriverStandingsViewModelOutputs {
    val uiState: StateFlow<DriverStandingsScreenState>
}

@HiltViewModel
class DriverStandingsViewModel @Inject constructor(
    private val seasonRepository: SeasonRepository,
    private val fetchSeasonUseCase: FetchSeasonUseCase,
    private val currentSeasonHolder: CurrentSeasonHolder,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel(), DriverStandingsViewModelInputs, DriverStandingsViewModelOutputs {

    val inputs: DriverStandingsViewModelInputs = this
    val outputs: DriverStandingsViewModelOutputs = this


    override val uiState: MutableStateFlow<DriverStandingsScreenState> = MutableStateFlow(
        DriverStandingsScreenState(season = currentSeasonHolder.currentSeason)
    )
    private val season: Int
        get() = uiState.value.season

    init {
        viewModelScope.launch(ioDispatcher) {
            currentSeasonHolder.currentSeasonFlow.collectLatest {
                uiState.value = uiState.value.copy(season = it)
                if (!populate() || it == currentSeasonHolder.defaultSeason) {
                    refresh()
                }
            }
        }
    }

    override fun selectDriver(driver: SeasonDriverStandingSeason) {
        uiState.value = uiState.value.copy(currentlySelected = driver)
    }

    override fun closeDriverDetails() {
        uiState.value = uiState.value.copy(currentlySelected = null)
    }

    override fun refresh() {
        viewModelScope.launch(ioDispatcher) {
            if (uiState.value.standings.isEmpty()) {
                populate()
            }
            uiState.value = uiState.value.copy(isLoading = true)
            fetchSeasonUseCase.fetchSeason(season)
            populate()
        }
    }

    private suspend fun populate(): Boolean {
        val currentStandings = seasonRepository.getDriverStandings(season).firstOrNull()?.standings ?: emptyList()
        val maxPoints = currentStandings.maxOfOrNull { it.points } ?: 850.0

        uiState.value = uiState.value.copy(
            standings = currentStandings,
            maxPoints = maxPoints,
            inProgress = currentStandings.firstOrNull()?.inProgressContent,
            isLoading = false
        )

        return currentStandings.isNotEmpty()
    }

    companion object {
        internal const val SEASON = "season"
    }
}