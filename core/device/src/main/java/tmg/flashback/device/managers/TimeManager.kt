package tmg.flashback.device.managers

import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TimeManager @Inject constructor() {
    val now: LocalDateTime
        get() = LocalDateTime.now()

    val nowMillis: Long
        get() = System.currentTimeMillis()
}