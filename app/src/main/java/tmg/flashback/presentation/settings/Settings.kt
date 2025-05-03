package tmg.flashback.presentation.settings

import tmg.flashback.R
import tmg.flashback.strings.R.string
import tmg.flashback.formula1.model.notifications.NotificationResultsAvailable
import tmg.flashback.formula1.model.notifications.NotificationReminder
import tmg.flashback.season.repository.models.prefKey
import tmg.flashback.ui.model.NightMode
import tmg.flashback.presentation.settings.appearance.nightmode.title
import tmg.flashback.presentation.settings.appearance.theme.title
import tmg.flashback.ui.settings.Setting

object Settings {

    object Theme {

        val darkMode = Setting.Section(
            _key = "dark_mode",
            title = string.settings_section_dark_mode_title,
            subtitle = string.settings_section_dark_mode_description,
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
            title = string.settings_section_theme_title,
            subtitle = string.settings_section_theme_description,
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
            title = string.settings_section_home_title,
            subtitle = string.settings_section_home_description,
            icon = R.drawable.ic_settings_home
        )

        const val collapseListKey = "collapse_list"
        fun collapseList(isChecked: Boolean, isEnabled: Boolean = true) = Setting.Switch(
            _key = collapseListKey,
            title = string.settings_pref_collapsed_list_title,
            subtitle = string.settings_pref_collapsed_list_description,
            isBeta = false,
            isChecked = isChecked,
            isEnabled = isEnabled
        )

        const val emptyWeeksInSchedule = "empty_weeks"
        fun showEmptyWeeksInSchedule(isChecked: Boolean, isEnabled: Boolean = true) = Setting.Switch(
            _key = emptyWeeksInSchedule,
            title = string.settings_pref_empty_week_title,
            subtitle = string.settings_pref_empty_week_description,
            isBeta = false,
            isChecked = isChecked,
            isEnabled = isEnabled
        )

        const val recentHighlights = "recent_highlights"
        fun showRecentHighlights(isChecked: Boolean, isEnabled: Boolean = true) = Setting.Switch(
            _key = recentHighlights,
            title = string.settings_pref_recent_highlights_title,
            subtitle = string.settings_pref_recent_highlights_description,
            isBeta = false,
            isChecked = isChecked,
            isEnabled = isEnabled
        )

        val weather = Setting.Section(
            _key = "weather",
            title = string.settings_section_weather_title,
            subtitle = string.settings_section_weather_description,
            icon = R.drawable.ic_settings_weather
        )
        const val temperatureUnits = "temperature_units"
        fun temperatureUnits(isChecked: Boolean) = Setting.Switch(
            _key = temperatureUnits,
            title = string.settings_pref_temperature_unit_title,
            subtitle = string.settings_pref_temperature_unit_description,
            isBeta = false,
            isChecked = isChecked,
            isEnabled = true
        )
        const val windSpeedUnits = "wind_speed_units"
        fun windSpeedUnits(isChecked: Boolean) = Setting.Switch(
            _key = windSpeedUnits,
            title = string.settings_pref_wind_speed_unit_title,
            subtitle = string.settings_pref_wind_speed_unit_description,
            isBeta = false,
            isChecked = isChecked,
            isEnabled = true
        )
    }

    object RSS {
        val rss = Setting.Section(
            _key = "rss",
            title = string.settings_section_rss_configure_title,
            subtitle = string.settings_section_rss_configure_description,
            icon = R.drawable.ic_settings_rss_configure
        )
    }

    object Web {
        val inAppBrowser = Setting.Section(
            _key = "in_app_browser",
            title = string.settings_section_web_browser_title,
            subtitle = string.settings_section_web_browser_description,
            icon = R.drawable.ic_settings_web
        )

        const val enable = "in_app_browser_enable"
        fun enable(isChecked: Boolean, isEnabled: Boolean = true) = Setting.Switch(
            _key = enable,
            title = string.settings_switch_enable_web_browser_title,
            subtitle = string.settings_switch_enable_web_browser_description,
            isChecked = isChecked,
            isEnabled = isEnabled
        )

        const val javascript = "enable_javascript"
        fun javascript(isChecked: Boolean, isEnabled: Boolean = true) = Setting.Switch(
            _key = javascript,
            title = string.settings_switch_enable_javascript_title,
            subtitle = string.settings_switch_enable_javascript_description,
            isChecked = isChecked,
            isEnabled = isEnabled
        )
    }

    object Notifications {
        val notificationUpcoming = Setting.Section(
            _key = "notification_upcoming",
            title = string.settings_section_notifications_upcoming_title,
            subtitle = string.settings_section_notifications_upcoming_description,
            icon = R.drawable.ic_settings_notifications_upcoming
        )

        val notificationResults = Setting.Section(
            _key = "notification_results",
            title = string.settings_section_notifications_results_title,
            subtitle = string.settings_section_notifications_results_description,
            icon = R.drawable.ic_settings_notifications_results
        )

        val notificationPermissionEnable = Setting.Pref(
            _key = "notification_permission",
            title = string.settings_pref_notification_permission_title,
            subtitle = string.settings_pref_notification_permission_description
        )

