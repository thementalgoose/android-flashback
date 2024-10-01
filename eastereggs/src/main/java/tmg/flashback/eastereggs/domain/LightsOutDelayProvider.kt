package tmg.flashback.eastereggs.domain

import javax.inject.Inject
import kotlin.random.Random

class LightsOutDelayProvider @Inject constructor() {
    fun getDelay(): Long {
        return Random.Default.nextLong(500, 2500)
    }
}