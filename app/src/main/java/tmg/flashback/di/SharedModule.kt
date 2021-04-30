package tmg.flashback.di

import org.koin.core.module.Module
import org.koin.dsl.module
import tmg.flashback.device.di.deviceModule
import tmg.flashback.device.managers.BuildConfigManager
import tmg.flashback.managers.buildconfig.AppBuildConfigManager

private val sharedModule = module {
    single<BuildConfigManager> { AppBuildConfigManager() }
}

val sharedModules = listOf(
    deviceModule,
    sharedModule
)