package tmg.common.testutils

import androidx.annotation.StringRes
import tmg.core.ui.settings.SettingsModel

fun List<SettingsModel>.findSwitch(@StringRes title: Int): SettingsModel.SwitchPref {
    return this
            .first {
                when (it) {
                    is SettingsModel.SwitchPref -> it.title == title
                    else -> false
                }
            } as SettingsModel.SwitchPref
}

fun List<SettingsModel>.findPref(@StringRes title: Int): SettingsModel.Pref {
    return this
            .first {
                when (it) {
                    is SettingsModel.Pref -> it.title == title
                    else -> false
                }
            } as SettingsModel.Pref
}