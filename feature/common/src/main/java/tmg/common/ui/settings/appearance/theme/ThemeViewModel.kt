package tmg.common.ui.settings.appearance.theme

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tmg.core.ui.bottomsheet.BottomSheetItem
import tmg.core.ui.controllers.ThemeController
import tmg.core.ui.extensions.icon
import tmg.core.ui.extensions.label
import tmg.core.ui.model.NightMode
import tmg.core.ui.model.Theme
import tmg.utilities.lifecycle.DataEvent
import tmg.utilities.models.Selected
import tmg.utilities.models.StringHolder

//region Inputs

interface ThemeViewModelInputs {
    fun selectTheme(theme: Theme)
}

//endregion

//region Outputs

interface ThemeViewModelOutputs {
    val themePreferences: LiveData<List<Selected<BottomSheetItem>>>
    val themeUpdated: LiveData<DataEvent<Pair<Theme, Boolean>>>
}

//endregion


class ThemeViewModel(
    private val themeController: ThemeController
): ViewModel(), ThemeViewModelInputs, ThemeViewModelOutputs {

    var inputs: ThemeViewModelInputs = this
    var outputs: ThemeViewModelOutputs = this

    override val themePreferences: MutableLiveData<List<Selected<BottomSheetItem>>> = MutableLiveData()
    override val themeUpdated: MutableLiveData<DataEvent<Pair<Theme, Boolean>>> = MutableLiveData()

    init {
        updateThemeList()
    }

    //region Inputs

    override fun selectTheme(theme: Theme) {
        val same = themeController.theme == theme
        themeController.theme = theme
        updateThemeList()
        themeUpdated.value = DataEvent(Pair(theme, same))
    }

    //endregion

    private fun updateThemeList() {
        themePreferences.value = Theme.values()
            .map {
                Selected(BottomSheetItem(it.ordinal, it.icon, StringHolder(it.label)), it == themeController.theme)
            }
    }
}
