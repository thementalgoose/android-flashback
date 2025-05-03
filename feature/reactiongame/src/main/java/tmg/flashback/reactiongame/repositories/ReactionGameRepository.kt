package tmg.flashback.reactiongame.repositories

import tmg.flashback.configuration.manager.ConfigManager
import javax.inject.Inject

internal class ReactionGameRepository @Inject constructor(
    private val configManager: ConfigManager
) {
    val isEnabled: Boolean
        get() = configManager.getBoolean(keyReactionGame)

    companion object {
        private const val keyReactionGame: String = "reaction_game"
    }
}