package tmg.flashback.device.di

import org.koin.dsl.module
import tmg.flashback.device.controllers.DeviceController
import tmg.flashback.device.managers.AndroidNetworkConnectivityManager
import tmg.flashback.device.managers.NetworkConnectivityManager
import tmg.flashback.device.repository.DeviceRepository

val deviceModule = module {
    single<NetworkConnectivityManager> { AndroidNetworkConnectivityManager(get()) }
    single { DeviceRepository(get()) }
    single { DeviceController(get(), get()) }
}