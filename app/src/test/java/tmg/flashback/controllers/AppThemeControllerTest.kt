package tmg.flashback.controllers

import android.content.Context
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import tmg.flashback.R
import tmg.flashback.device.repository.SharedPreferenceRepository
import tmg.flashback.shared.ui.model.Theme

internal class AppThemeControllerTest {

    private val mockApplicationContext: Context = mockk(relaxed = true)
    private val mockSharedPreferenceRepository: SharedPreferenceRepository = mockk(relaxed = true)

    private lateinit var sut: AppThemeController

    private fun initSUT() {
        sut = AppThemeController(mockApplicationContext, mockSharedPreferenceRepository)
    }

    @ParameterizedTest
    @CsvSource(
        "DEFAULT,AUTO",
        "DAY,DAY",
        "NIGHT,NIGHT"
    )
    fun `saving theme pref saves value in shared preference repo`(theme: Theme, key: String) {
        initSUT()
        sut.themePref = theme
        verify {
            mockSharedPreferenceRepository.save(keyTheme, key)
        }
    }

    @ParameterizedTest
    @CsvSource(
        "AUTO,DEFAULT",
        "DAY,DAY",
        "NIGHT,NIGHT",
        "test,DEFAULT"
    )
    fun `getting theme pref retrieves value from shared prefs repo`(key: String, theme: Theme) {
        every { mockSharedPreferenceRepository.getString(keyTheme) } returns key

        initSUT()
        assertEquals(theme, sut.themePref)
        verify {
            mockSharedPreferenceRepository.getString(keyTheme)
        }
    }

    @Test
    fun `get style resource for theme default returns 0`() {
        initSUT()

        assertEquals(0, sut.getStyleResource(Theme.DEFAULT))
    }

    @Test
    fun `get style resource for theme day returns day theme`() {
        initSUT()

        assertEquals(R.style.LightTheme, sut.getStyleResource(Theme.DAY))
    }

    @Test
    fun `get style resource for theme night returns night theme`() {
        initSUT()

        assertEquals(R.style.DarkTheme, sut.getStyleResource(Theme.NIGHT))
    }

    companion object {
        private const val keyTheme: String = "THEME"
    }
}