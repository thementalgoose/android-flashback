package tmg.flashback.reactiongame.usecases

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.reactiongame.repositories.ReactionGameRepository

internal class IsReactionGameEnabledUseCaseImplTest {

    private val mockReactionGameRepository: ReactionGameRepository = mockk(relaxed = true)

    private lateinit var underTest: IsReactionGameEnabledUseCaseImpl

    private fun initUnderTest() {
        underTest = IsReactionGameEnabledUseCaseImpl(
            reactionGameRepository = mockReactionGameRepository
        )
    }

    @Test
    fun `use case returns value from repository`() {
        every { mockReactionGameRepository.isEnabled } returns true
        initUnderTest()

        assertTrue(underTest.invoke())
        verify {
            mockReactionGameRepository.isEnabled
        }
    }
}