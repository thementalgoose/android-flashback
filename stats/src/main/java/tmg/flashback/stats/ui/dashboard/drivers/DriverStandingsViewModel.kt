package tmg.flashback.stats.ui.dashboard.drivers

import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import tmg.flashback.formula1.model.SeasonDriverStandingSeason
import tmg.flashback.statistics.repo.SeasonRepository
import tmg.flashback.stats.usecases.FetchSeasonUseCase

interface DriversStandingViewModelInputs {
    fun refresh()
}

interface DriversStandingViewModelOutputs {
    val items: LiveData<List<DriverStandingsModel>?>
    val isRefreshing: LiveData<Boolean>
}

class DriversStandingViewModel(
    private val seasonRepository: SeasonRepository,
    private val fetchSeasonUseCase: FetchSeasonUseCase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel(), DriversStandingViewModelInputs, DriversStandingViewModelOutputs {

    val inputs: DriversStandingViewModelInputs = this
    val outputs: DriversStandingViewModelOutputs = this

    override val isRefreshing: MutableLiveData<Boolean> = MutableLiveData(false)

    private val season: MutableStateFlow<Int> = MutableStateFlow(2022)
    override val items: LiveData<List<DriverStandingsModel>?> = season
        .flatMapLatest { season ->
            isRefreshing.postValue(true)
            fetchSeasonUseCase
                .fetch(season)
                .flatMapLatest { hasMadeRequest ->
                    seasonRepository.getDriverStandings(season)
                        .map {
                            isRefreshing.postValue(false)
                            if (!hasMadeRequest && it == null) {
                                return@map null
                            }
                            return@map it?.standings
                                ?.sortedBy { it.championshipPosition }
                                ?.map {
                                    DriverStandingsModel(standings = it)
                                }
                        }
                }
        }
        .flowOn(ioDispatcher)
        .asLiveData(viewModelScope.coroutineContext)

    override fun refresh() {
        isRefreshing.postValue(true)
        viewModelScope.launch(ioDispatcher) {
            fetchSeasonUseCase.fetchSeason(season.value)
            isRefreshing.postValue(false)
        }
    }
}