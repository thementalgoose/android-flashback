package tmg.flashback.web.ui.settings

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import tmg.flashback.ui.settings.SettingsModel
import tmg.flashback.ui.settings.SettingsViewModel
import tmg.flashback.web.R
import tmg.flashback.web.repository.WebBrowserRepository
import javax.inject.Inject

interface SettingsWebBrowserViewModelInputs {

}

interface SettingsWebBrowserViewModelOutputs {

}

@HiltViewModel
internal class SettingsWebBrowserViewModel @Inject constructor(
    private val webBrowserRepository: WebBrowserRepository
): SettingsViewModel(), SettingsWebBrowserViewModelInputs, SettingsWebBrowserViewModelOutputs {

    override val models: List<SettingsModel> get() = mutableListOf<SettingsModel>().apply {
        add(SettingsModel.Header(R.string.settings_web_browser_title))
        add(
            SettingsModel.SwitchPref(
                title = R.string.settings_web_browser_in_app_title,
                description = R.string.settings_web_browser_in_app_description,
                getState = { webBrowserRepository.openInExternal },
                saveState = { webBrowserRepository.openInExternal = it }
        ))
        add(
            SettingsModel.SwitchPref(
                title = R.string.settings_web_browser_javascript_title,
                description = R.string.settings_web_browser_javascript_description,
                getState = { webBrowserRepository.enableJavascript },
                saveState = { webBrowserRepository.enableJavascript = it }
        ))
    }

    var inputs: SettingsWebBrowserViewModelInputs = this
    var outputs: SettingsWebBrowserViewModelOutputs = this

    init {

    }

    //region Inputs

    //endregion

    //region Outputs

    //endregion
}