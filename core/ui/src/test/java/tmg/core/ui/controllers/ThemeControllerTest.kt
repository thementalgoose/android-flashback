package tmg.core.ui.controllers

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import tmg.core.prefs.manager.PreferenceManager
import tmg.core.ui.managers.StyleManager
import tmg.core.ui.model.AnimationSpeed
import tmg.core.ui.model.Theme
import tmg.testutils.BaseTest
import tmg.utilities.extensions.isInDayMode

internal class ThemeControllerTest: BaseTest() {

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
        sut.theme = Theme.DAY
        verify {
            mockPreferenceManager.save(keyTheme, Theme.DAY.key)
        }
    }

    @Test
    fun `retrieving theme queries preference manager`() {
        every { mockPreferenceManager.getString(any()) } returns Theme.DAY.key
        initSUT()
        assertEquals(Theme.DAY, sut.theme)
        verify {
            mockPreferenceManager.getString(keyTheme, null)
        }
    }

    @Test
    fun `retrieving theme defaults to DEFAULT when no value found in shared preferences`() {
        every { mockPreferenceManager.getString(any()) } returns null
        initSUT()
        assertEquals(Theme.DEFAULT, sut.theme)
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
    fun `getting style with theme and is in day mode returns expected theme`(themeValue: Theme, isInDayMode: Boolean, expectedThemeValue: Theme) {
        every { mockPreferenceManager.getString(keyTheme) } returns themeValue.key
        every { mockStyleManager.getStyleResource(Theme.DEFAULT) } returns Theme.DEFAULT.ordinal
        every { mockStyleManager.getStyleResource(Theme.DAY) } returns Theme.DAY.ordinal
        every { mockStyleManager.getStyleResource(Theme.NIGHT) } returns Theme.NIGHT.ordinal
//        stubConfiguration(isInDayMode)

        initSUT()

        assertEquals(expectedThemeValue.ordinal, sut.themeStyle)
        verify {
            mockPreferenceManager.getString(keyTheme)
            mockStyleManager.getStyleResource(expectedThemeValue)
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
