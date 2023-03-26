package tmg.flashback.ui.settings.layout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import tmg.flashback.results.repository.HomeRepository
import tmg.flashback.ui.settings.Setting
import tmg.flashback.ui.settings.Settings
import javax.inject.Inject

interface SettingsLayoutViewModelInputs {
    fun prefClicked(pref: Setting)
}

interface SettingsLayoutViewModelOutputs {
    val collapsedListEnabled: LiveData<Boolean>
}

@HiltViewModel
class SettingsLayoutViewModel @Inject constructor(
    private val homeRepository: HomeRepository
): ViewModel(), SettingsLayoutViewModelInputs, SettingsLayoutViewModelOutputs {

    val inputs: SettingsLayoutViewModelInputs = this
    val outputs: SettingsLayoutViewModelOutputs = this

    override val collapsedListEnabled: MutableLiveData<Boolean> = MutableLiveData(homeRepository.collapseList)

    override fun prefClicked(pref: Setting) {
        when (pref.key) {
            Settings.Layout.collapseListKey -> {
                homeRepository.collapseList = !homeRepository.collapseList
                collapsedListEnabled.value = homeRepository.collapseList
            }
        }
    }

}