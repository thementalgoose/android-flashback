package tmg.flashback

import androidx.test.espresso.action.ViewActions.scrollTo
import de.mannodermaus.junit5.ActivityScenarioExtension
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.test.KoinTest
import tmg.flashback.di.mockModules
import tmg.flashback.scenarios.settingsScreen
import tmg.flashback.utils.EspressoUtils.clickOn

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

        clickOn(R.id.settings)
        settingsScreen {
            checkRss()
            checkNotifications()
            checkTheme()
            checkCustomisation()
            checkSeasonList()
            checkAbout()
            checkFeedback()
        }

        settingsScreen {
            clickOnPrivacyPolicy()
        }

    }

    @AfterEach
    internal fun tearDown() {
        unloadKoinModules(mockModules)
    }
}