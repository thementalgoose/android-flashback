package tmg.core.ui.controllers

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import tmg.core.prefs.manager.PreferenceManager
import tmg.core.ui.managers.StyleManager
import tmg.core.ui.model.AnimationSpeed
import tmg.core.ui.model.NightMode
import tmg.testutils.BaseTest
import tmg.utilities.extensions.isInDayMode

internal class NightModeControllerTest: BaseTest() {

    private val mockApplicationContext: Context = mockk(relaxed = true)
    private val mockPreferenceManager: PreferenceManager = mockk()
    private val mockStyleManager: StyleManager = mockk()
    private val mockResources: Resources = mockk(relaxed = true)
    private val mockConfiguration: Configuration = mockk(relaxed = true)

    private lateinit var sut: ThemeController

    private fun initSUT() {
        every { mockPreferenceManager.save(any(), any<String>()) } returns Unit
        sut = ThemeController(mockApplicationContext, mockPreferenceManager, mockStyleManager)
    }

    @Test
    fun `saving animation speed writes correct value to preference manager`() {
        initSUT()
        sut.animationSpeed = AnimationSpeed.QUICK
        verify {
            mockPreferenceManager.save(keyAnimationSpeed, AnimationSpeed.QUICK.key)
        }
    }

    @Test
    fun `retrieving animation speed queries preference manager`() {
        every { mockPreferenceManager.getString(any()) } returns AnimationSpeed.MEDIUM.key
        initSUT()
        assertEquals(AnimationSpeed.MEDIUM, sut.animationSpeed)
        verify {
            mockPreferenceManager.getString(keyAnimationSpeed, null)
        }
    }

    @Test
    fun `retrieving animation speed defaults to MEDIUM when no value found in shared preferences`() {
        every { mockPreferenceManager.getString(any()) } returns null
        initSUT()
        assertEquals(AnimationSpeed.MEDIUM, sut.animationSpeed)
        verify {
            mockPreferenceManager.getString(keyAnimationSpeed, null)
        }
    }

    @Test
    fun `saving theme writes correct value to preference manager`() {
        initSUT()
        sut.nightMode = NightMode.DAY
        verify {
            mockPreferenceManager.save(keyTheme, NightMode.DAY.key)
        }
    }

    @Test
    fun `retrieving theme queries preference manager`() {
        every { mockPreferenceManager.getString(any()) } returns NightMode.DAY.key
        initSUT()
        assertEquals(NightMode.DAY, sut.nightMode)
        verify {
            mockPreferenceManager.getString(keyTheme, null)
        }
    }

    @Test
    fun `retrieving theme defaults to DEFAULT when no value found in shared preferences`() {
        every { mockPreferenceManager.getString(any()) } returns null
        initSUT()
        assertEquals(NightMode.DEFAULT, sut.nightMode)
        verify {
            mockPreferenceManager.getString(keyTheme, null)
        }
    }

    @ParameterizedTest(name = "themeStyle when theme={0} and isInDayMode={1} returns query to style manager for {2}")
    @CsvSource(
        "DAY,true,DAY",
        "DAY,false,DAY",
        "NIGHT,true,NIGHT",
        "NIGHT,false,NIGHT"
            // TODO: Get these working!
//        "DEFAULT,true,DAY",
//        "DEFAULT,false,NIGHT"
    )
    fun `getting style with theme and is in day mode returns expected theme`(nightModeValue: NightMode, isInDayMode: Boolean, expectedNightModeValue: NightMode) {
        every { mockPreferenceManager.getString(keyTheme) } returns nightModeValue.key
        every { mockStyleManager.getStyleResource(NightMode.DEFAULT) } returns NightMode.DEFAULT.ordinal
        every { mockStyleManager.getStyleResource(NightMode.DAY) } returns NightMode.DAY.ordinal
        every { mockStyleManager.getStyleResource(NightMode.NIGHT) } returns NightMode.NIGHT.ordinal
//        stubConfiguration(isInDayMode)

        initSUT()

        assertEquals(expectedNightModeValue.ordinal, sut.themeStyle)
        verify {
            mockPreferenceManager.getString(keyTheme)
            mockStyleManager.getStyleResource(expectedNightModeValue)
        }
    }

    private fun stubConfiguration(dayMode: Boolean) {
        val value = if (dayMode) 32 else 16

        every { mockResources.configuration } returns mockConfiguration
        mockkStatic(Configuration::class)
        every { Configuration.UI_MODE_NIGHT_MASK } returns 48
        every { Configuration.UI_MODE_NIGHT_YES } returns 32
        every { Configuration.UI_MODE_NIGHT_NO } returns 16
        every { mockConfiguration.uiMode } returns value


        every { mockApplicationContext.isInDayMode(any()) } returns dayMode
    }
    companion object {
        private const val keyTheme: String = "THEME"
        private const val keyAnimationSpeed: String = "BAR_ANIMATION"
    }
}
