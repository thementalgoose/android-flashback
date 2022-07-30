package tmg.flashback.stats.ui.dashboard.constructors

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import tmg.flashback.statistics.repo.SeasonRepository
import tmg.flashback.stats.StatsNavigationComponent
import tmg.flashback.stats.usecases.FetchSeasonUseCase
import javax.inject.Inject

interface ConstructorsStandingViewModelInputs {
    fun refresh()
    fun load(season: Int)

    fun clickItem(model: ConstructorStandingsModel.Standings)
}

interface ConstructorsStandingViewModelOutputs {
    val items: LiveData<List<ConstructorStandingsModel>?>
    val isRefreshing: LiveData<Boolean>
}

@HiltViewModel
class ConstructorsStandingViewModel @Inject constructor(
    private val seasonRepository: SeasonRepository,
    private val fetchSeasonUseCase: FetchSeasonUseCase,
    private val statsNavigationComponent: StatsNavigationComponent,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel(), ConstructorsStandingViewModelInputs, ConstructorsStandingViewModelOutputs {

    val inputs: ConstructorsStandingViewModelInputs = this
    val outputs: ConstructorsStandingViewModelOutputs = this

    override val isRefreshing: MutableLiveData<Boolean> = MutableLiveData(false)

    private val season: MutableStateFlow<Int?> = MutableStateFlow(null)
    override val items: LiveData<List<ConstructorStandingsModel>?> = season
        .filterNotNull()
        .flatMapLatest { season ->
            isRefreshing.postValue(true)
            fetchSeasonUseCase
                .fetch(season)
                .flatMapLatest { hasMadeRequest ->
                    seasonRepository.getConstructorStandings(season)
                        .map {
                            isRefreshing.postValue(false)
                            if (!hasMadeRequest) {
                                return@map listOf(ConstructorStandingsModel.Loading)
                            }
                            if (it == null) {
                                return@map null
                            }
                            return@map it.standings
                                .sortedBy { it.championshipPosition }
                                .map {
                                    ConstructorStandingsModel.Standings(it)
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

    override fun clickItem(model: ConstructorStandingsModel.Standings) {
        statsNavigationComponent.constructorOverview(
            id = model.standings.constructor.id,
            name = model.standings.constructor.name
        )
    }
}