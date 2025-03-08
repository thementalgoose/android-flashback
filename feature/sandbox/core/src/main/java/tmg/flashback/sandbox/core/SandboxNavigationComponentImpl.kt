package tmg.flashback.sandbox.core

import android.content.Intent
import org.threeten.bp.Year
import tmg.flashback.sandbox.SandboxNavigationComponent
import tmg.flashback.sandbox.core.presentation.adverts.AdvertsActivity
import tmg.flashback.sandbox.core.presentation.styleguide.StyleGuideComposeActivity
import tmg.flashback.device.ActivityProvider
import tmg.flashback.navigation.ApplicationNavigationComponent
import tmg.flashback.sandbox.core.SandboxItems.MENU_ADVERTS
import tmg.flashback.sandbox.core.SandboxItems.MENU_DEBUG
import tmg.flashback.sandbox.core.SandboxItems.MENU_F1_CONSTRUCTORS
import tmg.flashback.sandbox.core.SandboxItems.MENU_F1_DRIVERS
import tmg.flashback.sandbox.core.SandboxItems.MENU_F1_RESULTS
import tmg.flashback.sandbox.core.SandboxItems.MENU_GITHUB_ACTIONS
import tmg.flashback.sandbox.core.SandboxItems.MENU_STYLEGUIDE
import tmg.flashback.sandbox.core.SandboxItems.MENU_SYNC
import javax.inject.Inject
import androidx.core.net.toUri
import tmg.flashback.sandbox.core.presentation.SandboxActivity

internal class SandboxNavigationComponentImpl @Inject constructor(
    private val activityProvider: ActivityProvider,
    private val navComponent: ApplicationNavigationComponent
): SandboxNavigationComponent {
    override fun navigateTo(id: String) {
        when (id) {
            MENU_STYLEGUIDE -> activityProvider.launch {
                it.startActivity(Intent(it, StyleGuideComposeActivity::class.java))
            }
            MENU_DEBUG -> activityProvider.launch {
                it.startActivity(Intent(it, SandboxActivity::class.java))
            }
            MENU_ADVERTS -> activityProvider.launch {
                it.startActivity(Intent(it, AdvertsActivity::class.java))
            }
            MENU_SYNC -> activityProvider.launch {
                navComponent.syncActivity()
            }
            MENU_F1_RESULTS -> activityProvider.launch {
                val currentYear = Year.now().value
                it.startActivity(Intent(Intent.ACTION_VIEW, "https://www.formula1.com/en/results.html/${currentYear}/races.html".toUri()))
            }
            MENU_F1_DRIVERS -> activityProvider.launch {
                val currentYear = Year.now().value
                it.startActivity(Intent(Intent.ACTION_VIEW, "https://www.formula1.com/en/results.html/${currentYear}/drivers.html".toUri()))
            }
            MENU_F1_CONSTRUCTORS -> activityProvider.launch {
                val currentYear = Year.now().value
                it.startActivity(Intent(Intent.ACTION_VIEW, "https://www.formula1.com/en/results.html/${currentYear}/team.html".toUri()))
            }
            MENU_GITHUB_ACTIONS -> activityProvider.launch {
                it.startActivity(Intent(Intent.ACTION_VIEW, "https://github.com/thementalgoose/api-flashback/actions/workflows/sync-formula1.yml".toUri()))
            }
            else -> { }
        }
    }
}