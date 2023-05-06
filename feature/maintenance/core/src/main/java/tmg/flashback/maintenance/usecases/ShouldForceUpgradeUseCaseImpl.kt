package tmg.flashback.maintenance.usecases

import tmg.flashback.maintenance.contract.usecases.ShouldForceUpgradeUseCase
import tmg.flashback.maintenance.repository.MaintenanceRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ShouldForceUpgradeUseCaseImpl @Inject constructor(
    private val repository: MaintenanceRepository
): ShouldForceUpgradeUseCase {
    override fun shouldForceUpgrade(): Boolean {
        return repository.forceUpgrade != null
    }
}