package tmg.flashback.scenarios

import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import tmg.flashback.R
import tmg.flashback.di.mockModules
import tmg.flashback.utils.EspressoUtils.clickOn
import tmg.flashback.utils.EspressoUtils.collapseAppBar
import tmg.flashback.utils.EspressoUtils.pressBack
import tmg.flashback.utils.RecyclerViewUtils

fun startup(block: Home.() -> Unit) {
    block(Home)
}

object Home {

    fun goToSettings(block: Settings.() -> Unit) {
        clickOn(R.id.settings)
        collapseAppBar()
        block(Settings)
        pressBack()
    }

    fun goToRss(block: RSS.() -> Unit) {
        clickOn(R.id.nav_rss)
        block(RSS)
        pressBack()
    }
}

object Settings: RecyclerViewUtils(R.id.rvSettings) {

    fun goToPrivacyPolicy(block: SettingsPrivacyPolicy.() -> Unit) {
        clickOnListText(R.string.settings_help_privacy_policy_title)
        collapseAppBar()
        block(SettingsPrivacyPolicy)
        pressBack()
    }

    fun goToReleaseNotes(block: SettingsReleaseNotes.() -> Unit) {
        clickOnListText(R.string.settings_help_release_notes_title)
        collapseAppBar()
        block(SettingsReleaseNotes)
        pressBack()
    }

    fun goToAboutThisApp(block: SettingsAbout.() -> Unit) {
        clickOnListText(R.string.settings_help_about_title)
        block(SettingsAbout)
        pressBack()
    }

    fun goToRSSSettings(block: RSSSettings.() -> Unit) {
        clickOnListText(R.string.settings_customisation_rss_title)
        block(RSSSettings)
        pressBack()
    }
}

object SettingsPrivacyPolicy {

}

object SettingsReleaseNotes {

}

object SettingsAbout {

}

object RSSSettings: RecyclerViewUtils(R.id.rvSettings) {

    fun goToRSSConfigure(block: RSSSettingsConfigure.() -> Unit) {
        clickOnListText(R.string.settings_rss_configure_sources_title)
        block(RSSSettingsConfigure)
        pressBack()
    }
}

object RSSSettingsConfigure: RecyclerViewUtils(R.id.configuration) {


}

object RSS: RecyclerViewUtils(R.id.list) {

    fun clickOnArtitle(title: String, block: RSSBrowser.() -> Unit) {
        clickOnListText(title)
        block(RSSBrowser)
        pressBack()
    }

    fun goToRSSSettings(block: RSSSettings.() -> Unit) {
        clickOn(R.id.settings)
        block(RSSSettings)
        pressBack()
    }
}


object RSSBrowser

