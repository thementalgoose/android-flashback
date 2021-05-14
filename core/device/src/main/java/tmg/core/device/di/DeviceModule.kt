package tmg.core.device.di

import org.koin.dsl.module
import tmg.core.device.controllers.DeviceController
import tmg.core.device.managers.AndroidNetworkConnectivityManager
import tmg.core.device.managers.NetworkConnectivityManager
import tmg.core.device.repository.DeviceRepository

val deviceModule = module {
    single<NetworkConnectivityManager> { AndroidNetworkConnectivityManager(get()) }
    single { DeviceRepository(get()) }
    single { DeviceController(get(), get()) }
}