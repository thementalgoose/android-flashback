package tmg.flashback.debug.di

import org.koin.dsl.module
import tmg.flashback.ads.di.adsModule
import tmg.flashback.debug.DebugNavigationComponent
import tmg.flashback.debug.manager.DebugBaseUrlOverrideManager
import tmg.flashback.statistics.network.manager.BaseUrlLocalOverrideManager

val debugModule = adsModule + module {
    single { DebugNavigationComponent(get()) }
    single<BaseUrlLocalOverrideManager> { DebugBaseUrlOverrideManager(get()) }
}