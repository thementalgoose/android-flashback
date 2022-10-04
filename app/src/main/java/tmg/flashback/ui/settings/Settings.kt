package tmg.flashback.ui.settings

import tmg.flashback.R

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

    object Layout {
        val home = Setting.Section(
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
            isBeta = true,
            isChecked = isChecked,
            isEnabled = isEnabled
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

        val notificationUpcomingFreePracticeKey = "notification_upcoming_fp"
        fun notificationUpcomingFreePractice(isChecked: Boolean, isEnabled: Boolean = true) = Setting.Switch(
            _key = notificationUpcomingFreePracticeKey,
            title = R.string.settings_switch_notification_upcoming_fp_title,
            subtitle = null,
            isChecked = isChecked,
            isEnabled = isEnabled
        )
        val notificationUpcomingSprintKey = "notification_upcoming_sprint"
        fun notificationUpcomingSprint(isChecked: Boolean, isEnabled: Boolean = true) = Setting.Switch(
            _key = notificationUpcomingSprintKey,
            title = R.string.settings_switch_notification_upcoming_sprint_title,
            subtitle = null,
            isChecked = isChecked,
            isEnabled = isEnabled
        )
        val notificationUpcomingQualifyingKey = "notification_upcoming_qualifying"
        fun notificationUpcomingQualifying(isChecked: Boolean, isEnabled: Boolean = true) = Setting.Switch(
            _key = notificationUpcomingQualifyingKey,
            title = R.string.settings_switch_notification_upcoming_qualifying_title,
            subtitle = null,
            isChecked = isChecked,
            isEnabled = isEnabled
        )
        val notificationUpcomingOtherKey = "notification_upcoming_other"
        fun notificationUpcomingOther(isChecked: Boolean, isEnabled: Boolean = true) = Setting.Switch(
            _key = notificationUpcomingOtherKey,
            title = R.string.settings_switch_notification_upcoming_other_title,
            subtitle = null,
            isChecked = isChecked,
            isEnabled = isEnabled
        )
        val notificationUpcomingRaceKey = "notification_upcoming_race"
        fun notificationUpcomingRace(isChecked: Boolean, isEnabled: Boolean = true) = Setting.Switch(
            _key = notificationUpcomingRaceKey,
            title = R.string.settings_switch_notification_upcoming_race_title,
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


        val notificationResultsQualifyingKey = "notification_results_qualifying"
        fun notificationResultsQualifying(isChecked: Boolean, isEnabled: Boolean = true) = Setting.Switch(
            _key = notificationResultsQualifyingKey,
            title = R.string.settings_switch_notification_results_qualifying_title,
            subtitle = null,
            isChecked = isChecked,
            isEnabled = isEnabled
        )
        val notificationResultsSprintKey = "notification_results_sprint"
        fun notificationResultsSprint(isChecked: Boolean, isEnabled: Boolean = true) = Setting.Switch(
            _key = notificationResultsSprintKey,
            title = R.string.settings_switch_notification_results_sprint_title,
            subtitle = null,
            isChecked = isChecked,
            isEnabled = isEnabled
        )
        val notificationResultsRaceKey = "notification_results_race"
        fun notificationResultsRace(isChecked: Boolean, isEnabled: Boolean = true) = Setting.Switch(
            _key = notificationResultsRaceKey,
            title = R.string.settings_switch_notification_results_race_title,
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
        val releaseNotes = Setting.Pref(
            _key = "release_notes",
            title = R.string.settings_switch_release_notes_title,
            subtitle = R.string.settings_switch_release_notes_description
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