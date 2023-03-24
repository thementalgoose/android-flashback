package tmg.flashback.stats.ui.dashboard.constructors

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import tmg.flashback.statistics.repo.SeasonRepository
import tmg.flashback.stats.Constructor
import tmg.flashback.stats.usecases.FetchSeasonUseCase
import tmg.flashback.stats.with
import tmg.flashback.navigation.Navigator
import tmg.flashback.navigation.Screen
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
    private val navigator: tmg.flashback.navigation.Navigator,
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
        navigator.navigate(
            tmg.flashback.navigation.Screen.Constructor.with(
            constructorId = model.standings.constructor.id,
            constructorName = model.standings.constructor.name
        ))
    }
}