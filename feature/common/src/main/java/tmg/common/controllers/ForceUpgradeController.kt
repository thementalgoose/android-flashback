package tmg.common.controllers

import tmg.common.repository.ForceUpgradeRepository
import tmg.common.repository.model.ForceUpgrade

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