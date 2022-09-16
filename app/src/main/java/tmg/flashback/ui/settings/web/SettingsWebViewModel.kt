package tmg.flashback.ui.settings.web

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import tmg.flashback.ui.settings.Settings
import tmg.flashback.ui.settings.Setting
import tmg.flashback.web.repository.WebBrowserRepository
import javax.inject.Inject

interface SettingsWebViewModelInputs {
    fun prefClicked(pref: Setting)
}

interface SettingsWebViewModelOutputs {
    val enable: LiveData<Boolean>
    val enableJavascript: LiveData<Boolean>
}

@HiltViewModel
class SettingsWebViewModel @Inject constructor(
    private val webBrowserRepository: WebBrowserRepository,
): ViewModel(), SettingsWebViewModelInputs, SettingsWebViewModelOutputs {

    val inputs: SettingsWebViewModelInputs = this
    val outputs: SettingsWebViewModelOutputs = this

    override val enable: MutableLiveData<Boolean> = MutableLiveData(!webBrowserRepository.openInExternal)
    override val enableJavascript: MutableLiveData<Boolean> = MutableLiveData(webBrowserRepository.enableJavascript)

    override fun prefClicked(pref: Setting) {
        when (pref.key) {
            Settings.Web.enableKey -> {
                webBrowserRepository.openInExternal = !webBrowserRepository.openInExternal
                enable.value = !webBrowserRepository.openInExternal
            }
            Settings.Web.javascriptKey -> {
                webBrowserRepository.enableJavascript = !webBrowserRepository.enableJavascript
                enableJavascript.value = webBrowserRepository.enableJavascript
            }
        }
    }

}