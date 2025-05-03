package tmg.flashback.reactiongame.usecases

import tmg.flashback.reactiongame.repositories.ReactionGameRepository
import javax.inject.Inject

class IsReactionGameEnabledUseCase @Inject constructor(
    private val reactionGameRepository: ReactionGameRepository
) {
    operator fun invoke() = reactionGameRepository.isEnabled
}