package tmg.flashback.usecases

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.stats.repository.HomeRepository
import tmg.flashback.ui.dashboard.MenuSeasonItem
import tmg.flashback.ui.dashboard.model

internal class GetSeasonsUseCaseTest {

    private val mockHomeRepository: HomeRepository = mockk(relaxed = true)

    private lateinit var underTest: GetSeasonsUseCase

    private fun initUnderTest() {
        underTest = GetSeasonsUseCase(
            homeRepository = mockHomeRepository
        )
    }

    @Test
    fun `supported seasons single item returns single item`() {
        every { mockHomeRepository.supportedSeasons } returns setOf(2020)

        initUnderTest()
        val result = underTest.get(2019)

        val expected = listOf(
            MenuSeasonItem.model(season = 2020, isSelected = false, isLast = true, isFirst = true)
        )
        assertEquals(expected, result)
    }

    @Test
    fun `supported seasons with break shows correct pipe breakdown`() {
        every { mockHomeRepository.supportedSeasons } returns setOf(2020, 2019, 2017, 2016, 2015, 2013)

        initUnderTest()
        val result = underTest.get(2019)

        val expected = listOf(
            MenuSeasonItem.model(season = 2020, isSelected = false, isLast = false, isFirst = true),
            MenuSeasonItem.model(season = 2019, isSelected = true, isLast = true, isFirst = false),
            MenuSeasonItem.model(season = 2017, isSelected = false, isLast = false, isFirst = true),
            MenuSeasonItem.model(season = 2016, isSelected = false, isLast = false, isFirst = false),
            MenuSeasonItem.model(season = 2015, isSelected = false, isLast = true, isFirst = false),
            MenuSeasonItem.model(season = 2013, isSelected = false, isLast = true, isFirst = true)
        )
        assertEquals(expected, result)
    }

    @Test
    fun `supported seasons will return full list`() {
        every { mockHomeRepository.supportedSeasons } returns setOf(2021, 2020, 2019, 2018, 2017, 2016, 2015)

        initUnderTest()
        val result = underTest.get()

        val expected = listOf(
            MenuSeasonItem.model(season = 2021, isSelected = false, isLast = false, isFirst = true),
            MenuSeasonItem.model(season = 2020, isSelected = false, isLast = false, isFirst = false),
            MenuSeasonItem.model(season = 2019, isSelected = false, isLast = false, isFirst = false),
            MenuSeasonItem.model(season = 2018, isSelected = false, isLast = false, isFirst = false),
            MenuSeasonItem.model(season = 2017, isSelected = false, isLast = false, isFirst = false),
            MenuSeasonItem.model(season = 2016, isSelected = false, isLast = false, isFirst = false),
            MenuSeasonItem.model(season = 2015, isSelected = false, isLast = true, isFirst = false)
        )
        assertEquals(expected, result)
    }
}