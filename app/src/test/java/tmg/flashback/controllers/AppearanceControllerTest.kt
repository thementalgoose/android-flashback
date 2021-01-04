package tmg.flashback.controllers

import android.content.Context
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.repo.enums.BarAnimation
import tmg.flashback.repo.enums.ThemePref
import tmg.flashback.repo.pref.UserRepository
import tmg.flashback.testutils.BaseTest
import tmg.utilities.extensions.isInDayMode

internal class AppearanceControllerTest: BaseTest() {

    private var mockUserRepository: UserRepository = mockk(relaxed = true)
    private var mockContext: Context = mockk(relaxed = true)

    private lateinit var sut: AppearanceController

    private fun initSUT() {
        sut = AppearanceController(mockUserRepository, mockContext)
    }

    //region Theme

    @Test
    fun `AppearanceController current theme reads from pref`() {
        every { mockUserRepository.theme } returns ThemePref.NIGHT
        initSUT()

        assertEquals(ThemePref.NIGHT, sut.currentTheme)
        verify { mockUserRepository.theme }
    }

    @Test
    fun `AppearanceController current theme update saves pref`() {
        initSUT()
        sut.currentTheme = ThemePref.NIGHT
        verify { mockUserRepository.theme = ThemePref.NIGHT }
    }

    @Test
    fun `AppearanceController is light mode in light mode returns true`() {
        every { mockUserRepository.theme } returns ThemePref.DAY
        initSUT()

        assertTrue(sut.isLightMode)
        verify { mockUserRepository.theme }
    }

    @Test
    fun `AppearanceController is dark mode in dark mode returns false`() {
        every { mockUserRepository.theme } returns ThemePref.NIGHT
        initSUT()

        assertFalse(sut.isLightMode)
        verify { mockUserRepository.theme }
    }

    //endregion

    //region Bar animation speed

    @Test
    fun `AppearanceController read bar animation reads from prefs`() {
        every { mockUserRepository.barAnimation } returns BarAnimation.MEDIUM
        initSUT()

        assertEquals(BarAnimation.MEDIUM, sut.barAnimation)
        verify { mockUserRepository.barAnimation }
    }

    @Test
    fun `AppearanceController setting bar animation updates prefs`() {
        every { mockUserRepository.barAnimation } returns BarAnimation.MEDIUM
        initSUT()

        sut.barAnimation = BarAnimation.QUICK
        verify { mockUserRepository.barAnimation = BarAnimation.QUICK }
    }

    //endregion
}