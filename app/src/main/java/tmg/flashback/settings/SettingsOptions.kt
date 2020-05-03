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
    QUALIFYING_DELTAS("qualifyingDeltas",
        title = R.string.settings_customisation_qualifying_delta_title,
        description = R.string.settings_customisation_qualifying_delta_description
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