package tmg.flashback.settings

import android.content.Context
import androidx.annotation.StringRes
import tmg.components.prefs.AppPreferencesItem
import tmg.flashback.R

enum class SettingsOptions(
    val key: String,
    @StringRes val title: Int,
    @StringRes val description: Int
) {
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
    SHOW_DRIVERS_POINTS_IN_CONSTRUCTORS("showDriversInConstructorStandings",
        title = R.string.settings_customisation_driver_in_constructor_title,
        description = R.string.settings_customisation_driver_in_constructor_description
    ),
    NEWS("news",
        title = R.string.settings_customisation_news_title,
        description = R.string.settings_customisation_news_description
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

    fun toPref(context: Context): AppPreferencesItem.Preference {
        return AppPreferencesItem.Preference(key, context.getString(title), context.getString(description))
    }

    fun toSwitch(context: Context, isChecked: Boolean): AppPreferencesItem.SwitchPreference {
        return AppPreferencesItem.SwitchPreference(key, context.getString(title), context.getString(description), isChecked)
    }
}