package tmg.flashback.season.presentation.dashboard.drivers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import tmg.flashback.domain.repo.SeasonRepository
import tmg.flashback.domain.repo.usecases.FetchSeasonUseCase
import tmg.flashback.drivers.contract.Driver
import tmg.flashback.drivers.contract.DriverSeason
import tmg.flashback.drivers.contract.with
import tmg.flashback.formula1.model.SeasonDriverStandingSeason
import tmg.flashback.navigation.Navigator
import tmg.flashback.navigation.Screen
import tmg.flashback.season.usecases.DefaultSeasonUseCase
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
    private val defaultSeasonUseCase: DefaultSeasonUseCase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel(), DriverStandingsViewModelInputs, DriverStandingsViewModelOutputs {

    val inputs: DriverStandingsViewModelInputs = this
    val outputs: DriverStandingsViewModelOutputs = this

    private val season by lazy { defaultSeasonUseCase.defaultSeason } // savedStateHandle.get<Int>(SEASON)!! }

    override val uiState: MutableStateFlow<DriverStandingsScreenState> = MutableStateFlow(
        DriverStandingsScreenState(
            season = season
        )
    )

    init {
        viewModelScope.launch(ioDispatcher) {
            populate()
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

    private suspend fun populate() {
        val currentStandings = seasonRepository.getDriverStandings(season).firstOrNull()?.standings ?: emptyList()
        val maxPoints = currentStandings.maxOfOrNull { it.points } ?: 850.0

        uiState.value = uiState.value.copy(
            standings = currentStandings,
            maxPoints = maxPoints,
            inProgress = currentStandings.firstOrNull()?.inProgressContent,
            isLoading = false
        )
    }

    companion object {
        internal const val SEASON = "season"
    }
}