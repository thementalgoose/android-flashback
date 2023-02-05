package tmg.flashback.ui.settings.appearance.nightmode

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import tmg.flashback.ui.model.NightMode
import tmg.flashback.ui.repository.ThemeRepository
import tmg.flashback.ui.usecases.ChangeNightModeUseCase
import javax.inject.Inject

//region Inputs

interface NightModeViewModelInputs {
    fun selectNightMode(nightMode: NightMode)
}

//endregion

//region Outputs

interface NightModeViewModelOutputs {
    val currentlySelected: LiveData<NightMode>
}

//endregion

@HiltViewModel
class NightModeViewModel @Inject constructor(
    private val themeRepository: ThemeRepository,
    private val changeNightModeUseCase: ChangeNightModeUseCase
): ViewModel(), NightModeViewModelInputs, NightModeViewModelOutputs {

    var inputs: NightModeViewModelInputs = this
    var outputs: NightModeViewModelOutputs = this

    override val currentlySelected: MutableLiveData<NightMode> = MutableLiveData()

    init {
        currentlySelected.value = themeRepository.nightMode
    }

    //region Inputs

    override fun selectNightMode(nightMode: NightMode) {
        changeNightModeUseCase.setNightMode(nightMode)
    }

    //endregion
}
