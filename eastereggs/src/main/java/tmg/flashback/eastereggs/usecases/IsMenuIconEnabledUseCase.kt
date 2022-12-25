package tmg.flashback.eastereggs.usecases

import org.threeten.bp.LocalDate
import tmg.flashback.device.managers.TimeManager
import tmg.flashback.eastereggs.model.MenuKeys
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IsMenuIconEnabledUseCase @Inject constructor(
    private val timeManager: TimeManager
) {
    operator fun invoke(): MenuKeys? {
        val now = timeManager.now
        return MenuKeys.values()
            .firstOrNull { it.isNow(now.toLocalDate()) }
    }
}