package tmg.flashback.eastereggs.usecases

import tmg.flashback.device.repository.AccessibilityRepository
import tmg.flashback.eastereggs.repository.EasterEggsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IsSnowEnabledUseCase @Inject constructor(
    private val accessibilityRepository: AccessibilityRepository,
    private val easterEggsRepository: EasterEggsRepository
) {
    operator fun invoke(): Boolean {
        if (!accessibilityRepository.isAnimationsEnabled) {
            return false
        }

        return easterEggsRepository.isSnowEnabled
    }
}