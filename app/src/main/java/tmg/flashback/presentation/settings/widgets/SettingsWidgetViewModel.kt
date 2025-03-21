package tmg.flashback.presentation.settings.widgets

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import tmg.flashback.presentation.settings.Settings
import tmg.flashback.ui.managers.ToastManager
import tmg.flashback.ui.settings.Setting
import tmg.flashback.usecases.RefreshWidgetsUseCase
import tmg.flashback.widgets.upnext.repository.UpNextWidgetRepository
import tmg.flashback.strings.R.string
import javax.inject.Inject

interface SettingsWidgetViewModelInputs {
    fun prefClicked(pref: Setting)
    fun refreshWidgets()
}

interface SettingsWidgetViewModelOutputs {
    val showBackground: StateFlow<Boolean>
    val deeplinkToEvent: StateFlow<Boolean>
}

@HiltViewModel
class SettingsWidgetViewModel @Inject constructor(
    private val upNextWidgetRepository: UpNextWidgetRepository,
    private val refreshWidgetsUseCase: RefreshWidgetsUseCase,
    private val toastManager: ToastManager
): ViewModel(), SettingsWidgetViewModelInputs, SettingsWidgetViewModelOutputs {

    val inputs: SettingsWidgetViewModelInputs = this
    val outputs: SettingsWidgetViewModelOutputs = this

    override val showBackground: MutableStateFlow<Boolean> = MutableStateFlow(upNextWidgetRepository.showBackground)
    override val deeplinkToEvent: MutableStateFlow<Boolean> = MutableStateFlow(upNextWidgetRepository.deeplinkToEvent)

    override fun refreshWidgets() {
        refreshWidgetsUseCase.update()
    }

    override fun prefClicked(pref: Setting) {
        when (pref.key) {
            Settings.Widgets.showBackground -> {
                upNextWidgetRepository.showBackground = !upNextWidgetRepository.showBackground
                showBackground.value = upNextWidgetRepository.showBackground
            }
            Settings.Widgets.deeplinkToEvent -> {
                upNextWidgetRepository.deeplinkToEvent = !upNextWidgetRepository.deeplinkToEvent
                deeplinkToEvent.value = upNextWidgetRepository.deeplinkToEvent
            }
            Settings.Widgets.refreshWidgets.key -> {
                refreshWidgets()
                toastManager.displayToast(string.settings_section_refresh_widget_message)
            }
        }
    }

}