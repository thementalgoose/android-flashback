package tmg.flashback.eastereggs.usecases

import org.threeten.bp.Month
import tmg.flashback.device.managers.TimeManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IsValentinesDayEnabledUseCase @Inject constructor(
    private val timeManager: TimeManager
) {
    operator fun invoke(): Boolean {
        val now = timeManager.now.toLocalDate()
        return now.month == Month.FEBRUARY && (now.dayOfMonth in 12..14)
    }
}