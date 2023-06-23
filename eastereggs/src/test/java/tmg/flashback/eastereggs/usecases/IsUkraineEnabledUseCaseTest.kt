package tmg.flashback.eastereggs.usecases

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.eastereggs.repository.EasterEggsRepository

internal class IsUkraineEnabledUseCaseTest {

    private val mockEasterEggsRepository: EasterEggsRepository = mockk(relaxed = true)

    private lateinit var underTest: IsUkraineEnabledUseCase

    private fun initUnderTest() {
        underTest = IsUkraineEnabledUseCase(
            easterEggsRepository = mockEasterEggsRepository
        )
    }

    @Test
    fun `is enabled returns status from repository`() {
        every { mockEasterEggsRepository.isUkraineEnabled } returns true

        initUnderTest()
        assertTrue(underTest())

        verify {
            mockEasterEggsRepository.isUkraineEnabled
        }
    }
}