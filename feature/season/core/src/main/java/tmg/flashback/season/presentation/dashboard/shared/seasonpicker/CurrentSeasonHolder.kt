package tmg.flashback.season.presentation.dashboard.shared.seasonpicker

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import tmg.flashback.formula1.constants.Formula1.currentSeasonYear
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

    private val _newSeasonAvailable: MutableStateFlow<Boolean> = MutableStateFlow(newSeasonAvailable())
    val newSeasonAvailableFlow: StateFlow<Boolean> = _newSeasonAvailable

    val defaultSeason: Int
        get() = defaultSeasonUseCase.defaultSeason

    init {
        homeRepository.viewedSeasons = homeRepository.viewedSeasons + currentSeason
    }

    fun updateTo(season: Int) {
        homeRepository.viewedSeasons = (homeRepository.viewedSeasons + season)
        if (supportedSeasons.contains(season)) {
            _currentSeason.value = season
        }
        refresh()
    }

    fun refresh() {
        val seasons = homeRepository.supportedSeasons.sortedByDescending { it }
        _supportedSeasons.value = seasons
        _newSeasonAvailable.value = newSeasonAvailable()
        if (!seasons.contains(currentSeason)) {
            _currentSeason.value = defaultSeasonUseCase.defaultSeason
        }
    }

    private fun newSeasonAvailable(): Boolean {
        val viewedSeasons = homeRepository.viewedSeasons
        val newSeasons = supportedSeasons.filter { it > currentSeason }
        return newSeasons.any { season -> season !in viewedSeasons }
    }
}