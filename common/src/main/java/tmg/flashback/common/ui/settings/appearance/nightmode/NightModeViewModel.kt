package tmg.flashback.common.ui.settings.appearance.nightmode

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tmg.flashback.ui.bottomsheet.BottomSheetItem
import tmg.flashback.ui.controllers.ThemeController
import tmg.flashback.ui.extensions.icon
import tmg.flashback.ui.extensions.label
import tmg.flashback.ui.model.NightMode
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
        private val themeController: ThemeController
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
        val same = themeController.nightMode == nightMode
        themeController.nightMode = nightMode
        updateThemeList()
        nightModeUpdated.value = DataEvent(Pair(nightMode, same))
    }

    //endregion

    private fun updateThemeList() {
        themePreferences.value = NightMode.values()
                .map {
                    Selected(BottomSheetItem(it.ordinal, it.icon, StringHolder(it.label)), it == themeController.nightMode)
                }
    }
}
