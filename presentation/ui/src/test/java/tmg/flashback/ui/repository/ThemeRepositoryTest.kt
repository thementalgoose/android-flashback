package tmg.flashback.ui.repository

import androidx.appcompat.app.AppCompatDelegate
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import tmg.flashback.configuration.manager.ConfigManager
import tmg.flashback.device.managers.BuildConfigManager
import tmg.flashback.prefs.manager.PreferenceManager
import tmg.flashback.ui.model.NightMode
import tmg.flashback.ui.model.Theme

internal class ThemeRepositoryTest {

    private val mockPreferenceManager: PreferenceManager = mockk(relaxed = true)
    private val mockBuildConfigManager: BuildConfigManager = mockk(relaxed = true)

    private lateinit var underTest: ThemeRepository

    private fun initUnderTest() {
        underTest = ThemeRepository(
            buildConfigManager = mockBuildConfigManager,
            preferenceManager = mockPreferenceManager
        )
    }

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
        initUnderTest()
        underTest.theme = Theme.MATERIAL_YOU
        verify {
            mockPreferenceManager.save(keyTheme, Theme.MATERIAL_YOU.key)
        }
    }

    @Test
    fun `retrieving theme queries preference manager`() {
        every { mockPreferenceManager.getString(any()) } returns Theme.MATERIAL_YOU.key
        initUnderTest()
        assertEquals(Theme.MATERIAL_YOU, underTest.theme)
        verify {
            mockPreferenceManager.getString(keyTheme, null)
        }
    }

    @Test
    fun `retrieving theme defaults to DEFAULT when no value found in shared preferences and no monet theme`() {
        every { mockBuildConfigManager.isMonetThemeSupported } returns false
        every { mockPreferenceManager.getString(any()) } returns null
        initUnderTest()
        assertEquals(Theme.DEFAULT, underTest.theme)
        verify {
            mockPreferenceManager.getString(keyTheme, null)
        }
    }

    @Test
    fun `retrieving theme defaults to MATERIAL_YOU when no value found in shared preferences and monet theme`() {
        every { mockBuildConfigManager.isMonetThemeSupported } returns true
        every { mockPreferenceManager.getString(any()) } returns null
        initUnderTest()
        assertEquals(Theme.MATERIAL_YOU, underTest.theme)
        verify {
            mockPreferenceManager.getString(keyTheme, null)
        }
    }

    //endregion

    companion object {
        private const val keyNightMode: String = "THEME" // Used to be theme pref
        private const val keyTheme: String = "THEME_CHOICE" //
        private const val keyAnimationSpeed: String = "BAR_ANIMATION"
    }
}