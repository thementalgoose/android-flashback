package tmg.flashback.sandbox.usecases

import tmg.flashback.sandbox.model.SandboxMenuItem

interface GetSandboxMenuItemsUseCase {
    operator fun invoke(): List<SandboxMenuItem>
}