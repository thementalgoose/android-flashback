package tmg.flashback.ui.settings.web

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import tmg.flashback.ui.settings.Setting
import tmg.flashback.ui.settings.Settings
import tmg.flashback.web.repository.WebBrowserRepository
import javax.inject.Inject

interface SettingsWebViewModelInputs {
    fun prefClicked(pref: Setting)
}

interface SettingsWebViewModelOutputs {
    val enable: StateFlow<Boolean>
    val enableJavascript: StateFlow<Boolean>
}

@HiltViewModel
class SettingsWebViewModel @Inject constructor(
    private val webBrowserRepository: WebBrowserRepository,
): ViewModel(), SettingsWebViewModelInputs, SettingsWebViewModelOutputs {

    val inputs: SettingsWebViewModelInputs = this
    val outputs: SettingsWebViewModelOutputs = this

    override val enable: MutableStateFlow<Boolean> = MutableStateFlow(!webBrowserRepository.openInExternal)
    override val enableJavascript: MutableStateFlow<Boolean> = MutableStateFlow(webBrowserRepository.enableJavascript)

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