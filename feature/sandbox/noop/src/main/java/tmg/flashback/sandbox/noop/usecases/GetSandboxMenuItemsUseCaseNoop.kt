package tmg.flashback.sandbox.noop.usecases

import tmg.flashback.sandbox.model.SandboxMenuItem
import tmg.flashback.sandbox.usecases.GetSandboxMenuItemsUseCase
import javax.inject.Inject

class GetSandboxMenuItemsUseCaseNoop @Inject constructor(): GetSandboxMenuItemsUseCase {
    override operator fun invoke(): List<SandboxMenuItem> = emptyList()
}