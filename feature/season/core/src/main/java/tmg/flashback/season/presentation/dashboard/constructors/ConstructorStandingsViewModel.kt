package tmg.flashback.season.presentation.dashboard.constructors

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import tmg.flashback.constructors.contract.ConstructorSeason
import tmg.flashback.constructors.contract.with
import tmg.flashback.domain.repo.SeasonRepository
import tmg.flashback.domain.repo.usecases.FetchSeasonUseCase
import tmg.flashback.drivers.contract.DriverSeason
import tmg.flashback.drivers.contract.with
import tmg.flashback.formula1.model.SeasonConstructorStandingSeason
import tmg.flashback.navigation.Navigator
import tmg.flashback.navigation.Screen
import tmg.flashback.season.usecases.DefaultSeasonUseCase
import javax.inject.Inject

data class ConstructorStandingsScreenState(
    val season: Int,
    val standings: List<SeasonConstructorStandingSeason> = emptyList(),
    val inProgress: Pair<String, Int>? = null,
    val isLoading: Boolean = true,
    val maxPoints: Double = 0.0,
    val currentlySelected: SeasonConstructorStandingSeason? = null
)

interface ConstructorStandingsViewModelInputs {
    fun selectConstructor(constructor: SeasonConstructorStandingSeason)
    fun closeConstructor()
    fun refresh()
}

interface ConstructorStandingsViewModelOutputs {
    val uiState: StateFlow<ConstructorStandingsScreenState>
}

@HiltViewModel
class ConstructorStandingsViewModel @Inject constructor(
    private val seasonRepository: SeasonRepository,
    private val fetchSeasonUseCase: FetchSeasonUseCase,
    private val defaultSeasonUseCase: DefaultSeasonUseCase,
    private val navigator: Navigator,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel(), ConstructorStandingsViewModelInputs, ConstructorStandingsViewModelOutputs {

    val inputs: ConstructorStandingsViewModelInputs = this
    val outputs: ConstructorStandingsViewModelOutputs = this

    private val season by lazy { defaultSeasonUseCase.defaultSeason } // savedStateHandle.get<Int>(SEASON)!! }

    override val uiState: MutableStateFlow<ConstructorStandingsScreenState> = MutableStateFlow(
        ConstructorStandingsScreenState(
            season = season
        )
    )

    init {
        refresh()
    }

    override fun selectConstructor(constructor: SeasonConstructorStandingSeason) {
        uiState.value = uiState.value.copy(currentlySelected = constructor)
        navigator.navigate(
            Screen.ConstructorSeason.with(
                constructorId = constructor.constructor.id,
                constructorName = constructor.constructor.name,
                season = constructor.season
            )
        )
    }

    override fun closeConstructor() {
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
        val currentStandings = seasonRepository.getConstructorStandings(season).firstOrNull()?.standings ?: emptyList()
        val maxPoints = currentStandings.maxOfOrNull { it.points } ?: 800.0
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