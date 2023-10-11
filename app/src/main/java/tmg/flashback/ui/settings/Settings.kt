package tmg.flashback.ui.settings

import tmg.flashback.R
import tmg.flashback.season.contract.repository.models.NotificationResultsAvailable
import tmg.flashback.season.contract.repository.models.NotificationReminder
import tmg.flashback.season.repository.models.prefKey
import tmg.flashback.ui.model.NightMode
import tmg.flashback.ui.settings.appearance.nightmode.title
import tmg.flashback.ui.settings.appearance.theme.title

object Settings {

    object Theme {

        val darkMode = Setting.Section(
            _key = "dark_mode",
            title = R.string.settings_section_dark_mode_title,
            subtitle = R.string.settings_section_dark_mode_description,
            icon = R.drawable.ic_settings_dark_mode
        )
        fun darkModeOption(type: NightMode, isChecked: Boolean) = Setting.Option(
            _key = type.key,
            title = type.title,
            subtitle = type.title,
            isChecked = isChecked
        )

        val theme = Setting.Section(
            _key = "theme",
            title = R.string.settings_section_theme_title,
            subtitle = R.string.settings_section_theme_description,
            icon = R.drawable.ic_settings_theme
        )
        fun themeOption(type: tmg.flashback.ui.model.Theme, isChecked: Boolean) = Setting.Option(
            _key = type.key,
            title = type.title,
            subtitle = type.title,
            isChecked = isChecked
        )
    }

    object Data {
        val layout = Setting.Section(
            _key = "home",
            title = R.string.settings_section_home_title,
            subtitle = R.string.settings_section_home_description,
            icon = R.drawable.ic_settings_home
        )

        const val collapseListKey = "collapse_list"
        fun collapseList(isChecked: Boolean, isEnabled: Boolean = true) = Setting.Switch(
            _key = collapseListKey,
            title = R.string.settings_pref_collapsed_list_title,
            subtitle = R.string.settings_pref_collapsed_list_description,
            isBeta = false,
            isChecked = isChecked,
            isEnabled = isEnabled
        )

        const val emptyWeeksInSchedule = "empty_weeks"
        fun showEmptyWeeksInSchedule(isChecked: Boolean, isEnabled: Boolean = true) = Setting.Switch(
            _key = emptyWeeksInSchedule,
            title = R.string.settings_pref_empty_week_title,
            subtitle = R.string.settings_pref_empty_week_description,
            isBeta = false,
            isChecked = isChecked,
            isEnabled = isEnabled
        )

        val weather = Setting.Section(
            _key = "weather",
            title = R.string.settings_section_weather_title,
            subtitle = R.string.settings_section_weather_description,
            icon = R.drawable.ic_settings_weather
        )
        const val temperatureUnits = "temperature_units"
        fun temperatureUnits(isChecked: Boolean) = Setting.Switch(
            _key = temperatureUnits,
            title = R.string.settings_pref_temperature_unit_title,
            subtitle = R.string.settings_pref_temperature_unit_description,
            isBeta = false,
            isChecked = isChecked,
            isEnabled = true
        )
        const val windSpeedUnits = "wind_speed_units"
        fun windSpeedUnits(isChecked: Boolean) = Setting.Switch(
            _key = windSpeedUnits,
            title = R.string.settings_pref_wind_speed_unit_title,
            subtitle = R.string.settings_pref_wind_speed_unit_description,
            isBeta = false,
            isChecked = isChecked,
            isEnabled = true
        )
    }

    object RSS {
        val rss = Setting.Section(
            _key = "rss",
            title = R.string.settings_section_rss_configure_title,
            subtitle = R.string.settings_section_rss_configure_description,
            icon = R.drawable.ic_settings_rss_configure
        )
    }

    object Web {
        val inAppBrowser = Setting.Section(
            _key = "in_app_browser",
            title = R.string.settings_section_web_browser_title,
            subtitle = R.string.settings_section_web_browser_description,
            icon = R.drawable.ic_settings_web
        )

        const val enable = "in_app_browser_enable"
        fun enable(isChecked: Boolean, isEnabled: Boolean = true) = Setting.Switch(
            _key = enable,
            title = R.string.settings_switch_enable_web_browser_title,
            subtitle = R.string.settings_switch_enable_web_browser_description,
            isChecked = isChecked,
            isEnabled = isEnabled
        )

        const val javascript = "enable_javascript"
        fun javascript(isChecked: Boolean, isEnabled: Boolean = true) = Setting.Switch(
            _key = javascript,
            title = R.string.settings_switch_enable_javascript_title,
            subtitle = R.string.settings_switch_enable_javascript_description,
            isChecked = isChecked,
            isEnabled = isEnabled
        )
    }
    object Notifications {
        val notificationUpcoming = Setting.Section(
            _key = "notification_upcoming",
            title = R.string.settings_section_notifications_upcoming_title,
            subtitle = R.string.settings_section_notifications_upcoming_description,
            icon = R.drawable.ic_settings_notifications_upcoming
        )

