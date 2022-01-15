package tmg.flashback.common.controllers

import tmg.flashback.common.repository.ForceUpgradeRepository
import tmg.flashback.common.repository.model.ForceUpgrade

class ForceUpgradeController(
        private val forceUpgradeRepository: ForceUpgradeRepository
) {
    /**
     * Should we show the force upgrade button
     */
    val shouldForceUpgrade: Boolean
        get() = forceUpgradeRepository.forceUpgrade != null

    /**
     * Force upgrade config
     */
    val forceUpgrade: ForceUpgrade?
        get() = forceUpgradeRepository.forceUpgrade
}