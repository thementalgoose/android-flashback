package tmg.core.ui.controllers

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import tmg.core.prefs.manager.PreferenceManager
import tmg.core.ui.managers.StyleManager
import tmg.core.ui.model.AnimationSpeed
import tmg.core.ui.model.Theme

internal class ThemeControllerTest {

    private val mockApplicationContext: Context = mockk(relaxed = true)
    private val mockPreferenceManager: PreferenceManager = mockk(relaxed = true)
    private val mockStyleManager: StyleManager = mockk(relaxed = true)

    private lateinit var sut: ThemeController

    private fun initSUT() {
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
        "NIGHT,false,NIGHT",
        "DEFAULT,true,DAY",
        "DEFAULT,false,NIGHT"
    )
    fun `getting style with theme and is in day mode returns expected theme`(themeValue: Theme, isInDayMode: Boolean, expectedThemeValue: Theme) {
        every { mockPreferenceManager.getString(keyTheme) } returns themeValue.key
        every { mockStyleManager.getStyleResource(Theme.DEFAULT) } returns Theme.DEFAULT.ordinal
        every { mockStyleManager.getStyleResource(Theme.DAY) } returns Theme.DAY.ordinal
        every { mockStyleManager.getStyleResource(Theme.NIGHT) } returns Theme.NIGHT.ordinal
        stubConfiguration(isInDayMode)

        initSUT()

        assertEquals(expectedThemeValue.ordinal, sut.themeStyle)
        verify {
            mockPreferenceManager.getString(keyTheme)
            mockStyleManager.getStyleResource(expectedThemeValue)
        }
    }

    private fun stubConfiguration(dayMode: Boolean) {
        val value = if (dayMode) 32 else 16
        val mockResources: Resources = mockk(relaxed = true)
        val mockConfiguration: Configuration = mockk(relaxed = true)

        every { mockApplicationContext.resources } returns mockResources
        every { mockResources.configuration } returns mockConfiguration
        every { mockConfiguration.uiMode } returns value
    }
    companion object {
        private const val keyTheme: String = "THEME"
        private const val keyAnimationSpeed: String = "BAR_ANIMATION"
    }
}
