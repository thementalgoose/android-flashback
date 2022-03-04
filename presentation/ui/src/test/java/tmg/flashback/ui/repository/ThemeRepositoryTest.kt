package tmg.flashback.ui.repository

import androidx.appcompat.app.AppCompatDelegate
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import tmg.flashback.configuration.manager.ConfigManager
import tmg.flashback.prefs.manager.PreferenceManager
import tmg.flashback.ui.managers.StyleManager
import tmg.flashback.ui.model.AnimationSpeed
import tmg.flashback.ui.model.NightMode
import tmg.flashback.ui.model.Theme

internal class ThemeRepositoryTest {

    private val mockPreferenceManager: PreferenceManager = mockk(relaxed = true)
    private val mockConfigManager: ConfigManager = mockk(relaxed = true)

    private lateinit var underTest: ThemeRepository

    private fun initUnderTest() {
        underTest = ThemeRepository(
            mockPreferenceManager,
            mockConfigManager
        )
    }

    //region Animation Speed

    @Test
    fun `saving animation speed writes correct value to preference manager`() {
        initUnderTest()
        underTest.animationSpeed = AnimationSpeed.QUICK
        verify {
            mockPreferenceManager.save(keyAnimationSpeed, AnimationSpeed.QUICK.key)
        }
    }

    @Test
    fun `retrieving animation speed queries preference manager`() {
        every { mockPreferenceManager.getString(any()) } returns AnimationSpeed.MEDIUM.key
        initUnderTest()
        assertEquals(AnimationSpeed.MEDIUM, underTest.animationSpeed)
        verify {
            mockPreferenceManager.getString(keyAnimationSpeed, null)
        }
    }

    //endregion

    //region Night Mode

    @Test
    fun `saving night mode writes correct value to preference manager`() {
        initUnderTest()
        underTest.nightMode = NightMode.DAY
        mockkStatic(AppCompatDelegate::class)
        verify {
            mockPreferenceManager.save(keyNightMode, NightMode.DAY.key)
        }
    }

    @Test
    fun `retrieving night mode queries preference manager`() {
        every { mockPreferenceManager.getString(any()) } returns NightMode.DAY.key
        initUnderTest()
        assertEquals(NightMode.DAY, underTest.nightMode)
        verify {
            mockPreferenceManager.getString(keyNightMode, null)
        }
    }

    @Test
    fun `retrieving night mode defaults to DEFAULT when no value found in shared preferences`() {
        every { mockPreferenceManager.getString(any()) } returns null
        initUnderTest()
        assertEquals(NightMode.DEFAULT, underTest.nightMode)
        verify {
            mockPreferenceManager.getString(keyNightMode, null)
        }
    }

    //endregion

    //region Theme

    @Test
    fun `saving theme writes correct value to preference manager`() {
        every { mockConfigManager.getBoolean(keyMaterialYou) } returns true
        initUnderTest()
        underTest.theme = Theme.MATERIAL_YOU
        verify {
            mockPreferenceManager.save(keyTheme, Theme.MATERIAL_YOU.key)
        }
    }

    @Test
    fun `retrieving theme queries preference manager`() {
        every { mockPreferenceManager.getString(any()) } returns Theme.MATERIAL_YOU.key
        every { mockConfigManager.getBoolean(keyMaterialYou) } returns true
        initUnderTest()
        assertEquals(Theme.MATERIAL_YOU, underTest.theme)
        verify {
            mockPreferenceManager.getString(keyTheme, null)
        }
    }

    @Test
    fun `retrieving theme defaults to DEFAULT when no value found in shared preferences`() {
        every { mockPreferenceManager.getString(any()) } returns null
        every { mockConfigManager.getBoolean(keyMaterialYou) } returns true
        initUnderTest()
        assertEquals(Theme.DEFAULT, underTest.theme)
        verify {
            mockPreferenceManager.getString(keyTheme, null)
        }
    }

    @Test
    fun `retrieving theme with theme picker disabled just returns default`() {
        every { mockConfigManager.getBoolean(keyMaterialYou) } returns false
        initUnderTest()
        assertEquals(Theme.DEFAULT, underTest.theme)
        verify(exactly = 0) {
            mockPreferenceManager.getString(any())
        }
    }

    //endregion

    //region Theme Style

    @Test
    fun `enable material you comes from theme picker`() {
        every { mockConfigManager.getBoolean(keyMaterialYou) } returns true
        initUnderTest()
        assertTrue(underTest.enableThemePicker)
        verify {
            mockConfigManager.getBoolean(keyMaterialYou)
        }
    }

    @Test
    fun `disable material you comes from theme picker`() {
        every { mockConfigManager.getBoolean(keyMaterialYou) } returns false
        initUnderTest()
        assertFalse(underTest.enableThemePicker)
        verify {
            mockConfigManager.getBoolean(keyMaterialYou)
        }
    }
    
    //endregion

    companion object {
        private const val keyNightMode: String = "THEME" // Used to be theme pref
        private const val keyTheme: String = "THEME_CHOICE" //
        private const val keyAnimationSpeed: String = "BAR_ANIMATION"

        private const val keyMaterialYou: String = "material_you"
    }
}