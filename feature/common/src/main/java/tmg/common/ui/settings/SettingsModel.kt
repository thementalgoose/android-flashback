package tmg.common.ui.settings

import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import tmg.common.R

sealed class SettingsModel(
    @LayoutRes val layoutId: Int
) {

    data class Header(
        @StringRes
        val title: Int
    ): SettingsModel(R.layout.view_settings_category)

    data class SwitchPref(
        @StringRes
        val title: Int,
        @StringRes
        val description: Int,
        val getState: ViewModel.() -> Boolean,
        val saveState: ViewModel.(value: Boolean) -> Unit
    ): SettingsModel(R.layout.view_settings_preference_switch)

    data class Pref(
        @StringRes
        val title: Int,
        @StringRes
        val description: Int,
        val onClick: (ViewModel.() -> Unit)?
    ): SettingsModel(R.layout.view_settings_preference)
}