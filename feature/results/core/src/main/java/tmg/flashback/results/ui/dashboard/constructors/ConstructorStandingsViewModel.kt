package tmg.flashback.results.ui.dashboard.constructors

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
import tmg.flashback.constructors.contract.Constructor
import tmg.flashback.constructors.contract.with
import tmg.flashback.domain.repo.SeasonRepository
import tmg.flashback.navigation.Navigator
import tmg.flashback.navigation.Screen
import tmg.flashback.results.usecases.DefaultSeasonUseCase
import tmg.flashback.results.usecases.FetchSeasonUseCase
import javax.inject.Inject

interface ConstructorsStandingViewModelInputs {
    fun refresh()
    fun load(season: Int?)

    fun clickItem(model: ConstructorStandingsModel.Standings)
}

interface ConstructorsStandingViewModelOutputs {
    val items: StateFlow<List<ConstructorStandingsModel>?>
    val isRefreshing: StateFlow<Boolean>
}

@HiltViewModel
class ConstructorsStandingViewModel @Inject constructor(
    private val seasonRepository: SeasonRepository,
    private val fetchSeasonUseCase: FetchSeasonUseCase,
    private val navigator: Navigator,
    private val defaultSeasonUseCase: DefaultSeasonUseCase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel(), ConstructorsStandingViewModelInputs, ConstructorsStandingViewModelOutputs {

    val inputs: ConstructorsStandingViewModelInputs = this
    val outputs: ConstructorsStandingViewModelOutputs = this

    override val isRefreshing: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private val season: MutableStateFlow<Int?> = MutableStateFlow(null)
    override val items: StateFlow<List<ConstructorStandingsModel>?> = season
        .filterNotNull()
        .flatMapLatest { season ->
            isRefreshing.value = true
            fetchSeasonUseCase
                .fetch(season)
                .flatMapLatest { hasMadeRequest ->
                    seasonRepository.getConstructorStandings(season)
                        .map {
                            isRefreshing.value = false
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
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    override fun load(season: Int?) {
        if (season == null) {
            this.season.value = defaultSeasonUseCase.defaultSeason
        } else {
            this.season.value = season
        }
    }

    override fun refresh() {
        val season = season.value ?: return

        isRefreshing.value = true
        viewModelScope.launch(ioDispatcher) {
            fetchSeasonUseCase.fetchSeason(season)
            isRefreshing.value = false
        }
    }

    override fun clickItem(model: ConstructorStandingsModel.Standings) {
        navigator.navigate(
            Screen.Constructor.with(
            constructorId = model.standings.constructor.id,
            constructorName = model.standings.constructor.name
        ))
    }
}