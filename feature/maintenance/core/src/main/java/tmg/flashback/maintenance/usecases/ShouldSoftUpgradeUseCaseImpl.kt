package tmg.flashback.maintenance.usecases

import tmg.flashback.maintenance.contract.usecases.ShouldSoftUpgradeUseCase
import tmg.flashback.maintenance.repository.MaintenanceRepository
import javax.inject.Inject

internal class ShouldSoftUpgradeUseCaseImpl @Inject constructor(
    private val repository: MaintenanceRepository
): ShouldSoftUpgradeUseCase {
    override fun shouldSoftUpgrade() =
        repository.softUpgrade
}