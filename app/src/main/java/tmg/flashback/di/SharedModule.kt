package tmg.flashback.di

import org.koin.dsl.module
import tmg.flashback.device.di.deviceModule
import tmg.core.device.managers.BuildConfigManager
import tmg.flashback.managers.buildconfig.AppBuildConfigManager

private val sharedModule = module {
    single<tmg.core.device.managers.BuildConfigManager> { AppBuildConfigManager() }
}

val sharedModules = listOf(
    deviceModule,
    sharedModule
)