package tmg.flashback.settings.ui.settings.appearance.theme

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import tmg.flashback.ui.bottomsheet.BottomSheetItem
import tmg.flashback.ui.extensions.icon
import tmg.flashback.ui.extensions.label
import tmg.flashback.ui.model.Theme
import tmg.flashback.ui.repository.ThemeRepository
import tmg.utilities.lifecycle.DataEvent
import tmg.utilities.models.Selected
import tmg.utilities.models.StringHolder
import javax.inject.Inject

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

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val themeRepository: ThemeRepository
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
        val same = themeRepository.theme == theme
        themeRepository.theme = theme
        updateThemeList()
        themeUpdated.value = DataEvent(Pair(theme, same))
    }

    //endregion

    private fun updateThemeList() {
        themePreferences.value = Theme.values()
            .map {
                Selected(BottomSheetItem(it.ordinal, it.icon, StringHolder(it.label)), it == themeRepository.theme)
            }
    }
}
