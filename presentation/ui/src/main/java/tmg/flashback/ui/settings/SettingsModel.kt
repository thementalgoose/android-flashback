package tmg.flashback.ui.settings

import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import tmg.flashback.ui.R

sealed class SettingsModel(
    @LayoutRes val layoutId: Int
) {

    data class Header(
        @StringRes
        val title: Int,
        val beta: Boolean = false
    ): SettingsModel(R.layout.view_settings_category)

    data class SwitchPref(
        @StringRes
        val title: Int,
        @StringRes
        val description: Int,
        val getState: () -> Boolean,
        val saveState: (value: Boolean) -> Unit,
        val saveStateNotification: ((value: Boolean) -> Unit)? = null,
        val beta: Boolean = false
    ): SettingsModel(R.layout.view_settings_preference_switch) {
        val initialState: Boolean = getState()
    }

    data class Pref(
        @StringRes
        val title: Int,
        @StringRes
        val description: Int,
        val onClick: (() -> Unit)?,
        val beta: Boolean = false
    ): SettingsModel(R.layout.view_settings_preference)
}