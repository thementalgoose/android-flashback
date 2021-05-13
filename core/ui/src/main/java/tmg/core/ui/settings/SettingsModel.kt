package tmg.core.ui.settings

import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import tmg.core.ui.R

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
        val getState: () -> Boolean,
        val saveState: (value: Boolean) -> Unit,
        val saveStateNotification: ((value: Boolean) -> Unit)? = null
    ): SettingsModel(R.layout.view_settings_preference_switch)

    data class Pref(
        @StringRes
        val title: Int,
        @StringRes
        val description: Int,
        val onClick: (() -> Unit)?
    ): SettingsModel(R.layout.view_settings_preference)
}