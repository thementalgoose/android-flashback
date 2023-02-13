package tmg.flashback.eastereggs.usecases

import tmg.flashback.device.managers.TimeManager
import tmg.flashback.eastereggs.BuildConfig
import tmg.flashback.eastereggs.model.MenuIcons
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IsMenuIconEnabledUseCase @Inject constructor(
    private val timeManager: TimeManager
) {
    operator fun invoke(): MenuIcons? {
        val now = timeManager.now
        return MenuIcons.values()
            .firstOrNull { it.isNow(now.toLocalDate()) }
    }
}