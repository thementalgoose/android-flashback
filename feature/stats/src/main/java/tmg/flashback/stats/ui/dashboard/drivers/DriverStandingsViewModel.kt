package tmg.flashback.stats.ui.dashboard.drivers

import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import tmg.flashback.statistics.repo.SeasonRepository
import tmg.flashback.stats.StatsNavigationComponent
import tmg.flashback.stats.usecases.FetchSeasonUseCase

interface DriversStandingViewModelInputs {
    fun refresh()
    fun load(season: Int)

    fun clickItem(model: DriverStandingsModel.Standings)
}

interface DriversStandingViewModelOutputs {
    val items: LiveData<List<DriverStandingsModel>?>
    val isRefreshing: LiveData<Boolean>
}

class DriversStandingViewModel(
    private val seasonRepository: SeasonRepository,
    private val fetchSeasonUseCase: FetchSeasonUseCase,
    private val statsNavigator: StatsNavigationComponent,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel(), DriversStandingViewModelInputs, DriversStandingViewModelOutputs {

    val inputs: DriversStandingViewModelInputs = this
    val outputs: DriversStandingViewModelOutputs = this

    override val isRefreshing: MutableLiveData<Boolean> = MutableLiveData(false)

    private val season: MutableStateFlow<Int?> = MutableStateFlow(null)
    override val items: LiveData<List<DriverStandingsModel>?> = season
        .filterNotNull()
        .flatMapLatest { season ->
            isRefreshing.postValue(true)
            fetchSeasonUseCase
                .fetch(season)
                .flatMapLatest { hasMadeRequest ->
                    seasonRepository.getDriverStandings(season)
                        .map {
                            isRefreshing.postValue(false)
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
        .asLiveData(viewModelScope.coroutineContext)

    override fun load(season: Int) {
        this.season.value = season
    }

    override fun refresh() {
        val season = season.value ?: return

        isRefreshing.postValue(true)
        viewModelScope.launch(ioDispatcher) {
            fetchSeasonUseCase.fetchSeason(season)
            isRefreshing.postValue(false)
        }
    }

    override fun clickItem(model: DriverStandingsModel.Standings) {
        statsNavigator.driverOverview(
            id = model.standings.driver.id,
            name = model.standings.driver.name
        )
    }
}