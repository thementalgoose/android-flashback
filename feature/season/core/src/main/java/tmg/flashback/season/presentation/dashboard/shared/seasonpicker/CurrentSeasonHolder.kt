package tmg.flashback.season.presentation.dashboard.shared.seasonpicker

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import tmg.flashback.season.repository.HomeRepository
import tmg.flashback.season.usecases.DefaultSeasonUseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrentSeasonHolder @Inject constructor(
    private val defaultSeasonUseCase: DefaultSeasonUseCase,
    private val homeRepository: HomeRepository
) {

    private val _currentSeason: MutableStateFlow<Int> = MutableStateFlow(defaultSeasonUseCase.defaultSeason)
    val currentSeasonFlow: StateFlow<Int> = _currentSeason
    val currentSeason: Int
        get() = currentSeasonFlow.value

    private val _supportedSeasons: MutableStateFlow<List<Int>> = MutableStateFlow(homeRepository.supportedSeasons.sortedByDescending { it })
    val supportedSeasonFlow: StateFlow<List<Int>> = _supportedSeasons
    val supportedSeasons: List<Int>
        get() = _supportedSeasons.value

    val defaultSeason: Int
        get() = defaultSeasonUseCase.defaultSeason

    fun updateTo(season: Int) {
        if (supportedSeasons.contains(season)) {
            _currentSeason.value = season
        }
        update()
    }

    private fun update() {
        val seasons = homeRepository.supportedSeasons.sortedByDescending { it }
        _supportedSeasons.value = seasons
        if (!seasons.contains(currentSeason)) {
            _currentSeason.value = defaultSeasonUseCase.defaultSeason
        }
    }
}