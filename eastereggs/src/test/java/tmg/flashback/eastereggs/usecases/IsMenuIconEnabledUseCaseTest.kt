package tmg.flashback.eastereggs.usecases

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.threeten.bp.LocalDate
import tmg.flashback.device.managers.TimeManager
import tmg.flashback.eastereggs.model.MenuKeys
import tmg.flashback.eastereggs.repository.EasterEggsRepository

internal class IsMenuIconEnabledUseCaseTest {

    private val mockEasterEggsRepository: EasterEggsRepository = mockk(relaxed = true)

    private lateinit var underTest: IsMenuIconEnabledUseCase

    private fun initUnderTest() {
        underTest = IsMenuIconEnabledUseCase(
            easterEggsRepository = mockEasterEggsRepository
        )
    }

    @Test
    fun `menu icon returns from repo`() {
        every { mockEasterEggsRepository.menuIcon } returns MenuKeys.EASTER

        initUnderTest()
        assertEquals(MenuKeys.EASTER, underTest.invoke())
    }
    @Test
    fun `menu icon returns null from repo`() {
        every { mockEasterEggsRepository.menuIcon } returns null

        initUnderTest()
        assertNull(underTest.invoke())
    }
}