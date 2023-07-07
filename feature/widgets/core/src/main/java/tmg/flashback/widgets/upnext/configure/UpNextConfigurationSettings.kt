package tmg.flashback.widgets.upnext.configure

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
        isBeta = false
    )


    fun clickToEvent(isChecked: Boolean) = Setting.Switch(
        _key = "click_to_event",
        title = R.string.widget_settings_click_to_event_title,
        subtitle = R.string.widget_settings_click_to_event_description,
        isChecked = isChecked,
        isBeta = false
    )

    fun showWeather(show: Boolean) = Setting.Switch(
        _key = "show_weather",
        title = R.string.widget_settings_weather_title,
        subtitle = R.string.widget_settings_weather_description,
        isChecked = show,
        isBeta = true
    )
}