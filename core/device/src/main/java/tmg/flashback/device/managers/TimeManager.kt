package tmg.flashback.device.managers

import org.threeten.bp.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TimeManager @Inject constructor() {
    val now: LocalDateTime
        get() = LocalDateTime.now()
}