package tmg.flashback.usecases

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tmg.flashback.results.repository.HomeRepository


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
        val result = underTest.get()

        val expected = listOf(
            2020 to model(isLast = true, isFirst = true)
        )
        assertEquals(expected.toMap(), result)
    }

    @Test
    fun `supported seasons with break shows correct pipe breakdown`() {
        every { mockHomeRepository.supportedSeasons } returns setOf(2020, 2019, 2017, 2016, 2015, 2013)

        initUnderTest()
        val result = underTest.get()

        val expected = listOf(
            2020 to model(isLast = false, isFirst = true),
            2019 to model(isLast = true, isFirst = false),
            2017 to model(isLast = false, isFirst = true),
            2016 to model(isLast = false, isFirst = false),
            2015 to model(isLast = true, isFirst = false),
            2013 to model(isLast = true, isFirst = true)
        )
        assertEquals(expected.toMap(), result)
    }

    @Test
    fun `supported seasons will return full list`() {
        every { mockHomeRepository.supportedSeasons } returns setOf(2021, 2020, 2019, 2018, 2017, 2016, 2015)

        initUnderTest()
        val result = underTest.get()

        val expected = listOf(
            2021 to model(isLast = false, isFirst = true),
            2020 to model(isLast = false, isFirst = false),
            2019 to model(isLast = false, isFirst = false),
            2018 to model(isLast = false, isFirst = false),
            2017 to model(isLast = false, isFirst = false),
            2016 to model(isLast = false, isFirst = false),
            2015 to model(isLast = true, isFirst = false)
        )
        assertEquals(expected.toMap(), result)
    }

    private fun model(isFirst: Boolean, isLast: Boolean) = Pair(IsFirst(isFirst), IsLast(isLast))
}