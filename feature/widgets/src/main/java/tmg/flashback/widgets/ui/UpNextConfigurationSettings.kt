package tmg.flashback.widgets.ui

import tmg.flashback.strings.R
import tmg.flashback.ui.settings.Setting

object UpNextConfigurationSettings {

    val header = Setting.Heading(
        _key = "up_next_header",
        title = R.string.widget_configure
    )

    fun showBackground(isChecked: Boolean) = Setting.Switch(
        _key = "show_background",
        title = R.string.widget_settings_background_title,
        subtitle = R.string.widget_settings_background_description,
        isChecked = isChecked,
        isBeta = true
    )
}