        val notificationResults = Setting.Section(
            _key = "notification_results",
            title = R.string.settings_section_notifications_results_title,
            subtitle = R.string.settings_section_notifications_results_description,
            icon = R.drawable.ic_settings_notifications_results
        )

        val notificationPermissionEnable = Setting.Pref(
            _key = "notification_permission",
            title = R.string.settings_pref_notification_permission_title,
            subtitle = R.string.settings_pref_notification_permission_description
        )

        val notificationExactAlarmEnable = Setting.Pref(
            _key = "notification_exact_alarm",
            title = R.string.settings_pref_schedule_exact_alarm_title,
            subtitle = R.string.settings_pref_schedule_exact_alarm_description
        )


        val notificationUpcomingNotice = Setting.Pref(
            _key = "notification_notice_period",
            title = R.string.settings_pref_notification_notice_period_title,
            subtitle = R.string.settings_pref_notification_notice_period_description,
            icon = 0
        )
        fun notificationNoticePeriod(reminder: NotificationReminder, isChecked: Boolean, isEnabled: Boolean) = Setting.Option(
            _key = reminder.name,
            title = reminder.label,
            subtitle = null,
            isChecked = isChecked,
            isEnabled = isEnabled
        )


        val NotificationResultsAvailable.title: Int
            get() = when (this) {
                NotificationResultsAvailable.RACE -> R.string.settings_switch_notification_results_race_title
                NotificationResultsAvailable.SPRINT -> R.string.settings_switch_notification_results_sprint_title
                NotificationResultsAvailable.SPRINT_QUALIFYING -> R.string.settings_switch_notification_results_sprint_qualifying_title
                NotificationResultsAvailable.QUALIFYING -> R.string.settings_switch_notification_results_qualifying_title
            }
        fun notificationResultsAvailable(available: NotificationResultsAvailable, isChecked: Boolean, isEnabled: Boolean = true) = Setting.Switch(
            _key = available.prefKey,
            title = available.title,
            subtitle = null,
            isChecked = isChecked,
            isEnabled = isEnabled
        )

    }
    object Ads {
        val ads = Setting.Section(
            _key = "ads",
            title = R.string.settings_section_ads_title,
            subtitle = R.string.settings_section_ads_description,
            icon = R.drawable.ic_settings_ads
        )
        const val enableAds = "ads_enable"
        fun enableAds(isChecked: Boolean, isEnabled: Boolean = true) = Setting.Switch(
            _key = enableAds,
            title = R.string.settings_switch_ads_enable_title,
            subtitle = R.string.settings_switch_ads_enable_description,
            isChecked = isChecked,
            isEnabled = isEnabled
        )
    }
    object Other {
        val privacy = Setting.Section(
            _key = "privacy",
            title = R.string.settings_section_privacy_title,
            subtitle = R.string.settings_section_privacy_description,
            icon = R.drawable.ic_settings_privacy
        )
        val privacyPolicy = Setting.Pref(
            _key = "privacy-policy",
            title = R.string.settings_pref_privacy_policy_title,
            subtitle = R.string.settings_pref_privacy_policy_description
        )
        const val crashReporting = "crash_reporting"
        fun crashReporting(isChecked: Boolean, isEnabled: Boolean = true) = Setting.Switch(
            _key = crashReporting,
            title = R.string.settings_pref_crash_reporting_title,
            subtitle = R.string.settings_pref_crash_reporting_description,
            isChecked = isChecked,
            isEnabled = isEnabled
        )
        const val analytics = "analytics"
        fun analytics(isChecked: Boolean, isEnabled: Boolean = true) = Setting.Switch(
            _key = "analytics",
            title = R.string.settings_pref_analytics_title,
            subtitle = R.string.settings_pref_analytics_description,
            isChecked = isChecked,
            isEnabled = isEnabled
        )


        val about = Setting.Section(
            _key = "about",
            title = R.string.settings_section_about_title,
            subtitle = R.string.settings_section_about_description,
            icon = R.drawable.ic_settings_about
        )
        val aboutThisApp = Setting.Pref(
            _key = "about_this_app",
            title = R.string.settings_switch_about_this_app_title,
            subtitle = R.string.settings_switch_about_this_app_description
        )
        val review = Setting.Pref(
            _key = "review",
            title = R.string.settings_switch_review_title,
            subtitle = R.string.settings_switch_review_description
        )
        const val shakeToReport = "shake_to_report"
        fun shakeToReport(isChecked: Boolean, isEnabled: Boolean = true) = Setting.Switch(
            _key = shakeToReport,
            title = R.string.settings_pref_shake_to_report_title,
            subtitle = R.string.settings_pref_shake_to_report_description,
            isChecked = isChecked,
            isEnabled = isEnabled
        )
    }
}