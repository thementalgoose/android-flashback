package tmg.flashback.stats.ui.weekend.sprint

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.map
import tmg.flashback.formula1.constants.Formula1
import tmg.flashback.statistics.repo.RaceRepository
import tmg.flashback.stats.ui.weekend.race.RaceModel
import javax.inject.Inject

interface SprintViewModelInputs {
    fun load(season: Int, round: Int)
}

interface SprintViewModelOutputs {
    val list: LiveData<List<SprintModel>>
}

@HiltViewModel
class SprintViewModel @Inject constructor(
    private val raceRepository: RaceRepository,
    private val ioDispatcher: CoroutineDispatcher
): ViewModel(), SprintViewModelInputs, SprintViewModelOutputs {

    val inputs: SprintViewModelInputs = this
    val outputs: SprintViewModelOutputs = this

    private val seasonRound: MutableStateFlow<Pair<Int, Int>?> = MutableStateFlow(null)
    override val list: LiveData<List<SprintModel>> = seasonRound
        .filterNotNull()
        .flatMapLatest { (season, round) -> raceRepository.getRace(season, round) }
        .flowOn(ioDispatcher)
        .map { race ->
            if (race == null || race.sprint.isEmpty()) {
                val list = mutableListOf<SprintModel>().apply {
                    if ((seasonRound.value?.first ?: Formula1.currentSeasonYear) >= Formula1.currentSeasonYear) {
                        add(SprintModel.NotAvailableYet)
                    } else {
                        add(SprintModel.NotAvailable)
                    }
                }
                return@map list
            }

            return@map race
                .sprint
                .map { model ->
                    SprintModel.Result(model)
                }
                .sortedBy { it.result.finish }
        }
        .asLiveData(viewModelScope.coroutineContext)

    override fun load(season: Int, round: Int) {
        seasonRound.value = Pair(season, round)
    }
}