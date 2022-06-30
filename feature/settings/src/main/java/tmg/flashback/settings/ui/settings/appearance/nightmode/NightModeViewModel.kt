package tmg.flashback.settings.ui.settings.appearance.nightmode

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tmg.flashback.ui.bottomsheet.BottomSheetItem
import tmg.flashback.ui.extensions.icon
import tmg.flashback.ui.extensions.label
import tmg.flashback.ui.model.NightMode
import tmg.flashback.ui.repository.ThemeRepository
import tmg.flashback.ui.usecases.ChangeNightModeUseCase
import tmg.utilities.lifecycle.DataEvent
import tmg.utilities.models.Selected
import tmg.utilities.models.StringHolder

//region Inputs

interface NightModeViewModelInputs {
    fun selectNightMode(nightMode: NightMode)
}

//endregion

//region Outputs

interface NightModeViewModelOutputs {

    val themePreferences: LiveData<List<Selected<BottomSheetItem>>>
    val nightModeUpdated: LiveData<DataEvent<Pair<NightMode, Boolean>>>
}

//endregion

class NightMoveViewModel(
        private val themeRepository: ThemeRepository,
        private val changeNightModeUseCase: ChangeNightModeUseCase
): ViewModel(), NightModeViewModelInputs, NightModeViewModelOutputs {

    var inputs: NightModeViewModelInputs = this
    var outputs: NightModeViewModelOutputs = this

    override val themePreferences: MutableLiveData<List<Selected<BottomSheetItem>>> = MutableLiveData()
    override val nightModeUpdated: MutableLiveData<DataEvent<Pair<NightMode, Boolean>>> = MutableLiveData()

    init {

        updateThemeList()
    }

    //region Inputs

    override fun selectNightMode(nightMode: NightMode) {
        val same = themeRepository.nightMode == nightMode
        changeNightModeUseCase.setNightMode(nightMode)
        updateThemeList()
        nightModeUpdated.value = DataEvent(Pair(nightMode, same))
    }

    //endregion

    private fun updateThemeList() {
        themePreferences.value = NightMode.values()
                .map {
                    Selected(BottomSheetItem(it.ordinal, it.icon, StringHolder(it.label)), it == themeRepository.nightMode)
                }
    }
}
