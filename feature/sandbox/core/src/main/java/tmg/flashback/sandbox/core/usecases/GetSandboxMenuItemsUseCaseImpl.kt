package tmg.flashback.sandbox.core.usecases

import tmg.flashback.sandbox.core.R
import tmg.flashback.sandbox.core.SandboxItems.MENU_ADVERTS
import tmg.flashback.sandbox.core.SandboxItems.MENU_DEBUG
import tmg.flashback.sandbox.core.SandboxItems.MENU_F1_CONSTRUCTORS
import tmg.flashback.sandbox.core.SandboxItems.MENU_F1_DRIVERS
import tmg.flashback.sandbox.core.SandboxItems.MENU_F1_RESULTS
import tmg.flashback.sandbox.core.SandboxItems.MENU_GITHUB_ACTIONS
import tmg.flashback.sandbox.core.SandboxItems.MENU_STYLEGUIDE
import tmg.flashback.sandbox.core.SandboxItems.MENU_SYNC
import tmg.flashback.sandbox.model.SandboxMenuItem
import tmg.flashback.sandbox.usecases.GetSandboxMenuItemsUseCase
import javax.inject.Inject

internal class GetSandboxMenuItemsUseCaseImpl @Inject constructor(): GetSandboxMenuItemsUseCase {
    override operator fun invoke() = listOf(
        SandboxMenuItem(R.string.debug_menu_debug, R.drawable.debug_list_debug, MENU_DEBUG),
        SandboxMenuItem(R.string.debug_menu_styleguide, R.drawable.debug_list_styleguide, MENU_STYLEGUIDE),
        SandboxMenuItem(R.string.debug_menu_ads_config, R.drawable.debug_list_adverts, MENU_ADVERTS),
        SandboxMenuItem(R.string.debug_menu_sync, R.drawable.debug_list_sync, MENU_SYNC),
        SandboxMenuItem(R.string.debug_menu_f1_race, R.drawable.debug_list_formula1, MENU_F1_RESULTS),
        SandboxMenuItem(R.string.debug_menu_f1_drivers, R.drawable.debug_list_formula1, MENU_F1_DRIVERS),
        SandboxMenuItem(R.string.debug_menu_f1_constructors, R.drawable.debug_list_formula1, MENU_F1_CONSTRUCTORS),
        SandboxMenuItem(R.string.debug_menu_github_actions, R.drawable.debug_list_github, MENU_GITHUB_ACTIONS)
    )
}