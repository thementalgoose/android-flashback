package tmg.flashback.widgets.upnext.configure

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import tmg.flashback.widgets.repository.WidgetRepository
import tmg.flashback.widgets.usecases.UpdateWidgetsUseCaseImpl
import tmg.utilities.lifecycle.SingleLiveEvent
import javax.inject.Inject

interface UpNextConfigurationViewModelInputs {
    fun load(appWidgetId: Int)
    fun changeShowBackground(enabled: Boolean)
    fun changeShowWeather(show: Boolean)
    fun save()
    fun update()
}

interface UpNextConfigurationViewModelOutputs {
    val showBackground: StateFlow<Boolean>
    val showWeather: StateFlow<Boolean>
}

@HiltViewModel
internal class UpNextConfigurationViewModel @Inject constructor(
    private val widgetRepository: WidgetRepository,
    private val updateWidgetsUseCase: UpdateWidgetsUseCaseImpl
): ViewModel(), UpNextConfigurationViewModelInputs, UpNextConfigurationViewModelOutputs {

    val inputs: UpNextConfigurationViewModelInputs = this
    val outputs: UpNextConfigurationViewModelOutputs = this

    private var appWidgetId: Int = -1

    override val showBackground: MutableStateFlow<Boolean> = MutableStateFlow(false)
    override val showWeather: MutableStateFlow<Boolean> = MutableStateFlow(false)

    override fun load(appWidgetId: Int) {
        this.appWidgetId = appWidgetId
        showBackground.value = widgetRepository.getShowBackground(appWidgetId)
        showWeather.value = widgetRepository.getShowWeather(appWidgetId)
    }

    override fun changeShowBackground(enabled: Boolean) {
        widgetRepository.setShowBackground(appWidgetId, enabled)
        showBackground.value = enabled
    }

    override fun changeShowWeather(show: Boolean) {
        widgetRepository.setShowWeather(appWidgetId, show)
        showWeather.value = show
    }

    override fun update() {
        updateWidgetsUseCase.update()
    }

    override fun save() {
        update()
    }
}