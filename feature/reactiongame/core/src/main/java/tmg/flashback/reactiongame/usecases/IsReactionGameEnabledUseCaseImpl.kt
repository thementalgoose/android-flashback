package tmg.flashback.reactiongame.usecases

import tmg.flashback.reactiongame.contract.usecases.IsReactionGameEnabledUseCase
import tmg.flashback.reactiongame.repositories.ReactionGameRepository
import javax.inject.Inject

internal class IsReactionGameEnabledUseCaseImpl @Inject constructor(
    private val reactionGameRepository: ReactionGameRepository
): IsReactionGameEnabledUseCase {
    override fun invoke() = reactionGameRepository.isEnabled
}