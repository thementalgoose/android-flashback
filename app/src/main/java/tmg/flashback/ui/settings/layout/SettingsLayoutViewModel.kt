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
    val menuAllExpandedEnabled: LiveData<Boolean>
    val menuFavouriteExpandedEnabled: LiveData<Boolean>
    val providedByAtTopEnabled: LiveData<Boolean>
}

@HiltViewModel
class SettingsLayoutViewModel @Inject constructor(
    private val homeRepository: HomeRepository
): ViewModel(), SettingsLayoutViewModelInputs, SettingsLayoutViewModelOutputs {

    val inputs: SettingsLayoutViewModelInputs = this
    val outputs: SettingsLayoutViewModelOutputs = this

    override val menuAllExpandedEnabled: MutableLiveData<Boolean> = MutableLiveData(homeRepository.showListAll)
    override val menuFavouriteExpandedEnabled: MutableLiveData<Boolean> = MutableLiveData(homeRepository.showListFavourited)
    override val providedByAtTopEnabled: MutableLiveData<Boolean> = MutableLiveData(homeRepository.dataProvidedByAtTop)

    override fun prefClicked(pref: Setting) {
        when (pref.key) {
            Settings.Layout.menuAllExpandedKey -> {
                homeRepository.showListAll = !homeRepository.showListAll
                menuAllExpandedEnabled.value = homeRepository.showListAll
            }
            Settings.Layout.menuFavouriteExpandedKey -> {
                homeRepository.showListFavourited = !homeRepository.showListFavourited
                menuFavouriteExpandedEnabled.value = homeRepository.showListFavourited
            }
            Settings.Layout.providedByAtTopKey -> {
                homeRepository.dataProvidedByAtTop = !homeRepository.dataProvidedByAtTop
                providedByAtTopEnabled.value = homeRepository.dataProvidedByAtTop
            }
        }
    }

}