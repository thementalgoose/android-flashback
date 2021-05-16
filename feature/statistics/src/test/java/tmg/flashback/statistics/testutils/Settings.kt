package tmg.flashback.statistics.testutils

import androidx.annotation.StringRes
import org.junit.jupiter.api.Assertions
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

fun List<SettingsModel>.assertExpectedOrder(expected: List<Pair<Int, Int?>>) {
    this.forEachIndexed { index, settingsModel ->
        if (settingsModel is SettingsModel.Header) {
            Assertions.assertEquals(expected[index].first, settingsModel.title)
        }
        if (settingsModel is SettingsModel.SwitchPref) {
            Assertions.assertEquals(expected[index].first, settingsModel.title)
            Assertions.assertEquals(expected[index].second, settingsModel.description)
        }
        if (settingsModel is SettingsModel.Pref) {
            Assertions.assertEquals(expected[index].first, settingsModel.title)
            Assertions.assertEquals(expected[index].second, settingsModel.description)
        }
    }
}