package tmg.flashback.ui.settings.layout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import tmg.flashback.stats.repository.HomeRepository
import tmg.flashback.ui.settings.Settings
import tmg.flashback.ui.settings.Setting
import javax.inject.Inject

interface SettingsLayoutViewModelInputs {
    fun prefClicked(pref: Setting)
}

interface SettingsLayoutViewModelOutputs {
    val providedByAtTopEnabled: LiveData<Boolean>
}

@HiltViewModel
class SettingsLayoutViewModel @Inject constructor(
    private val homeRepository: HomeRepository
): ViewModel(), SettingsLayoutViewModelInputs, SettingsLayoutViewModelOutputs {

    val inputs: SettingsLayoutViewModelInputs = this
    val outputs: SettingsLayoutViewModelOutputs = this

    override val providedByAtTopEnabled: MutableLiveData<Boolean> = MutableLiveData(homeRepository.dataProvidedByAtTop)

    override fun prefClicked(pref: Setting) {
        when (pref.key) {
            Settings.Layout.providedByAtTopKey -> {
                homeRepository.dataProvidedByAtTop = !homeRepository.dataProvidedByAtTop
                providedByAtTopEnabled.value = homeRepository.dataProvidedByAtTop
            }
        }
    }

}