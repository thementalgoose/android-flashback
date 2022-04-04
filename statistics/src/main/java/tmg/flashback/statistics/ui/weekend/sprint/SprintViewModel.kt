package tmg.flashback.statistics.ui.weekend.sprint

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import tmg.flashback.statistics.repo.RaceRepository

interface SprintViewModelInputs {
    fun load(season: Int, round: Int)
}

interface SprintViewModelOutputs {
    val list: LiveData<List<SprintModel>>
}

class SprintViewModel(
    private val raceRepository: RaceRepository
): ViewModel(), SprintViewModelInputs, SprintViewModelOutputs {

    val inputs: SprintViewModelInputs = this
    val outputs: SprintViewModelOutputs = this

    private val seasonRound: MutableStateFlow<Pair<Int, Int>?> = MutableStateFlow(null)
    override val list: LiveData<List<SprintModel>> = seasonRound
        .filterNotNull()
        .flatMapLatest { (season, round) -> raceRepository.getRace(season, round) }
        .map {
            val sprint = it?.sprint ?: return@map emptyList<SprintModel>()

            return@map sprint
                .map { model ->
                    SprintModel(model)
                }
                .sortedBy { it.result.finish }
        }
        .asLiveData(viewModelScope.coroutineContext)

    override fun load(season: Int, round: Int) {
        seasonRound.value = Pair(season, round)
    }
}