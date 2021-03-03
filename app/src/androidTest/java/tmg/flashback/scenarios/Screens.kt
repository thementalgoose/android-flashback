package tmg.flashback.scenarios

import tmg.flashback.R
import tmg.flashback.di.remoteconfig.MockRemoteConfigRepository
import tmg.flashback.statistics.ui.utils.EspressoUtils.clickOn
import tmg.flashback.statistics.ui.utils.EspressoUtils.collapseAppBar
import tmg.flashback.statistics.ui.utils.EspressoUtils.pressBack
import tmg.flashback.statistics.ui.utils.RecyclerViewUtils

fun startup(block: Home.() -> Unit) {
    block(Home)
}

object Home: RecyclerViewUtils(R.id.dataList) {

    fun goToSeasonList(block: SeasonList.() -> Unit) {
        clickOn(R.id.menuButton)
        block(SeasonList)
        pressBack()
    }

    fun goToRss(block: RSS.() -> Unit) {
        clickOn(R.id.nav_rss)
        block(RSS)
        pressBack()
    }

    fun clickOnRace(raceName: String, block: Race.() -> Unit) {
        clickOnCalendar()
        clickOnListText(raceName)
        collapseAppBar()
        block(Race)
        pressBack()
    }

    fun clickOnDriver(name: String, block: DriverOverview.() -> Unit) {
        clickOnDriver()
        clickOnListText(name)
        collapseAppBar()
        block(DriverOverview)
        pressBack()
    }

    fun clickOnConstructor(name: String, block: ConstructorOverview.() -> Unit) {
        clickOnConstructor()
        clickOnListText(name)
        collapseAppBar()
        block(ConstructorOverview)
        pressBack()
    }

    fun clickOnDataProvidedBanner(block: SettingsAbout.() -> Unit) {
        clickOnListText(MockRemoteConfigRepository.dataProvidedBy)
        block(SettingsAbout)
        pressBack()
    }

    fun clickOnCalendar() = clickOn(R.id.nav_calendar)
    fun clickOnDriver() = clickOn(R.id.nav_drivers)
    fun clickOnConstructor() = clickOn(R.id.nav_constructor)
}

object SeasonList: RecyclerViewUtils(R.id.rvContent) {

    fun goToSettings(block: Settings.() -> Unit) {
        clickOn(R.id.settingsButton)
        collapseAppBar()
        block(Settings)
        pressBack()
    }
}

object Race: RecyclerViewUtils(R.id.rvContent) {

    fun clickOnDriver(name: String, block: DriverOverview.() -> Unit) {
        clickOnRace()
        clickOnListText(name)
        block(DriverOverview)
        pressBack()
    }

    fun clickOnConstructor(name: String, block: ConstructorOverview.() -> Unit) {
        clickOnConstructors()
        clickOnListText(name)
        block(ConstructorOverview)
        pressBack()
    }

    fun clickOnQualifying() = clickOn(R.id.nav_qualifying)
    fun clickOnRace() = clickOn(R.id.nav_race)
    fun clickOnConstructors() = clickOn(R.id.nav_constructor)
}

object DriverOverview: RecyclerViewUtils(R.id.list) {

    fun clickOnYear(year: Int, block: DriverSeasonOverview.() -> Unit) {
        clickOnListText(year.toString())
        block(DriverSeasonOverview)
        pressBack()
    }
}

object DriverSeasonOverview: RecyclerViewUtils(R.id.list) {

    fun clickOnRace(raceName: String, block: Race.() -> Unit) {

        clickOnListText(raceName)
        collapseAppBar()
        block(Race)
        pressBack()
    }
}

object ConstructorOverview: RecyclerViewUtils(R.id.list) {


}

//region Settings

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

object SettingsPrivacyPolicy

object SettingsReleaseNotes

object SettingsAbout

//endregion

//region RSS

object RSSSettings: RecyclerViewUtils(R.id.rvSettings) {

    fun goToRSSConfigure(block: RSSSettingsConfigure.() -> Unit) {
        clickOnListText(R.string.settings_rss_configure_sources_title)
        block(RSSSettingsConfigure)
        pressBack()
    }
}

object RSSSettingsConfigure: RecyclerViewUtils(R.id.configuration)

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

//endregion

