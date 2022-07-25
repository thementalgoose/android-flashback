package tmg.flashback.di.navigation

import tmg.flashback.appshortcuts.provider.HomeClassProvider
import tmg.flashback.ui.HomeActivity

class AppHomeClassProvider: HomeClassProvider {
    override fun getHomeActivity(): Class<*> = HomeActivity::class.java
}