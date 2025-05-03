package tmg.flashback.maintenance.usecases

import tmg.flashback.maintenance.repository.MaintenanceRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShouldForceUpgradeUseCase @Inject constructor(
    private val repository: MaintenanceRepository
) {
    fun shouldForceUpgrade(): Boolean {
        return repository.forceUpgrade != null
    }
}