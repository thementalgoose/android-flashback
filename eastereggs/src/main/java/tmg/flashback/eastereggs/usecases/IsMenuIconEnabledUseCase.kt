package tmg.flashback.eastereggs.usecases

import tmg.flashback.eastereggs.model.MenuKeys
import tmg.flashback.eastereggs.repository.EasterEggsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IsMenuIconEnabledUseCase @Inject constructor(
    private val easterEggsRepository: EasterEggsRepository
) {
    operator fun invoke(): MenuKeys? = easterEggsRepository.menuIcon
}