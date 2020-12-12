package tmg.flashback.settings

import androidx.annotation.StringRes
import tmg.components.prefs.AppPreferencesItem
import tmg.flashback.R

enum class SettingsOptions(
        val key: String,
        @StringRes val title: Int,
        @StringRes val description: Int
) {
    NEWS("news",
            title = R.string.settings_customisation_rss_title,
            description = R.string.settings_customisation_rss_description
    ),
    NOTIFICATIONS_CHANNEL_RACE("notificationRace",
        title = R.string.settings_notifications_channel_race_title,
        description = R.string.settings_notifications_channel_race_description
    ),
    NOTIFICATIONS_CHANNEL_QUALIFYING("notificationQualifying",
        title = R.string.settings_notifications_channel_qualifying_title,
        description = R.string.settings_notifications_channel_qualifying_description
    ),
    NOTIFICATIONS_SETTINGS("notificationSetting",
        title = R.string.settings_notifications_nonchannel_title,
        description = R.string.settings_notifications_nonchannel_description
    ),
    THEME("theme",
            title = R.string.settings_theme_theme_title,
            description = R.string.settings_theme_theme_description
    ),
    QUALIFYING_DELTAS("qualifyingDeltas",
            title = R.string.settings_customisation_qualifying_delta_title,
            description = R.string.settings_customisation_qualifying_delta_description
    ),
    QUALIFYING_GRID_PENALTY("qualifyingGridPenalty",
            title = R.string.settings_customisation_qualifying_grid_penalties_title,
            description = R.string.settings_customisation_qualifying_grid_penalties_description
    ),
    BAR_ANIMATION_SPEED("barAnimationSpeed",
            title = R.string.settings_bar_animation_animation_title,
            description = R.string.settings_bar_animation_animation_description
    ),
    SEASON_BOTTOM_SHEET_EXPANDED("bottomSheetExpanded",
            title = R.string.settings_customisation_season_expanded_title,
            description = R.string.settings_customisation_season_expanded_description
    ),
    SEASON_BOTTOM_SHEET_FAVOURITED("bottomSheetFavourited",
            title = R.string.settings_customisation_season_favourited_expanded_title,
            description = R.string.settings_customisation_season_favourited_expanded_description
    ),
    SEASON_BOTTOM_SHEET_ALL("bottomSheetAll",
            title = R.string.settings_customisation_season_all_expanded_title,
            description = R.string.settings_customisation_season_all_expanded_description
    ),
    ABOUT("about",
            title = R.string.settings_help_about_title,
            description = R.string.settings_help_about_description
    ),
    REVIEW("review",
            title = R.string.settings_help_review_title,
            description = R.string.settings_help_review_description
    ),
    PRIVACY_POLICY("privacy_policy",
            title = R.string.settings_help_privacy_policy_title,
            description = R.string.settings_help_privacy_policy_description
    ),
    RELEASE("release",
            title = R.string.settings_help_release_notes_title,
            description = R.string.settings_help_release_notes_description
    ),
    CRASH("crash",
            title = R.string.settings_help_crash_reporting_title,
            description = R.string.settings_help_crash_reporting_description
    ),
    SUGGESTION("suggestions",
            title = R.string.settings_help_suggestions_title,
            description = R.string.settings_help_suggestions_description
    ),
    SHAKE("shake",
            title = R.string.settings_help_shake_to_report_title,
            description = R.string.settings_help_shake_to_report_description
    );

    fun toPref(): AppPreferencesItem.Preference {
        return AppPreferencesItem.Preference(key, title, description)
    }

    fun toSwitch(isChecked: Boolean): AppPreferencesItem.SwitchPreference {
        return AppPreferencesItem.SwitchPreference(key, title, description, isChecked)
    }
}