package tmg.flashback.circuits.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import tmg.flashback.device.managers.NetworkConnectivityManager
import tmg.flashback.formula1.model.CircuitHistory
import tmg.flashback.domain.repo.CircuitRepository
import tmg.flashback.navigation.Navigator
import tmg.flashback.navigation.Screen
import tmg.flashback.web.usecases.OpenWebpageUseCase
import tmg.flashback.weekend.contract.Weekend
import tmg.flashback.weekend.contract.model.WeekendInfo
import tmg.flashback.weekend.contract.with
import javax.inject.Inject

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

@HiltViewModel
class CircuitViewModel @Inject constructor(
    private val circuitRepository: CircuitRepository,
    private val networkConnectivityManager: NetworkConnectivityManager,
    private val openWebpageUseCase: OpenWebpageUseCase,
    private val navigator: Navigator,
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
                        (it == null || it.results.isEmpty()) && !isConnected -> list.add(
                            CircuitModel.Error
                        )
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
        openWebpageUseCase.open(url = link, title = "")
    }

    override fun itemClicked(model: CircuitModel.Item) {
        val weekend = WeekendInfo(
            season = model.data.season,
            round = model.data.round,
            raceName = model.data.name,
            circuitId = model.circuitId,
            circuitName = model.circuitName,
            country = model.country,
            countryISO = model.countryISO,
            date = model.data.date
        )
        navigator.navigate(Screen.Weekend.with(weekend))
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