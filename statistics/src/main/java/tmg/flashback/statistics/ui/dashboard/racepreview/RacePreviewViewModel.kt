package tmg.flashback.statistics.ui.dashboard.racepreview

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.statistics.repo.OverviewRepository

//region Inputs

interface RacePreviewViewModelInputs {
    fun input(season: Int, round: Int)
}

//endregion

//region Outputs

interface RacePreviewViewModelOutputs {
    val overview: LiveData<OverviewRace?>
}

//endregion

class RacePreviewViewModel(
    private val repository: OverviewRepository
): ViewModel(), RacePreviewViewModelInputs, RacePreviewViewModelOutputs {

    val inputs: RacePreviewViewModelInputs = this
    val outputs: RacePreviewViewModelOutputs = this

    private val seasonRound: MutableStateFlow<Pair<Int, Int>> = MutableStateFlow(Pair(0, 0))

    override val overview: LiveData<OverviewRace?> = seasonRound
        .filter { it.first != 0 && it.second != 0 }
        .flatMapLatest { (season, round) -> repository.getOverview(season, round) }
        .asLiveData(viewModelScope.coroutineContext)

    override fun input(season: Int, round: Int) {
        seasonRound.value = Pair(season, round)
    }
}