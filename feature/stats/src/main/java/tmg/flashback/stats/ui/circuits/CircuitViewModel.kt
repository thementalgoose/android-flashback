package tmg.flashback.stats.ui.circuits

import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import tmg.flashback.device.managers.NetworkConnectivityManager
import tmg.flashback.formula1.model.CircuitHistory
import tmg.flashback.statistics.repo.CircuitRepository
import tmg.flashback.stats.StatsNavigationComponent
import tmg.flashback.stats.ui.weekend.WeekendInfo
import tmg.flashback.ui.navigation.ApplicationNavigationComponent

interface CircuitViewModelInputs {
    fun load(circuitId: String)
    fun linkClicked(link: String)
    fun itemClicked(model: CircuitModel.Item)
    fun refresh()
}

interface CircuitViewModelOutputs {
    val list: LiveData<List<CircuitModel>>
    val showLoading: LiveData<Boolean>
}

class CircuitViewModel(
    private val circuitRepository: CircuitRepository,
    private val networkConnectivityManager: NetworkConnectivityManager,
    private val applicationNavigationComponent: ApplicationNavigationComponent,
    private val statsNavigationComponent: StatsNavigationComponent,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel(), CircuitViewModelInputs, CircuitViewModelOutputs {

    val inputs: CircuitViewModelInputs = this
    val outputs: CircuitViewModelOutputs = this

    override val showLoading: MutableLiveData<Boolean> = MutableLiveData()

    private val isConnected: Boolean
        get() = networkConnectivityManager.isConnected

    private val circuitId: MutableStateFlow<String?> = MutableStateFlow(null)
    private val circuitIdWithRequest: Flow<String?> = circuitId
        .filterNotNull()
        .flatMapLatest { id ->
            return@flatMapLatest flow {
                if (circuitRepository.getCircuitRounds(id) == 0) {
                    showLoading.postValue(true)
                    emit(null)
                    circuitRepository.fetchCircuit(id)
                    showLoading.postValue(false)
                    emit(id)
                }
                else {
                    emit(id)
                }
            }
        }
        .flowOn(ioDispatcher)


    override val list: LiveData<List<CircuitModel>> = circuitIdWithRequest
        .flatMapLatest { id ->
            if (id == null) {
                return@flatMapLatest flow {
                    emit(mutableListOf(CircuitModel.Loading))
                }
            }

            return@flatMapLatest circuitRepository.getCircuitHistory(id)
                .map {
                    val list = mutableListOf<CircuitModel>()
                    it?.let { list.add(it.toStatModel()) }
                    when {
                        (it == null || it.results.isEmpty()) && !isConnected -> list.add(CircuitModel.Error)
                        (it == null || it.results.isEmpty()) -> list.add(CircuitModel.Error)
                        else -> {
                            list.addAll(it.results
                                .map { result ->
                                    CircuitModel.Item(
                                        circuitId = it.data.id,
                                        circuitName = it.data.name,
                                        country = it.data.country,
                                        countryISO = it.data.countryISO,
                                        result
                                    )
                                }
                                .sortedByDescending { it.data.season * 1000 + it.data.round }
                            )
                        }
                    }
                    return@map list
                }
        }
        .asLiveData(viewModelScope.coroutineContext)

    override fun load(circuitId: String) {
        this.circuitId.value = circuitId
    }

    override fun linkClicked(link: String) {
        this.applicationNavigationComponent.openUrl(link)
    }

    override fun itemClicked(model: CircuitModel.Item) {
        this.statsNavigationComponent.weekend(WeekendInfo(
            season = model.data.season,
            round = model.data.round,
            raceName = model.data.name,
            circuitId = model.circuitId,
            circuitName = model.circuitName,
            country = model.country,
            countryISO = model.countryISO,
            date = model.data.date
        ))
    }

    override fun refresh() {
        this.refresh(circuitId.value)
    }
    private fun refresh(circuitId: String? = this.circuitId.value) {
        viewModelScope.launch(context = ioDispatcher) {
            circuitId?.let {
                circuitRepository.fetchCircuit(circuitId)
                showLoading.postValue(false)
            }
        }
    }

    private fun CircuitHistory.toStatModel(): CircuitModel.Stats {
        return CircuitModel.Stats(
            circuitId = this.data.id,
            name = this.data.name,
            country = this.data.country,
            countryISO = this.data.countryISO,
            numberOfGrandPrix = this.results.size,
            startYear = this.results.minByOrNull { it.season }?.season,
            endYear = this.results.maxByOrNull { it.season }?.season,
            wikipedia = this.data.wikiUrl,
            location = this.data.location,
        )
    }
}