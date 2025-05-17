package tmg.flashback.presentation.settings.data

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import tmg.flashback.season.repository.HomeRepository
import tmg.flashback.ui.settings.Setting
import tmg.flashback.presentation.settings.Settings
import javax.inject.Inject

interface SettingsLayoutViewModelInputs {
    fun prefClicked(pref: Setting)
}

interface SettingsLayoutViewModelOutputs {
    val collapsedListEnabled: StateFlow<Boolean>
    val emptyWeeksInSchedule: StateFlow<Boolean>
    val recentHighlights: StateFlow<Boolean>
    val rememberSeasonChange: StateFlow<Boolean>
}

@HiltViewModel
class SettingsLayoutViewModel @Inject constructor(
    private val homeRepository: HomeRepository
): ViewModel(), SettingsLayoutViewModelInputs, SettingsLayoutViewModelOutputs {

    val inputs: SettingsLayoutViewModelInputs = this
    val outputs: SettingsLayoutViewModelOutputs = this

    override val collapsedListEnabled: MutableStateFlow<Boolean> = MutableStateFlow(homeRepository.collapseList)
    override val emptyWeeksInSchedule: MutableStateFlow<Boolean> = MutableStateFlow(homeRepository.emptyWeeksInSchedule)
    override val recentHighlights: MutableStateFlow<Boolean> = MutableStateFlow(homeRepository.recentHighlights)
    override val rememberSeasonChange: MutableStateFlow<Boolean> = MutableStateFlow(homeRepository.rememberSeasonChange)

    override fun prefClicked(pref: Setting) {
        when (pref.key) {
            Settings.Data.collapseListKey -> {
                homeRepository.collapseList = !homeRepository.collapseList
                collapsedListEnabled.value = homeRepository.collapseList
            }
            Settings.Data.emptyWeeksInSchedule -> {
                homeRepository.emptyWeeksInSchedule = !homeRepository.emptyWeeksInSchedule
                emptyWeeksInSchedule.value = homeRepository.emptyWeeksInSchedule
            }
            Settings.Data.recentHighlights -> {
                homeRepository.recentHighlights = !homeRepository.recentHighlights
                recentHighlights.value = homeRepository.recentHighlights
            }
            Settings.Data.rememberSeasonChange -> {
                homeRepository.rememberSeasonChange = !homeRepository.rememberSeasonChange
                rememberSeasonChange.value = homeRepository.rememberSeasonChange
            }
        }
    }
}