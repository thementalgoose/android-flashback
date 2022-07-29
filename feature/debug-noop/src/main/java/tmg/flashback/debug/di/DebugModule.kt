package tmg.flashback.debug.di

import org.koin.dsl.module
import tmg.flashback.debug.manager.DebugBaseUrlOverrideManager
import tmg.flashback.statistics.network.manager.BaseUrlLocalOverrideManager

val debugModule = module {
    single<BaseUrlLocalOverrideManager> { DebugBaseUrlOverrideManager() }
}