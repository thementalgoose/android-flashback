package tmg.flashback.controllers

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.controllers.AppHintsController
import tmg.flashback.data.enums.AppHints
import tmg.flashback.data.pref.UserRepository
import tmg.flashback.testutils.BaseTest

internal class AppHintsControllerTest: BaseTest() {

    private var mockUserRepository: UserRepository = mockk(relaxed = true)

    private lateinit var sut: AppHintsController

    private fun initSUT() {
        sut = AppHintsController(mockUserRepository)
    }

    //region Qualifying Long Press

    @Test
    fun `AppHintsManager show qualifying seen`() {

        every { mockUserRepository.appHints } returns setOf(AppHints.RACE_QUALIFYING_LONG_CLICK)

        initSUT()

        assertFalse(sut.showQualifyingLongPress)
        verify { mockUserRepository.appHints }
    }

    @Test
    fun `AppHintsManager show qualifying not seen`() {

        every { mockUserRepository.appHints } returns setOf()

        initSUT()

        assertTrue(sut.showQualifyingLongPress)
        verify { mockUserRepository.appHints }
    }

    @Test
    fun `AppHintsManager marking show qualifying as seen`() {

        every { mockUserRepository.appHints } returns setOf()

        initSUT()

        sut.showQualifyingLongPress = true
        verify { mockUserRepository.appHints = setOf(AppHints.RACE_QUALIFYING_LONG_CLICK) }
    }

    @Test
    fun `AppHintsManager marking show qualifying as not seen`() {

        every { mockUserRepository.appHints } returns setOf(AppHints.RACE_QUALIFYING_LONG_CLICK)

        initSUT()

        sut.showQualifyingLongPress = false
        verify { mockUserRepository.appHints = emptySet() }
    }

    //endregion

}