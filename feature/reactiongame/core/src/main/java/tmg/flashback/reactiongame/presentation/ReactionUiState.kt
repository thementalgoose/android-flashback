package tmg.flashback.reactiongame.presentation

sealed class ReactionUiState {

    data class Game(
        val lights: Int,
        val hasDisplayedSequence: Boolean = false
    ): ReactionUiState()

    data object Start: ReactionUiState()

    data object JumpStart: ReactionUiState()

    data object Missed: ReactionUiState()

    data class Results(
        val tier: ReactionResultTier,
        val timeMillis: Long,
    ): ReactionUiState()
}

enum class ReactionResultTier {
    SUPERHUMAN,
    GREAT,
    GOOD,
    AVERAGE,
    NOT_GOOD,
    POOR
}