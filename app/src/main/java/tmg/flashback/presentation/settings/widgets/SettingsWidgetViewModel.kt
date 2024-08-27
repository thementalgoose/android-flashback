package tmg.flashback.presentation.settings.widgets

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import tmg.flashback.presentation.settings.Settings
import tmg.flashback.ui.settings.Setting
import tmg.flashback.weekend.repository.WeatherRepository
import tmg.flashback.widgets.repository.WidgetRepository
import javax.inject.Inject

interface SettingsWidgetViewModelInputs {
    fun prefClicked(pref: Setting)
}

interface SettingsWidgetViewModelOutputs {
    val showBackground: StateFlow<Boolean>
    val deeplinkToEvent: StateFlow<Boolean>
}

@HiltViewModel
class SettingsWidgetViewModel @Inject constructor(
    private val widgetRepository: WidgetRepository
): ViewModel(), SettingsWidgetViewModelInputs, SettingsWidgetViewModelOutputs {

    val inputs: SettingsWidgetViewModelInputs = this
    val outputs: SettingsWidgetViewModelOutputs = this

    override val showBackground: MutableStateFlow<Boolean> = MutableStateFlow(widgetRepository.showBackground)
    override val deeplinkToEvent: MutableStateFlow<Boolean> = MutableStateFlow(widgetRepository.deeplinkToEvent)

    override fun prefClicked(pref: Setting) {
        when (pref.key) {
            Settings.Widgets.showBackground -> {
                widgetRepository.showBackground = !widgetRepository.showBackground
                showBackground.value = widgetRepository.showBackground
            }
            Settings.Widgets.deeplinkToEvent -> {
                widgetRepository.deeplinkToEvent = !widgetRepository.deeplinkToEvent
                deeplinkToEvent.value = widgetRepository.deeplinkToEvent
            }
        }
    }

}