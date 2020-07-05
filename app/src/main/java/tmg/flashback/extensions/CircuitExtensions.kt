package tmg.flashback.extensions

import tmg.flashback.TrackLayout
import tmg.flashback.repo.models.stats.Circuit
import tmg.utilities.extensions.toEnum

val Circuit.circuitIcon: TrackLayout?
    get() = this.id.toEnum<TrackLayout> { it.circuitId }