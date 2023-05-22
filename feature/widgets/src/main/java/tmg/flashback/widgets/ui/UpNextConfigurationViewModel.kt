package tmg.flashback.widgets.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import tmg.flashback.ui.settings.Setting
import tmg.flashback.widgets.R
import tmg.flashback.strings.R.*
import tmg.flashback.widgets.repository.WidgetRepository
import tmg.flashback.widgets.usecases.UpdateWidgetsUseCase
import tmg.utilities.lifecycle.SingleLiveEvent
import javax.inject.Inject

interface UpNextConfigurationViewModelInputs {
    fun load(appWidgetId: Int)
    fun changeShowBackground(enabled: Boolean)
    fun save()
}

interface UpNextConfigurationViewModelOutputs {
    val showBackground: LiveData<Boolean>
    val save: SingleLiveEvent<Unit>
}

@HiltViewModel
internal class UpNextConfigurationViewModel @Inject constructor(
    private val widgetRepository: WidgetRepository,
    private val updateWidgetsUseCase: UpdateWidgetsUseCase
): ViewModel(), UpNextConfigurationViewModelInputs, UpNextConfigurationViewModelOutputs {

    val inputs: UpNextConfigurationViewModelInputs = this
    val outputs: UpNextConfigurationViewModelOutputs = this

    private var appWidgetId: Int = -1

    override val save: SingleLiveEvent<Unit> = SingleLiveEvent()
    override val showBackground: MutableLiveData<Boolean> = MutableLiveData(false)

    override fun load(appWidgetId: Int) {
        this.appWidgetId = appWidgetId
        showBackground.postValue(widgetRepository.getShowBackground(appWidgetId))
    }

    override fun changeShowBackground(enabled: Boolean) {
        widgetRepository.setShowBackground(appWidgetId, enabled)
        showBackground.postValue(enabled)
    }

    override fun save() {
        updateWidgetsUseCase.update()
        save.call()
    }
}