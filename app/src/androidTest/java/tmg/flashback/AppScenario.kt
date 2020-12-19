package tmg.flashback

import android.content.Intent
import de.mannodermaus.junit5.ActivityScenarioExtension
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.test.KoinTest
import tmg.flashback.di.data.*
import tmg.flashback.di.data.mockConstructorBlue
import tmg.flashback.di.data.mockDriverAlex
import tmg.flashback.di.data.mockDriverBrian
import tmg.flashback.di.data.mockDriverCharlie
import tmg.flashback.di.data.mockDriverDaniel
import tmg.flashback.di.mockModules
import tmg.flashback.di.remoteconfig.MockRemoteConfigRepository
import tmg.flashback.di.rss.mockRssGoogle
import tmg.flashback.scenarios.startup
import tmg.flashback.utils.EspressoUtils.assertViewDisplayed
import tmg.flashback.utils.EspressoUtils.assertTextDisplayed
import tmg.flashback.utils.EspressoUtils.clickOn
import tmg.flashback.utils.EspressoUtils.assertIntentFired
import tmg.flashback.utils.EspressoUtils.collapseAppBar

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class AppScenario: KoinTest {

    @JvmField
    @RegisterExtension
    val scenarioExtension = ActivityScenarioExtension.launch<SplashActivity>()

    @BeforeEach
    fun setUp() {
        loadKoinModules(mockModules)
    }

    @Test
    fun runThroughApp() {

        startup {

            assertTextDisplayedInList(MockRemoteConfigRepository.banner)

            this.clickOnCalendar()

            assertTextDisplayedInList(mockRound1.name)
            assertTextDisplayedInList(mockRound2.name)

            this.clickOnDriver()

            assertTextDisplayedInList(mockDriverAlex.name)
            assertTextDisplayedInList(mockDriverBrian.name)
            assertTextDisplayedInList(mockDriverCharlie.name)
            assertTextDisplayedInList(mockDriverDaniel.name)

            this.clickOnConstructor()

            assertTextDisplayedInList(mockConstructorBlue.name)
            assertTextDisplayedInList(mockConstructorGreen.name)

            this.clickOnRace(mockRound1.name) {

                this.clickOnRace()

                assertTextDisplayedInList(mockDriverAlex.name)
                assertTextDisplayedInList(mockDriverBrian.name)
                assertTextDisplayedInList(mockDriverCharlie.name)
                assertTextDisplayedInList(mockDriverDaniel.name)

                this.clickOnQualifying()

                assertTextDisplayedInList(mockDriverAlex.name)
                assertTextDisplayedInList(mockDriverBrian.name)
                assertTextDisplayedInList(mockDriverCharlie.name)
                assertTextDisplayedInList(mockDriverDaniel.name)

                this.clickOnConstructors()

                assertTextDisplayedInList(mockConstructorGreen.name)
                assertTextDisplayedInList(mockConstructorBlue.name)

                this.clickOnDriver(mockDriverDaniel.name) {

                    assertItemsAtLeast(10)
                    assertTextDisplayed(mockDriverDaniel.name)
                    collapseAppBar()
                    assertTextDisplayedInList(mockSeason.season.toString())

                    this.clickOnYear(mockSeason.season) {

                        assertItemsAtLeast(10)
                        assertTextDisplayed("${mockDriverDaniel.name} ${mockSeason.season}")
                        collapseAppBar()
                        assertTextDisplayedInList(mockRound1.name)
                    }
                }

                this.clickOnConstructor(mockConstructorBlue.name) {

                    assertItemsAtLeast(10)
                    assertTextDisplayed(mockConstructorBlue.name)
                    collapseAppBar()
                    assertTextDisplayedInList(mockSeason.season.toString())
                }
            }

            this.clickOnConstructor(mockConstructorBlue.name) {

                assertItemsAtLeast(10)
            }

            this.clickOnDriver(mockDriverDaniel.name) {

                assertItemsAtLeast(10)
            }

            // TODO: Banner text resolving needs to be injected and is currently being hacked in so can't be tested
//            this.clickOnDataProvidedBanner {
//
//                assertTextDisplayed(R.string.about_name)
//                assertTextDisplayed(R.string.about_desc)
//            }

            this.goToRss {

                assertTextDisplayedInList(mockRssGoogle.title)
                assertTextDisplayedInList(mockRssGoogle.description)

                this.clickOnArtitle(mockRssGoogle.title) {

                    assertViewDisplayed(R.id.share)
                    assertViewDisplayed(R.id.openInBrowser)

                    assertIntentFired(Intent.ACTION_CHOOSER) {

                        clickOn(R.id.share)
                    }
                }

                this.goToRSSSettings {

                    assertTextDisplayedInList(R.string.settings_rss_configure)
                    assertTextDisplayedInList(R.string.settings_rss_appearance_title)
                    assertTextDisplayedInList(R.string.settings_rss_browser)

                    this.goToRSSConfigure {

                        assertTextDisplayedInList(R.string.rss_configure_header_items)
                        assertTextDisplayedInList(R.string.rss_configure_header_add)
                        assertTextDisplayedInList(R.string.rss_configure_header_quick_add)
                        assertItemsAtLeast(10)
                    }
                }
            }

            this.goToSettings {

                assertTextDisplayedInList(R.string.settings_customisation_rss)
                assertTextDisplayedInList(R.string.settings_notifications_title)
                assertTextDisplayedInList(R.string.settings_theme)
                assertTextDisplayedInList(R.string.settings_customisation)
                assertTextDisplayedInList(R.string.settings_season_list)
                assertTextDisplayedInList(R.string.settings_help)
                assertTextDisplayedInList(R.string.settings_feedback)

                this.goToPrivacyPolicy {

                    assertTextDisplayed(R.string.privacy_policy_title)
                }

                this.goToReleaseNotes {

                    assertTextDisplayed(R.string.settings_help_release_notes_title)
                }

                this.goToAboutThisApp {

                    assertTextDisplayed(R.string.about_name)
                    assertTextDisplayed(R.string.about_desc)
                }

                this.goToRSSSettings {

                    assertTextDisplayed(R.string.settings_rss_title)
                }
            }

        }
    }



    @AfterEach
    internal fun tearDown() {
        unloadKoinModules(mockModules)
    }
}