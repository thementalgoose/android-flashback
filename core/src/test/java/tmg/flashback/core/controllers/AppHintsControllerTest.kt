package tmg.flashback.core.controllers

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.core.enums.AppHints
import tmg.flashback.core.repositories.CoreRepository

internal class AppHintsControllerTest {

    private var mockCoreRepository: CoreRepository = mockk(relaxed = true)

    private lateinit var sut: AppHintsController

    private fun initSUT() {
        sut = AppHintsController(mockCoreRepository)
    }

    //region Qualifying Long Press

    @Test
    fun `AppHintsManager show qualifying seen`() {

        every { mockCoreRepository.appHints } returns setOf(AppHints.RACE_QUALIFYING_LONG_CLICK)

        initSUT()

        assertFalse(sut.showQualifyingLongPress)
        verify { mockCoreRepository.appHints }
    }

    @Test
    fun `AppHintsManager show qualifying not seen`() {

        every { mockCoreRepository.appHints } returns setOf()

        initSUT()

        assertTrue(sut.showQualifyingLongPress)
        verify { mockCoreRepository.appHints }
    }

    @Test
    fun `AppHintsManager marking show qualifying as seen`() {

        every { mockCoreRepository.appHints } returns setOf()

        initSUT()

        sut.showQualifyingLongPress = true
        verify { mockCoreRepository.appHints = setOf(AppHints.RACE_QUALIFYING_LONG_CLICK) }
    }

    @Test
    fun `AppHintsManager marking show qualifying as not seen`() {

        every { mockCoreRepository.appHints } returns setOf(AppHints.RACE_QUALIFYING_LONG_CLICK)

        initSUT()

        sut.showQualifyingLongPress = false
        verify { mockCoreRepository.appHints = emptySet() }
    }

    //endregion

}