package tmg.flashback.ui.settings.appearance.theme

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import tmg.flashback.R
import tmg.flashback.ui.managers.ToastManager
import tmg.flashback.ui.model.Theme
import tmg.flashback.ui.repository.ThemeRepository
import javax.inject.Inject

//region Inputs

interface SettingsThemeViewModelInputs {
    fun selectTheme(theme: Theme)
}

//endregion

//region Outputs

interface SettingsThemeViewModelOutputs {
    val currentlySelected: StateFlow<Theme>
}

//endregion

@HiltViewModel
class SettingsThemeViewModel @Inject constructor(
    private val themeRepository: ThemeRepository,
    private val toastManager: ToastManager
): ViewModel(), SettingsThemeViewModelInputs, SettingsThemeViewModelOutputs {

    var inputs: SettingsThemeViewModelInputs = this
    var outputs: SettingsThemeViewModelOutputs = this

    override val currentlySelected: MutableStateFlow<Theme> = MutableStateFlow(Theme.DEFAULT)

    init {
        currentlySelected.value = themeRepository.theme
    }

    //region Inputs

    override fun selectTheme(theme: Theme) {
        val existing = theme == themeRepository.theme
        if (!existing) {
            toastManager.displayToast(R.string.settings_restart_app_required)
        }
        themeRepository.theme = theme
        currentlySelected.value = themeRepository.theme
    }

    //endregion
}
