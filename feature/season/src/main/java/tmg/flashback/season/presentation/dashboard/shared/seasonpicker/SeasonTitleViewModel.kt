package tmg.flashback.season.presentation.dashboard.shared.seasonpicker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import tmg.flashback.season.repository.HomeRepository
import javax.inject.Inject

interface SeasonTitleViewModelInputs {
    fun currentSeasonUpdate(season: Int)
}

interface SeasonTitleViewModelOutputs {
    val currentSeason: StateFlow<Int>
    val supportedSeasons: StateFlow<List<Int>>
    val newSeasonAvailable: StateFlow<Boolean>
}

@HiltViewModel
class SeasonTitleViewModel @Inject constructor(
    private val currentSeasonHolder: CurrentSeasonHolder,
): ViewModel(), SeasonTitleViewModelInputs,
    SeasonTitleViewModelOutputs {

    val inputs: SeasonTitleViewModelInputs = this
    val outputs: SeasonTitleViewModelOutputs = this

    override val currentSeason: StateFlow<Int> = currentSeasonHolder.currentSeasonFlow

    override val supportedSeasons: StateFlow<List<Int>> = currentSeasonHolder.supportedSeasonFlow

    override val newSeasonAvailable: StateFlow<Boolean> = currentSeasonHolder.newSeasonAvailableFlow

    override fun currentSeasonUpdate(season: Int) {
        currentSeasonHolder.updateTo(season)
    }
}