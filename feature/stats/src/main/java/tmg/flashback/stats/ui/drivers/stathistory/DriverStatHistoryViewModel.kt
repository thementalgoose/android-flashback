package tmg.flashback.stats.ui.drivers.stathistory

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import tmg.flashback.formula1.model.DriverHistory
import tmg.flashback.statistics.repo.DriverRepository
import tmg.utilities.extensions.combinePair
import javax.inject.Inject

interface DriverStatHistoryViewModelInputs {
    fun load(driverId: String, driverStatHistoryType: DriverStatHistoryType)
}

interface DriverStatHistoryViewModelOutputs {
    val results: LiveData<List<DriverStatHistoryModel>>
}

@HiltViewModel
class DriverStatHistoryViewModel @Inject constructor(
    private val driverRepository: DriverRepository,
    private val ioDispatcher: CoroutineDispatcher
): ViewModel(), DriverStatHistoryViewModelInputs, DriverStatHistoryViewModelOutputs {

    val inputs: DriverStatHistoryViewModelInputs = this
    val outputs: DriverStatHistoryViewModelOutputs = this

    private val driverId: MutableStateFlow<String?> = MutableStateFlow(null)
    private val statType: MutableStateFlow<DriverStatHistoryType?> = MutableStateFlow(null)

    override val results: LiveData<List<DriverStatHistoryModel>> = driverId
        .filterNotNull()
        .flatMapLatest { driverRepository.getDriverOverview(it) }
        .combinePair(statType.filterNotNull())
        .map { (driverHistory, driverStatType) ->
            if (driverHistory == null) {
                return@map listOf(DriverStatHistoryModel.Empty)
            }
            return@map when (driverStatType) {
                DriverStatHistoryType.CHAMPIONSHIPS -> buildChampionshipList(driverHistory)
                DriverStatHistoryType.WINS -> buildWinsList(driverHistory)
                DriverStatHistoryType.POLES -> buildPoleList(driverHistory)
            }
        }
        .flowOn(ioDispatcher)
        .asLiveData(viewModelScope.coroutineContext)

    override fun load(driverId: String, driverStatHistoryType: DriverStatHistoryType) {
        this.driverId.value = driverId
        this.statType.value = driverStatHistoryType
    }

    private fun buildChampionshipList(driverHistory: DriverHistory): List<DriverStatHistoryModel> {
        return driverHistory.standings
            .filter { !it.isInProgress }
            .sortedBy { it.season }
            .filter { it.championshipStanding == 1 }
            .map {
                DriverStatHistoryModel.Year(it.season)
            }
    }

    private fun buildWinsList(driverHistory: DriverHistory): List<DriverStatHistoryModel> {
        return driverHistory.standings
            .filter { it.wins >= 1 }
            .sortedBy { it.season }
            .map { it.raceOverview.filter { it.finished == 1 } }
            .flatten()
            .groupBy { it.raceInfo.season }
            .map {
                val list = mutableListOf<DriverStatHistoryModel>()
                list.add(DriverStatHistoryModel.Label(it.key.toString()))
                list.addAll(it.value
                    .map {
                        DriverStatHistoryModel.Race(
                            raceInfo = it.raceInfo,
                            constructor = it.constructor
                        )
                    }
                    .sortedBy { it.raceInfo.round }
                )
                return@map list
            }
            .flatten()
    }

    private fun buildPoleList(driverHistory: DriverHistory): List<DriverStatHistoryModel> {
        return driverHistory.standings
            .sortedBy { it.season }
            .map { it.raceOverview.filter { it.qualified == 1 } }
            .flatten()
            .groupBy { it.raceInfo.season }
            .map {
                val list = mutableListOf<DriverStatHistoryModel>()
                list.add(DriverStatHistoryModel.Label(it.key.toString()))
                list.addAll(it.value
                    .map {
                        DriverStatHistoryModel.Race(
                            raceInfo = it.raceInfo,
                            constructor = it.constructor
                        )
                    }
                    .sortedBy { it.raceInfo.round }
                )
                return@map list
            }
            .flatten()
    }
}