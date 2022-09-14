package tmg.flashback.ui.settings

import tmg.flashback.R

object AppSettings {

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

        fun menuAllExpanded(isChecked: Boolean) = Setting.Switch(
            _key = "menu_all_expanded",
            title = R.string.settings_pref_menu_all_title,
            subtitle = R.string.settings_pref_menu_all_description,
            isChecked = isChecked
        )

        fun menuFavouriteExpanded(isChecked: Boolean) = Setting.Switch(
            _key = "menu_favourite_expanded",
            title = R.string.settings_pref_menu_favourites_title,
            subtitle = R.string.settings_pref_menu_favourites_description,
            isChecked = isChecked
        )

        fun providedByAtTop(isChecked: Boolean) = Setting.Switch(
            _key = "provided_by_at_top",
            title = R.string.settings_pref_provided_by_at_top_title,
            subtitle = R.string.settings_pref_provided_by_at_top_description,
            isChecked = isChecked
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

        val enable = Setting.Pref(
            _key = "in_app_browser_enable",
            title = R.string.settings_switch_enable_web_browser_title,
            subtitle = R.string.settings_switch_enable_web_browser_description
        )

        val javascript = Setting.Pref(
            _key = "enable_javascript",
            title = R.string.settings_switch_enable_javascript_title,
            subtitle = R.string.settings_switch_enable_javascript_description
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

        fun notificationUpcomingFreePractice(isChecked: Boolean) = Setting.Switch(
            _key = "notification_upcoming_fp",
            title = R.string.settings_switch_notification_upcoming_fp_title,
            subtitle = null,
            isChecked = isChecked
        )
        fun notificationUpcomingQualifying(isChecked: Boolean) = Setting.Switch(
            _key = "notification_upcoming_qualifying",
            title = R.string.settings_switch_notification_upcoming_qualifying_title,
            subtitle = null,
            isChecked = isChecked
        )
        fun notificationUpcomingSprint(isChecked: Boolean) = Setting.Switch(
            _key = "notification_upcoming_sprint",
            title = R.string.settings_switch_notification_upcoming_sprint_title,
            subtitle = null,
            isChecked = isChecked
        )
        fun notificationUpcomingRace(isChecked: Boolean) = Setting.Switch(
            _key = "notification_upcoming_race",
            title = R.string.settings_switch_notification_upcoming_race_title,
            subtitle = null,
            isChecked = isChecked
        )

        val notificationNoticePeriod = Setting.Pref(
            _key = "notification_notice_period",
            title = R.string.settings_pref_notification_notice_period_title,
            subtitle = R.string.settings_pref_notification_notice_period_description
        )


        fun notificationResultsQualifying(isChecked: Boolean) = Setting.Switch(
            _key = "notification_results_qualifying",
            title = R.string.settings_switch_notification_results_qualifying_title,
            subtitle = null,
            isChecked = isChecked
        )
        fun notificationResultsSprint(isChecked: Boolean) = Setting.Switch(
            _key = "notification_results_sprint",
            title = R.string.settings_switch_notification_results_sprint_title,
            subtitle = null,
            isChecked = isChecked
        )
        fun notificationResultsRace(isChecked: Boolean) = Setting.Switch(
            _key = "notification_results_race",
            title = R.string.settings_switch_notification_results_race_title,
            subtitle = null,
            isChecked = isChecked
        )

    }
    object Ads {
        val ads = Setting.Section(
            _key = "ads",
            title = R.string.settings_section_ads_title,
            subtitle = R.string.settings_section_ads_description,
            icon = R.drawable.ic_settings_ads
        )
        fun enableAds(isChecked: Boolean) = Setting.Switch(
            _key = "ads_enable",
            title = R.string.settings_switch_ads_enable_title,
            subtitle = R.string.settings_switch_ads_enable_title,
            isChecked = isChecked
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
        val crashReporting = Setting.Pref(
            _key = "crash_reporting",
            title = R.string.settings_pref_crash_reporting_title,
            subtitle = R.string.settings_pref_crash_reporting_description
        )
        val analytics = Setting.Pref(
            _key = "analytics",
            title = R.string.settings_pref_analytics_title,
            subtitle = R.string.settings_pref_analytics_description
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
        fun shakeToReport(isChecked: Boolean) = Setting.Switch(
            _key = "shake_to_report",
            title = R.string.settings_pref_shake_to_report_title,
            subtitle = R.string.settings_pref_shake_to_report_description,
            isChecked = isChecked
        )
    }
}