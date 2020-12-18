package tmg.flashback.scenarios

import tmg.flashback.R
import tmg.flashback.utils.EspressoUtils.assertTextDisplayed
import tmg.flashback.utils.EspressoUtils.clickOn
import tmg.flashback.utils.EspressoUtils.clickOnText
import tmg.flashback.utils.EspressoUtils.collapseAppBar
import tmg.flashback.utils.EspressoUtils.withRecyclerView

fun settingsScreen(inside: SettingsScreen.() -> Unit) {
    collapseAppBar()
    inside(SettingsScreen)
}

object SettingsScreen {

    fun checkRss() = apply {
        withRecyclerView(R.id.rvSettings) {
            assertTextDisplayed(R.string.settings_customisation_rss)
            assertTextDisplayed(R.string.settings_customisation_rss_title)
            assertTextDisplayed(R.string.settings_customisation_rss_description)
        }
    }

    fun checkNotifications() = apply {
        withRecyclerView(R.id.rvSettings) {
            assertTextDisplayed(R.string.settings_notifications_title)
            assertTextDisplayed(R.string.settings_notifications_channel_qualifying_title)
            assertTextDisplayed(R.string.settings_notifications_channel_qualifying_description)
            assertTextDisplayed(R.string.settings_notifications_channel_race_title)
            assertTextDisplayed(R.string.settings_notifications_channel_race_description)
        }
    }

    fun checkTheme() = apply {
        withRecyclerView(R.id.rvSettings) {
            assertTextDisplayed(R.string.settings_theme)
            assertTextDisplayed(R.string.settings_theme_theme_title)
            assertTextDisplayed(R.string.settings_theme_theme_description)
        }
    }

    fun checkCustomisation() = apply {
        withRecyclerView(R.id.rvSettings) {
            assertTextDisplayed(R.string.settings_customisation)
            assertTextDisplayed(R.string.settings_bar_animation_animation_title)
            assertTextDisplayed(R.string.settings_bar_animation_animation_description)
            assertTextDisplayed(R.string.settings_customisation_qualifying_delta_title)
            assertTextDisplayed(R.string.settings_customisation_qualifying_delta_description)
            assertTextDisplayed(R.string.settings_customisation_qualifying_grid_penalties_title)
            assertTextDisplayed(R.string.settings_customisation_qualifying_grid_penalties_description)
        }
    }

    fun checkSeasonList() = apply {
        withRecyclerView(R.id.rvSettings) {
            assertTextDisplayed(R.string.settings_season_list)
            assertTextDisplayed(R.string.settings_customisation_season_expanded_title)
            assertTextDisplayed(R.string.settings_customisation_season_expanded_description)
            assertTextDisplayed(R.string.settings_customisation_season_favourited_expanded_title)
            assertTextDisplayed(R.string.settings_customisation_season_favourited_expanded_description)
            assertTextDisplayed(R.string.settings_customisation_season_all_expanded_title)
            assertTextDisplayed(R.string.settings_customisation_season_all_expanded_description)
        }
    }

    fun checkAbout() = apply {
        withRecyclerView(R.id.rvSettings) {
            assertTextDisplayed(R.string.settings_help)
            assertTextDisplayed(R.string.settings_help_about_title)
            assertTextDisplayed(R.string.settings_help_about_description)
            assertTextDisplayed(R.string.settings_help_review_title)
            assertTextDisplayed(R.string.settings_help_review_description)
            assertTextDisplayed(R.string.settings_help_privacy_policy_title)
            assertTextDisplayed(R.string.settings_help_privacy_policy_description)
        }
    }

    fun checkFeedback() = apply {
        withRecyclerView(R.id.rvSettings) {
            assertTextDisplayed(R.string.settings_feedback)
            assertTextDisplayed(R.string.settings_help_crash_reporting_title)
            assertTextDisplayed(R.string.settings_help_crash_reporting_description)
            assertTextDisplayed(R.string.settings_help_suggestions_title)
            assertTextDisplayed(R.string.settings_help_suggestions_description)
            assertTextDisplayed(R.string.settings_help_shake_to_report_title)
            assertTextDisplayed(R.string.settings_help_shake_to_report_description)
        }
    }

    fun clickOnPrivacyPolicy() = apply {
        withRecyclerView(R.id.rvSettings) {
            assertTextDisplayed(R.string.settings_help_privacy_policy_title)
            clickOnText(R.string.settings_help_privacy_policy_title)
        }
    }
}