package tmg.flashback.season.presentation.dashboard.constructors

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import tmg.flashback.device.managers.NetworkConnectivityManager
import tmg.flashback.domain.repo.SeasonRepository
import tmg.flashback.domain.repo.usecases.FetchSeasonUseCase
import tmg.flashback.formula1.model.SeasonConstructorStandingSeason
import tmg.flashback.navigation.Navigator
import tmg.flashback.season.presentation.dashboard.shared.seasonpicker.CurrentSeasonHolder
import javax.inject.Inject

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
    private val currentSeasonHolder: CurrentSeasonHolder,
    private val navigator: Navigator,
    private val networkConnectivityManager: NetworkConnectivityManager,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel(), ConstructorStandingsViewModelInputs, ConstructorStandingsViewModelOutputs {

    val inputs: ConstructorStandingsViewModelInputs = this
    val outputs: ConstructorStandingsViewModelOutputs = this

    override val uiState: MutableStateFlow<ConstructorStandingsScreenState> = MutableStateFlow(
        ConstructorStandingsScreenState(season = currentSeasonHolder.currentSeason)
    )
    private val season: Int
        get() = uiState.value.season


    init {
        viewModelScope.launch(ioDispatcher) {
            currentSeasonHolder.currentSeasonFlow.collectLatest {
                uiState.value = uiState.value.copy(season = it)
                if (!populate()) {
                    refresh()
                }
            }
        }
    }

    override fun selectConstructor(constructor: SeasonConstructorStandingSeason) {
        uiState.value = uiState.value.copy(currentlySelected = constructor)
    }

    override fun closeConstructor() {
        uiState.value = uiState.value.copy(currentlySelected = null)
    }

    override fun refresh() {
        viewModelScope.launch(ioDispatcher) {
            if (uiState.value.standings.isEmpty()) {
                populate()
            }
            uiState.value = uiState.value.copy(isLoading = true, networkAvailable = networkConnectivityManager.isConnected)
            fetchSeasonUseCase.fetchSeason(season)
            populate()
        }
    }

    private suspend fun populate(): Boolean {
        val currentStandings = seasonRepository.getConstructorStandings(season).firstOrNull()?.standings ?: emptyList()
        val maxPoints = currentStandings.maxOfOrNull { it.points } ?: 800.0
        uiState.value = uiState.value.copy(
            standings = currentStandings,
            maxPoints = maxPoints,
            inProgress = currentStandings.firstOrNull()?.inProgressContent,
            isLoading = false,
            networkAvailable = networkConnectivityManager.isConnected
        )

        return currentStandings.isNotEmpty()
    }

    companion object {
        internal const val SEASON = "season"
    }
}