package tmg.flashback.reactiongame.repositories

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import tmg.flashback.configuration.manager.ConfigManager

internal class ReactionGameRepositoryTest {

    private val mockConfigManager: ConfigManager = mockk(relaxed = true)

    private lateinit var underTest: ReactionGameRepository

    private fun initUnderTest() {
        underTest = ReactionGameRepository(
            configManager = mockConfigManager
        )
    }

    @Test
    fun `is enabled calls config manager`() {
        every { mockConfigManager.getBoolean(keyReactionGame) } returns true
        initUnderTest()

        assertTrue(underTest.isEnabled)
        verify {
            mockConfigManager.getBoolean(keyReactionGame)
        }
    }

    companion object {
        private const val keyReactionGame: String = "reaction_game"
    }
}