        val notificationExactAlarmEnable = Setting.Pref(
            _key = "notification_exact_alarm",
            title = string.settings_pref_schedule_exact_alarm_title,
            subtitle = string.settings_pref_schedule_exact_alarm_description
        )


        val notificationUpcomingNotice = Setting.Pref(
            _key = "notification_notice_period",
            title = string.settings_pref_notification_notice_period_title,
            subtitle = string.settings_pref_notification_notice_period_description,
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
                NotificationResultsAvailable.RACE -> string.settings_switch_notification_results_race_title
                NotificationResultsAvailable.SPRINT -> string.settings_switch_notification_results_sprint_title
                NotificationResultsAvailable.SPRINT_QUALIFYING -> string.settings_switch_notification_results_sprint_qualifying_title
                NotificationResultsAvailable.QUALIFYING -> string.settings_switch_notification_results_qualifying_title
            }
        fun notificationResultsAvailable(available: NotificationResultsAvailable, isChecked: Boolean, isEnabled: Boolean = true) = Setting.Switch(
            _key = available.prefKey,
            title = available.title,
            subtitle = null,
            isChecked = isChecked,
            isEnabled = isEnabled
        )
    }

    object Widgets {
        val widgets = Setting.Section(
            _key = "widgets",
            title = string.settings_section_widgets_title,
            subtitle = string.settings_section_widgets_description,
            icon = R.drawable.ic_settings_widgets
        )
        const val showBackground = "widgets_show_background"
        fun showBackground(isChecked: Boolean, isEnabled: Boolean = true) = Setting.Switch(
            _key = showBackground,
            title = string.settings_section_widgets_show_background_title,
            subtitle = string.settings_section_widgets_show_background_description,
            isChecked = isChecked,
            isEnabled = isEnabled
        )
        const val deeplinkToEvent = "widgets_deeplink_to_event"
        fun deeplinkToEvent(isChecked: Boolean, isEnabled: Boolean = true) = Setting.Switch(
            _key = deeplinkToEvent,
            title = string.settings_section_widgets_deeplink_event_title,
            subtitle = string.settings_section_widgets_deeplink_event_description,
            isChecked = isChecked,
            isEnabled = isEnabled,
            isBeta = true
        )

        val refreshWidgets = Setting.Pref(
            _key = "refresh",
            title = string.settings_section_refresh_widget_title,
            subtitle = string.settings_section_refresh_widget_description,
        )
    }

    object Ads {
        val ads = Setting.Section(
            _key = "ads",
            title = string.settings_section_ads_title,
            subtitle = string.settings_section_ads_description,
            icon = R.drawable.ic_settings_ads
        )
        const val enableAds = "ads_enable"
        fun enableAds(isChecked: Boolean, isEnabled: Boolean = true) = Setting.Switch(
            _key = enableAds,
            title = string.settings_switch_ads_enable_title,
            subtitle = string.settings_switch_ads_enable_description,
            isChecked = isChecked,
            isEnabled = isEnabled
        )
    }
    object Other {
        val privacy = Setting.Section(
            _key = "privacy",
            title = string.settings_section_privacy_title,
            subtitle = string.settings_section_privacy_description,
            icon = R.drawable.ic_settings_privacy
        )
        val privacyPolicy = Setting.Pref(
            _key = "privacy-policy",
            title = string.settings_pref_privacy_policy_title,
            subtitle = string.settings_pref_privacy_policy_description
        )
        const val crashReporting = "crash_reporting"
        fun crashReporting(isChecked: Boolean, isEnabled: Boolean = true) = Setting.Switch(
            _key = crashReporting,
            title = string.settings_pref_crash_reporting_title,
            subtitle = string.settings_pref_crash_reporting_description,
            isChecked = isChecked,
            isEnabled = isEnabled
        )
        const val analytics = "analytics"
        fun analytics(isChecked: Boolean, isEnabled: Boolean = true) = Setting.Switch(
            _key = "analytics",
            title = string.settings_pref_analytics_title,
            subtitle = string.settings_pref_analytics_description,
            isChecked = isChecked,
            isEnabled = isEnabled
        )


        val about = Setting.Section(
            _key = "about",
            title = string.settings_section_about_title,
            subtitle = string.settings_section_about_description,
            icon = R.drawable.ic_settings_about
        )
        val aboutThisApp = Setting.Pref(
            _key = "about_this_app",
            title = string.settings_switch_about_this_app_title,
            subtitle = string.settings_switch_about_this_app_description
        )
        val review = Setting.Pref(
            _key = "review",
            title = string.settings_switch_review_title,
            subtitle = string.settings_switch_review_description
        )
        val buildVersion = Setting.Pref(
            _key = "build_version",
            title = string.settings_build_version,
            subtitle = R.string.app_version_name
        )
        const val shakeToReport = "shake_to_report"
        fun shakeToReport(isChecked: Boolean, isEnabled: Boolean = true) = Setting.Switch(
            _key = shakeToReport,
            title = string.settings_pref_shake_to_report_title,
            subtitle = string.settings_pref_shake_to_report_description,
            isChecked = isChecked,
            isEnabled = isEnabled
        )


        val resetFirstTimeSync = Setting.Pref(
            _key = "first_time_sync",
            title = string.settings_pref_reset_title,
            subtitle = string.settings_pref_reset_description
        )
    }
}