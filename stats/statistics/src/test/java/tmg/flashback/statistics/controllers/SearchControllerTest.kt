package tmg.flashback.statistics.controllers

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.statistics.repository.HomeRepository
import tmg.testutils.BaseTest

internal class SearchControllerTest: BaseTest() {

    private val mockHomeRepository: HomeRepository = mockk(relaxed = true)

    private lateinit var sut: SearchController

    private fun initSUT() {
        sut = SearchController(mockHomeRepository)
    }

    @Test
    fun `enabled returns value from repository`() {
        every { mockHomeRepository.searchEnabled } returns true
        initSUT()
        assertTrue(sut.enabled)
        verify {
            mockHomeRepository.searchEnabled
        }
    }
}