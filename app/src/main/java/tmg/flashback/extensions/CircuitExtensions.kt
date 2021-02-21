package tmg.flashback.extensions

import tmg.flashback.statistics.enums.TrackLayout
import tmg.flashback.data.models.stats.Circuit
import tmg.utilities.extensions.toEnum

val Circuit.circuitIcon: TrackLayout?
    get() = this.id.toEnum<TrackLayout> { it.circuitId }