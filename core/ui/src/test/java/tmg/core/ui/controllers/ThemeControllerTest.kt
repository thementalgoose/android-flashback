package tmg.core.ui.controllers

import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import tmg.configuration.manager.ConfigManager
import tmg.core.prefs.manager.PreferenceManager
import tmg.core.ui.managers.StyleManager
import tmg.core.ui.model.AnimationSpeed
import tmg.core.ui.model.NightMode
import tmg.core.ui.model.Theme
import tmg.testutils.BaseTest

internal class ThemeControllerTest: BaseTest() {

    private val mockPreferenceManager: PreferenceManager = mockk()
    private val mockStyleManager: StyleManager = mockk()
    private val mockConfigManager: ConfigManager = mockk()

    private lateinit var sut: ThemeController

    private fun initSUT() {
        every { mockPreferenceManager.save(any(), any<String>()) } returns Unit
        sut = ThemeController(mockPreferenceManager, mockConfigManager, mockStyleManager)
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
    fun `saving night mode writes correct value to preference manager`() {
        initSUT()
        sut.nightMode = NightMode.DAY
        verify {
            mockPreferenceManager.save(keyNightMode, NightMode.DAY.key)
        }
    }

    @Test
    fun `retrieving night mode queries preference manager`() {
        every { mockPreferenceManager.getString(any()) } returns NightMode.DAY.key
        initSUT()
        assertEquals(NightMode.DAY, sut.nightMode)
        verify {
            mockPreferenceManager.getString(keyNightMode, null)
        }
    }

    @Test
    fun `retrieving night mode defaults to DEFAULT when no value found in shared preferences`() {
        every { mockPreferenceManager.getString(any()) } returns null
        initSUT()
        assertEquals(NightMode.DEFAULT, sut.nightMode)
        verify {
            mockPreferenceManager.getString(keyNightMode, null)
        }
    }

    @Test
    fun `saving theme writes correct value to preference manager`() {
        every { mockConfigManager.getBoolean(keyMaterialYou) } returns true
        initSUT()
        sut.theme = Theme.MATERIAL_YOU
        verify {
            mockPreferenceManager.save(keyTheme, Theme.MATERIAL_YOU.key)
        }
    }

    @Test
    fun `retrieving theme queries preference manager`() {
        every { mockPreferenceManager.getString(any()) } returns Theme.MATERIAL_YOU.key
        every { mockConfigManager.getBoolean(keyMaterialYou) } returns true
        initSUT()
        assertEquals(Theme.MATERIAL_YOU, sut.theme)
        verify {
            mockPreferenceManager.getString(keyTheme, null)
        }
    }

    @Test
    fun `retrieving theme defaults to DEFAULT when no value found in shared preferences`() {
        every { mockPreferenceManager.getString(any()) } returns null
        every { mockConfigManager.getBoolean(keyMaterialYou) } returns true
        initSUT()
        assertEquals(Theme.DEFAULT, sut.theme)
        verify {
            mockPreferenceManager.getString(keyTheme, null)
        }
    }

    @Test
    fun `retrieving theme with theme picker disabled just returns default`() {
        every { mockConfigManager.getBoolean(keyMaterialYou) } returns false
        initSUT()
        assertEquals(Theme.DEFAULT, sut.theme)
        verify(exactly = 0) {
            mockPreferenceManager.getString(any())
        }
    }






    @Test
    fun `enable material you comes from theme picker`() {
        every { mockConfigManager.getBoolean(keyMaterialYou) } returns true
        initSUT()
        assertTrue(sut.enableThemePicker)
        verify {
            mockConfigManager.getBoolean(keyMaterialYou)
        }
    }

    @Test
    fun `disable material you comes from theme picker`() {
        every { mockConfigManager.getBoolean(keyMaterialYou) } returns false
        initSUT()
        assertFalse(sut.enableThemePicker)
        verify {
            mockConfigManager.getBoolean(keyMaterialYou)
        }
    }

    @ParameterizedTest
    @CsvSource(
        "DEFAULT,DEFAULT",
        "DEFAULT,DAY",
        "DEFAULT,NIGHT",
        "MATERIAL_YOU,DEFAULT",
        "MATERIAL_YOU,DAY",
        "MATERIAL_YOU,NIGHT",
    )
    fun `themeStyle calls style manager properly`(theme: Theme, nightMode: NightMode) {

        every { mockPreferenceManager.getString(keyTheme) } returns theme.key
        every { mockPreferenceManager.getString(keyNightMode) } returns nightMode.key
        every { mockConfigManager.getBoolean(keyMaterialYou) } returns true

        every { mockStyleManager.getStyleResource(theme, nightMode) } returns theme.ordinal * nightMode.ordinal

        initSUT()
        assertEquals(theme.ordinal * nightMode.ordinal, sut.themeStyle)

        verify {
            mockStyleManager.getStyleResource(theme, nightMode)
        }
    }

    companion object {
        private const val keyNightMode: String = "THEME" // Used to be theme pref
        private const val keyTheme: String = "THEME_CHOICE" //
        private const val keyAnimationSpeed: String = "BAR_ANIMATION"

        private const val keyMaterialYou: String = "material_you"
    }
}
