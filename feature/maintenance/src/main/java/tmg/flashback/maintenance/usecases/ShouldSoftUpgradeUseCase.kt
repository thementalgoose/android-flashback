package tmg.flashback.maintenance.usecases

import tmg.flashback.maintenance.repository.MaintenanceRepository
import javax.inject.Inject

class ShouldSoftUpgradeUseCase @Inject constructor(
    private val repository: MaintenanceRepository
) {
    fun shouldSoftUpgrade() = repository.softUpgrade
}