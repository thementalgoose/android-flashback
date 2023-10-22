package tmg.flashback.season.presentation.dashboard.shared.seasonpicker

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

interface SeasonTitleViewModelInputs {
    fun currentSeasonUpdate(season: Int)
}

interface SeasonTitleViewModelOutputs {
    val currentSeason: StateFlow<Int>
    val supportedSeasons: StateFlow<List<Int>>
}

@HiltViewModel
class SeasonTitleViewModel @Inject constructor(
    private val currentSeasonHolder: CurrentSeasonHolder
): ViewModel(), SeasonTitleViewModelInputs,
    SeasonTitleViewModelOutputs {

    val inputs: SeasonTitleViewModelInputs = this
    val outputs: SeasonTitleViewModelOutputs = this

    override val currentSeason: StateFlow<Int> = currentSeasonHolder.currentSeasonFlow

    override val supportedSeasons: StateFlow<List<Int>> = currentSeasonHolder.supportedSeasonFlow

    override fun currentSeasonUpdate(season: Int) {
        currentSeasonHolder.updateTo(season)
    }
}