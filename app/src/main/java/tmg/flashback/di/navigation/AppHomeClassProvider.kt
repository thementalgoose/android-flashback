package tmg.flashback.di.navigation

import tmg.flashback.appshortcuts.provider.HomeClassProvider
import tmg.flashback.ui.HomeActivity
import javax.inject.Inject

class AppHomeClassProvider @Inject constructor(): HomeClassProvider {
    override fun getHomeActivity(): Class<*> = HomeActivity::class.java
}