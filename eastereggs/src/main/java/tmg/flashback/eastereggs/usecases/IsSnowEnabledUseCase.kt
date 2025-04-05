package tmg.flashback.eastereggs.usecases

import java.time.format.DateTimeFormatter
import tmg.flashback.device.managers.TimeManager
import tmg.flashback.device.repository.AccessibilityRepository
import tmg.flashback.eastereggs.repository.EasterEggsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IsSnowEnabledUseCase @Inject constructor(
    private val accessibilityRepository: AccessibilityRepository,
    private val easterEggsRepository: EasterEggsRepository,
    private val timeManager: TimeManager
) {

    companion object {
        private const val startMonthAndDate = "12-20"
        private const val endMonthAndDate = "01-14"
    }

    operator fun invoke(): Boolean {
        if (!accessibilityRepository.isAnimationsEnabled) {
            return false
        }

        val monthDay = timeManager.now.format(DateTimeFormatter.ofPattern("MM-dd"))
        if (monthDay >= startMonthAndDate || monthDay <= endMonthAndDate) {
            return true
        }

        return easterEggsRepository.isSnowEnabled
    }
}
