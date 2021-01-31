package tmg.flashback.core.controllers

import android.content.Context
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.core.enums.AnimationSpeed
import tmg.flashback.core.enums.Theme
import tmg.flashback.core.repositories.CoreRepository

internal class AppearanceControllerTest {

    private var mockCoreRepository: CoreRepository = mockk(relaxed = true)
    private var mockContext: Context = mockk(relaxed = true)

    private lateinit var sut: AppearanceController

    private fun initSUT() {
        sut = AppearanceController(mockCoreRepository, mockContext)
    }

    //region Theme

    @Test
    fun `current theme reads from pref`() {
        every { mockCoreRepository.theme } returns Theme.NIGHT
        initSUT()

        assertEquals(Theme.NIGHT, sut.currentTheme)
        verify { mockCoreRepository.theme }
    }

    @Test
    fun `current theme update saves pref`() {
        initSUT()
        sut.currentTheme = Theme.NIGHT
        verify { mockCoreRepository.theme = Theme.NIGHT }
    }

    @Test
    fun `is light mode in light mode returns true`() {
        every { mockCoreRepository.theme } returns Theme.DAY
        initSUT()

        assertTrue(sut.isLightMode)
        verify { mockCoreRepository.theme }
    }

    @Test
    fun `is dark mode in dark mode returns false`() {
        every { mockCoreRepository.theme } returns Theme.NIGHT
        initSUT()

        assertFalse(sut.isLightMode)
        verify { mockCoreRepository.theme }
    }

    //endregion

    //region Animation speed

    @Test
    fun `read animation speed reads from prefs`() {
        every { mockCoreRepository.animationSpeed } returns AnimationSpeed.MEDIUM
        initSUT()

        assertEquals(AnimationSpeed.MEDIUM, sut.animationSpeed)
        verify { mockCoreRepository.animationSpeed }
    }

    @Test
    fun `setting animation speed updates prefs`() {
        every { mockCoreRepository.animationSpeed } returns AnimationSpeed.MEDIUM
        initSUT()

        sut.animationSpeed = AnimationSpeed.QUICK
        verify { mockCoreRepository.animationSpeed = AnimationSpeed.QUICK }
    }

    //endregion
}