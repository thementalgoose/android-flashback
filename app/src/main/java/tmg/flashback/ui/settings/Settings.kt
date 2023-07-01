package tmg.flashback.ui.settings

import tmg.flashback.R
import tmg.flashback.results.contract.repository.models.NotificationResultsAvailable
import tmg.flashback.results.contract.repository.models.NotificationUpcoming
import tmg.flashback.results.repository.models.prefKey

object Settings {

    object Theme {

        val darkMode = Setting.Section(
            _key = "dark_mode",
            title = R.string.settings_section_dark_mode_title,
            subtitle = R.string.settings_section_dark_mode_description,
            icon = R.drawable.ic_settings_dark_mode
        )

        val theme = Setting.Section(
            _key = "theme",
            title = R.string.settings_section_theme_title,
            subtitle = R.string.settings_section_theme_description,
            icon = R.drawable.ic_settings_theme
        )
    }

    object Data {
        val layout = Setting.Section(
            _key = "home",
            title = R.string.settings_section_home_title,
            subtitle = R.string.settings_section_home_description,
            icon = R.drawable.ic_settings_home
        )

        val collapseListKey = "collapse_list"
        fun collapseList(isChecked: Boolean, isEnabled: Boolean = true) = Setting.Switch(
            _key = collapseListKey,
            title = R.string.settings_pref_collapsed_list_title,
            subtitle = R.string.settings_pref_collapsed_list_description,
            isBeta = false,
            isChecked = isChecked,
            isEnabled = isEnabled
        )

        val emptyWeeksInSchedule = "empty_weeks"
        fun showEmptyWeeksInSchedule(isChecked: Boolean, isEnabled: Boolean = true) = Setting.Switch(
            _key = emptyWeeksInSchedule,
            title = R.string.settings_pref_empty_week_title,
            subtitle = R.string.settings_pref_empty_week_description,
            isBeta = true,
            isChecked = isChecked,
            isEnabled = isEnabled
        )

        val weather = Setting.Section(
            _key = "weather",
            title = R.string.settings_section_weather_title,
            subtitle = R.string.settings_section_weather_description,
            icon = R.drawable.ic_settings_weather
        )
        val temperatureUnitsKey = "temperature_units"
        fun temperatureUnits(isChecked: Boolean) = Setting.Switch(
            _key = temperatureUnitsKey,
            title = R.string.settings_pref_temperature_unit_title,
            subtitle = R.string.settings_pref_temperature_unit_description,
            isBeta = false,
            isChecked = isChecked,
            isEnabled = true
        )
        val windSpeedUnitsKey = "wind_speed_units"
        fun windSpeedUnits(isChecked: Boolean) = Setting.Switch(
            _key = windSpeedUnitsKey,
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

        val enableKey = "in_app_browser_enable"
        fun enable(isChecked: Boolean, isEnabled: Boolean = true) = Setting.Switch(
            _key = enableKey,
            title = R.string.settings_switch_enable_web_browser_title,
            subtitle = R.string.settings_switch_enable_web_browser_description,
            isChecked = isChecked,
            isEnabled = isEnabled
        )

        val javascriptKey = "enable_javascript"
        fun javascript(isChecked: Boolean, isEnabled: Boolean = true) = Setting.Switch(
            _key = javascriptKey,
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

        val NotificationUpcoming.title: Int
            get() = when (this) {
                NotificationUpcoming.RACE -> R.string.settings_switch_notification_upcoming_race_title
                NotificationUpcoming.SPRINT -> R.string.settings_switch_notification_upcoming_sprint_title
                NotificationUpcoming.QUALIFYING -> R.string.settings_switch_notification_upcoming_qualifying_title
                NotificationUpcoming.FREE_PRACTICE -> R.string.settings_switch_notification_upcoming_fp_title
                NotificationUpcoming.OTHER -> R.string.settings_switch_notification_upcoming_other_title
            }
        fun notificationUpcoming(value: NotificationUpcoming, isChecked: Boolean, isEnabled: Boolean = true) = Setting.Switch(
            _key = value.prefKey,
            title = value.title,
            subtitle = null,
            isChecked = isChecked,
            isEnabled = isEnabled
        )


        val notificationNoticePeriodKey = "notification_notice_period"
        fun notificationNoticePeriod(isEnabled: Boolean = true) = Setting.Pref(
            _key = notificationNoticePeriodKey,
            title = R.string.settings_pref_notification_notice_period_title,
            subtitle = R.string.settings_pref_notification_notice_period_description,
            isEnabled = isEnabled
        )


        val NotificationResultsAvailable.title: Int
            get() = when (this) {
                NotificationResultsAvailable.RACE -> R.string.settings_switch_notification_results_race_title
                NotificationResultsAvailable.SPRINT -> R.string.settings_switch_notification_results_sprint_title
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
        val enableAdsKey = "ads_enable"
        fun enableAds(isChecked: Boolean, isEnabled: Boolean = true) = Setting.Switch(
            _key = enableAdsKey,
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
        val crashReportingKey = "crash_reporting"
        fun crashReporting(isChecked: Boolean, isEnabled: Boolean = true) = Setting.Switch(
            _key = crashReportingKey,
            title = R.string.settings_pref_crash_reporting_title,
            subtitle = R.string.settings_pref_crash_reporting_description,
            isChecked = isChecked,
            isEnabled = isEnabled
        )
        val analyticsKey = "analytics"
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
        val shakeToReportKey = "shake_to_report"
        fun shakeToReport(isChecked: Boolean, isEnabled: Boolean = true) = Setting.Switch(
            _key = shakeToReportKey,
            title = R.string.settings_pref_shake_to_report_title,
            subtitle = R.string.settings_pref_shake_to_report_description,
            isChecked = isChecked,
            isEnabled = isEnabled
        )
    }
}