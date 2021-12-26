package tmg.flashback

import org.koin.dsl.module
import tmg.flashback.managers.AppBaseUrlLocalOverrideManager
import tmg.flashback.statistics.network.manager.BaseUrlLocalOverrideManager

val debugModule = module {
    single<BaseUrlLocalOverrideManager> { AppBaseUrlLocalOverrideManager() }
}