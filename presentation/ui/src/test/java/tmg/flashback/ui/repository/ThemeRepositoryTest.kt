package tmg.flashback.ui.repository

import androidx.appcompat.app.AppCompatDelegate
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.prefs.manager.PreferenceManager
import tmg.flashback.ui.controllers.ThemeControllerTest
import tmg.flashback.ui.model.NightMode

internal class ThemeRepositoryTest {

    private val mockPreferenceManager: PreferenceManager = mockk(relaxed = true)

    private lateinit var underTest: ThemeRepository

    private fun initUnderTest() {
        underTest = ThemeRepository(mockPreferenceManager)
    }


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

    companion object {
        private const val keyNightMode: String = "THEME" // Used to be theme pref
        private const val keyTheme: String = "THEME_CHOICE" //
        private const val keyAnimationSpeed: String = "BAR_ANIMATION"

        private const val keyMaterialYou: String = "material_you"
    }
}