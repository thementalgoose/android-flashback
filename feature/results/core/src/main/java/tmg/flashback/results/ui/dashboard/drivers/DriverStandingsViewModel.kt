package tmg.flashback.results.ui.dashboard.drivers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import tmg.flashback.domain.repo.SeasonRepository
import tmg.flashback.drivers.contract.Driver
import tmg.flashback.drivers.contract.with
import tmg.flashback.navigation.Navigator
import tmg.flashback.navigation.Screen
import tmg.flashback.results.usecases.FetchSeasonUseCase
import javax.inject.Inject

interface DriversStandingViewModelInputs {
    fun refresh()
    fun load(season: Int)

    fun clickItem(model: DriverStandingsModel.Standings)
}

interface DriversStandingViewModelOutputs {
    val items: StateFlow<List<DriverStandingsModel>?>
    val isRefreshing: StateFlow<Boolean>
}

@HiltViewModel
class DriversStandingViewModel @Inject constructor(
    private val seasonRepository: SeasonRepository,
    private val fetchSeasonUseCase: FetchSeasonUseCase,
    private val navigator: Navigator,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel(), DriversStandingViewModelInputs, DriversStandingViewModelOutputs {

    val inputs: DriversStandingViewModelInputs = this
    val outputs: DriversStandingViewModelOutputs = this

    override val isRefreshing: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private val season: MutableStateFlow<Int?> = MutableStateFlow(null)
    override val items: StateFlow<List<DriverStandingsModel>?> = season
        .filterNotNull()
        .flatMapLatest { season ->
            isRefreshing.value = true
            fetchSeasonUseCase
                .fetch(season)
                .flatMapLatest { hasMadeRequest ->
                    seasonRepository.getDriverStandings(season)
                        .map {
                            isRefreshing.value = false
                            if (!hasMadeRequest) {
                                return@map listOf(DriverStandingsModel.Loading)
                            }
                            if (it == null) {
                                return@map null
                            }
                            return@map it.standings
                                .sortedBy { it.championshipPosition }
                                .map {
                                    DriverStandingsModel.Standings(standings = it)
                                }
                        }
                }
        }
        .flowOn(ioDispatcher)
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    override fun load(season: Int) {
        this.season.value = season
    }

    override fun refresh() {
        val season = season.value ?: return

        isRefreshing.value = true
        viewModelScope.launch(ioDispatcher) {
            fetchSeasonUseCase.fetchSeason(season)
            isRefreshing.value = false
        }
    }

    override fun clickItem(model: DriverStandingsModel.Standings) {
        navigator.navigate(
            Screen.Driver.with(
            driverId = model.standings.driver.id,
            driverName = model.standings.driver.name
        ))
    }
}