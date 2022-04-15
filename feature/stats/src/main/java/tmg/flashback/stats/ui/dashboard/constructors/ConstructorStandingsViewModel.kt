package tmg.flashback.stats.ui.dashboard.constructors

import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import tmg.flashback.formula1.model.SeasonConstructorStandingSeason
import tmg.flashback.statistics.repo.SeasonRepository
import tmg.flashback.stats.di.StatsNavigator
import tmg.flashback.stats.usecases.FetchSeasonUseCase

interface ConstructorsStandingViewModelInputs {
    fun refresh()
    fun load(season: Int)

    fun clickItem(model: ConstructorStandingsModel)
}

interface ConstructorsStandingViewModelOutputs {
    val items: LiveData<List<ConstructorStandingsModel>?>
    val isRefreshing: LiveData<Boolean>
}

class ConstructorsStandingViewModel(
    private val seasonRepository: SeasonRepository,
    private val fetchSeasonUseCase: FetchSeasonUseCase,
    private val statsNavigator: StatsNavigator,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel(), ConstructorsStandingViewModelInputs, ConstructorsStandingViewModelOutputs {

    val inputs: ConstructorsStandingViewModelInputs = this
    val outputs: ConstructorsStandingViewModelOutputs = this

    override val isRefreshing: MutableLiveData<Boolean> = MutableLiveData()

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
                            if (!hasMadeRequest && it == null) {
                                return@map null
                            }
                            return@map it?.standings
                                ?.sortedBy { it.championshipPosition }
                                ?.map {
                                    ConstructorStandingsModel(it)
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

    override fun clickItem(model: ConstructorStandingsModel) {
        statsNavigator.goToConstructorOverview(
            model.standings.constructor.id,
            model.standings.constructor.name
        )
    }
}