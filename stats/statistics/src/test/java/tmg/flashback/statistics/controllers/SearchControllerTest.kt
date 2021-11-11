package tmg.flashback.statistics.controllers

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.statistics.repository.StatisticsRepository
import tmg.testutils.BaseTest

internal class SearchControllerTest: BaseTest() {

    private val mockStatisticsRepository: StatisticsRepository = mockk(relaxed = true)

    private lateinit var sut: SearchController

    private fun initSUT() {
        sut = SearchController(mockStatisticsRepository)
    }

    @Test
    fun `enabled returns value from repository`() {
        every { mockStatisticsRepository.searchEnabled } returns true
        initSUT()
        assertTrue(sut.enabled)
        verify {
            mockStatisticsRepository.searchEnabled
        }
    }
}