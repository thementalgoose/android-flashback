package tmg.flashback.ui.settings.appearance.theme

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import tmg.flashback.R
import tmg.flashback.ui.managers.ToastManager
import tmg.flashback.ui.model.Theme
import tmg.flashback.ui.repository.ThemeRepository
import javax.inject.Inject

//region Inputs

interface ThemeViewModelInputs {
    fun selectTheme(theme: Theme)
}

//endregion

//region Outputs

interface ThemeViewModelOutputs {
    val currentlySelected: LiveData<Theme>
}

//endregion

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val themeRepository: ThemeRepository,
    private val toastManager: ToastManager
): ViewModel(), ThemeViewModelInputs, ThemeViewModelOutputs {

    var inputs: ThemeViewModelInputs = this
    var outputs: ThemeViewModelOutputs = this

    override val currentlySelected: MutableLiveData<Theme> = MutableLiveData()

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
    }

    //endregion
}
