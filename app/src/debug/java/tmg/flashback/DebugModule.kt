package tmg.flashback

import org.koin.dsl.module
import tmg.flashback.debug.manager.DebugBaseUrlLocalOverrideManager
import tmg.flashback.statistics.network.manager.BaseUrlLocalOverrideManager

val debugModule = module {
    single<BaseUrlLocalOverrideManager> { DebugBaseUrlLocalOverrideManager(get()) }
}