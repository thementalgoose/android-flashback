package tmg.flashback.upnext.repository

import tmg.configuration.manager.ConfigManager
import tmg.flashback.upnext.repository.converters.convert
import tmg.flashback.upnext.repository.json.UpNextJson
import tmg.flashback.upnext.repository.model.UpNextSchedule

class UpNextRepository(
    private val configManager: ConfigManager
) {

    companion object {
        private const val keyUpNext: String = "up_next"
    }

    val upNext: List<UpNextSchedule>
        get() = configManager
                .getJson<UpNextJson>(keyUpNext)
                ?.convert()
                ?: emptyList()
}