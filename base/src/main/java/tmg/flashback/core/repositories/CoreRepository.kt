package tmg.flashback.core.repositories

import tmg.configuration.repository.models.TimeListDisplayType
import tmg.flashback.core.enums.AppHints

interface CoreRepository {

    /**
     * App hints that have been shown in the app
     */
    var appHints: Set<AppHints>
}