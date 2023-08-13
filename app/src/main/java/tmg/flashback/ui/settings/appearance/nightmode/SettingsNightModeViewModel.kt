package tmg.flashback.ui.settings.appearance.nightmode

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import tmg.flashback.ui.model.NightMode
import tmg.flashback.ui.repository.ThemeRepository
import tmg.flashback.ui.usecases.ChangeNightModeUseCase
import javax.inject.Inject

//region Inputs

interface SettingsNightModeViewModelInputs {
    fun selectNightMode(nightMode: NightMode)
}

//endregion

//region Outputs

interface SettingsNightModeViewModelOutputs {
    val currentlySelected: StateFlow<NightMode>
}

//endregion

@HiltViewModel
class SettingsNightModeViewModel @Inject constructor(
    private val themeRepository: ThemeRepository,
    private val changeNightModeUseCase: ChangeNightModeUseCase
): ViewModel(), SettingsNightModeViewModelInputs, SettingsNightModeViewModelOutputs {

    var inputs: SettingsNightModeViewModelInputs = this
    var outputs: SettingsNightModeViewModelOutputs = this

    override val currentlySelected: MutableStateFlow<NightMode> = MutableStateFlow(NightMode.DEFAULT)

    init {
        currentlySelected.value = themeRepository.nightMode
    }

    //region Inputs

    override fun selectNightMode(nightMode: NightMode) {
        changeNightModeUseCase.setNightMode(nightMode)
        currentlySelected.value = themeRepository.nightMode
    }

    //endregion
}
