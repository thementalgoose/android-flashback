package tmg.flashback.season.presentation.dashboard.drivers

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
import tmg.flashback.data.repo.SeasonRepository
import tmg.flashback.data.repo.usecases.FetchSeasonUseCase
import tmg.flashback.formula1.model.SeasonDriverStandingSeason
import tmg.flashback.reviews.usecases.AppSection
import tmg.flashback.reviews.usecases.AppSection.HOME_STANDINGS
import tmg.flashback.reviews.usecases.ReviewSectionSeenUseCase
import tmg.flashback.season.presentation.dashboard.shared.seasonpicker.CurrentSeasonHolder
import javax.inject.Inject

interface DriverStandingsViewModelInputs {
    fun selectDriver(driver: SeasonDriverStandingSeason)
    fun selectComparison()
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
    private val networkConnectivityManager: NetworkConnectivityManager,
    private val reviewSectionSeenUseCase: ReviewSectionSeenUseCase,
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
                if (!populate()) {
                    refresh()
                }
            }
        }
        reviewSectionSeenUseCase.invoke(HOME_STANDINGS)
    }

    override fun selectDriver(driver: SeasonDriverStandingSeason) {
        uiState.value = uiState.value.copy(currentlySelected = Selected.Driver(driver))
    }

    override fun selectComparison() {
        uiState.value = uiState.value.copy(currentlySelected = Selected.Comparison)
    }

    override fun closeDriverDetails() {
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
        val currentStandings = seasonRepository.getDriverStandings(season).firstOrNull()?.standings ?: emptyList()
        val maxPoints = currentStandings.maxOfOrNull { it.points } ?: 850.0

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